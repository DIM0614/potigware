package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import br.ufrn.dimap.middleware.remotting.interfaces.Callback;
import br.ufrn.dimap.middleware.extension.interfaces.ClientProtocolPlugIn;
import br.ufrn.dimap.middleware.remotting.interfaces.Marshaller;
import br.ufrn.dimap.middleware.remotting.interfaces.PollObject;

/**
 * Represents the default protocol to the Client Request Handler,
 * which handles networking synchronous communication
 * inside client applications.
 * 
 * Allows to set limit the number of threads connecting to server and
 * the time which connections will be cached.
 * 
 * The default format of messages is:
 * 
 * First, one byte for a character which tells the type of the message.
 * Then, if applicable, 4 bytes to tell the size of the message, and finally 
 * message itself.
 * 
 * The possible values for the first character are:
 * 
 * a - Asynchronous request followed by the request, tells the server there's no
 *     need to send the response
 * c - Confirmation from the server, doesn't have message after it
 * d - Disconnect, doesn't have message after it
 * e - Remote error, the message is the error message
 * q - Query from the client followed by the request, expects a response
 * r - Response from the server with the marshaled results
 * 
 * @author victoragnez
 */
public class DefaultClientProtocol implements ClientProtocolPlugIn {
	
	/**
	 * ExecutorService to limit number of threads connecting to server
	 */
	protected final ThreadPoolExecutor tasksExecutor;
	
	/**
	 * Maps the address to the available connections
	 */
	protected final Map<String, Queue<Connection> > cache = new ConcurrentHashMap<String, Queue<Connection> >();
	
	/**
	 * Queue with available connections to close the old unused ones.
	 */
	protected final Queue<WrappedConnection> oldConnections = new ConcurrentLinkedQueue<WrappedConnection>();
	
	/**
	 * Set of all open connections
	 */
	protected final Set<Connection> allConnections = ConcurrentHashMap.<Connection>newKeySet();
	
	/**
	 * Maximum time a connection can be alive and not used
	 */
	protected final long timeLimit;
	
	/**
	 * Avoids racing when adding a new queue to the cache
	 */
	protected final WrappedPut synchronizedPut = new WrappedPut();
	
	/**
	 * Marshaller to deserialize messages
	 */
	protected final Marshaller marshaller = new XMLMarshaller();
	
	/**
	 * Default constructor with maximum number of threads set to 1000
	 */
	public DefaultClientProtocol() {
		this(1000);
	}
	
	/**
	 * Creates the client protocol with maximum number of threads and
	 * sets time limit of caching connections to 1s
	 * @param maxConnections maximum number of threads
	 */
	public DefaultClientProtocol(int maxConnections) {
		this(maxConnections, 1000L);
	}
	
	/**
	 * Creates the client protocol with maximum number of threads and
	 * the time limit (in milliseconds) of keeping connections alive to cache
	 * @param maxConnections maximum number of threads
	 */
	public DefaultClientProtocol(int maxConnections, long timeLimit) {
		if(timeLimit < 0) {
			throw new IllegalArgumentException("timeLimit cannot be negative, got " + timeLimit);
		}
		if(maxConnections <= 0) {
			throw new IllegalArgumentException("maxConnections must be positive, got " + maxConnections);
		}
		tasksExecutor = new ThreadPoolExecutor(maxConnections + 1, maxConnections + 1,
				timeLimit, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		tasksExecutor.submit(() -> deleteOldConnections());
		this.timeLimit = timeLimit; 
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugin#send(java.lang.String, int, java.io.ByteArrayOutputStream)
	 */
	@Override
	public ByteArrayInputStream send(String host, int port, ByteArrayOutputStream msg) throws RemoteError {
		try {
			return tasksExecutor.submit(() -> sendAndCache(host, port, msg, true) ).get();
		} catch (Exception e) {
			throw new RemoteError(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugin#send(java.lang.String, int, java.io.ByteArrayOutputStream, br.ufrn.dimap.middleware.remotting.interfaces.Callback)
	 */
	@Override
	public void send(String host, int port, ByteArrayOutputStream msg, Callback callback) throws RemoteError {
		tasksExecutor.submit(() -> sendAndCallback(host, port, msg, callback) );
	}

	
	@Override
	public void send(String host, int port, ByteArrayOutputStream msg, boolean waitConfirmation)
			throws RemoteError {
		if(waitConfirmation) {
			try {
				tasksExecutor.submit(() -> sendAndCache(host, port, msg, false) ).get();
				return;
			} catch (Exception e) {
				throw new RemoteError(e);
			}
		} else {
			tasksExecutor.submit(() -> sendUDP(host, port, msg) );
		}
	}
	
	@Override
	public void send(String host, int port, ByteArrayOutputStream msg, PollObject pollObject)
			throws RemoteError {
		tasksExecutor.submit(() -> sendAndPollObject(host, port, msg, pollObject) );
	}

	/**
	 * Sends the data using a cached connection if available, and caches after
	 * sending and receiving the server reply
	 * 
	 * @param host the host to send the data
	 * @param port the port to send the data
	 * @param msg the message to be sent
	 * @param waitResponse should be true if it's expected to receive the server's response
	 * @return the server reply
	 * @throws RemoteError if any error occur
	 */
	protected ByteArrayInputStream sendAndCache(String host, int port, ByteArrayOutputStream msg, boolean waitResponse) throws RemoteError {
		Connection con = null;
		String fullAddr = host + ":" + port;
		
		while(cache.get(fullAddr) != null && (con = cache.get(fullAddr).poll()) != null) {
			if(!con.use()) {
				con = null;
				continue;
			}
			break;
		}
		
		if(con == null) {
			con = new Connection(host, port);
			con.use();
			allConnections.add(con);
		}
		
		ByteArrayInputStream ret;
		RemoteError error = null;
		
		byte[] byteMsg = msg.toByteArray();
		
		try {
			DataOutputStream outToServer = con.getOutput();
			outToServer.writeByte((byte)(waitResponse ? 'q' : 'a'));
			outToServer.writeInt(byteMsg.length);
			outToServer.write(byteMsg);
			
			if(waitResponse) {
				DataInputStream inFromServer = con.getInput();
				char kind = (char)inFromServer.read();
				
				if(kind != 'r' && kind != 'e') {
					throw new IOException("Invalid response from server");
				}
				
				int length = inFromServer.readInt();
				byte[] byteAns = new byte[length];
				
				inFromServer.readFully(byteAns, 0, byteAns.length);
				
				if(kind == 'e') {
					String errorMsg = "";
					for(byte b : byteAns) {
						errorMsg += (char)b;
					}
					error = new RemoteError(errorMsg);
					ret = null;
				}
				
				else {
					ret = new ByteArrayInputStream(byteAns);
				}
			}
			else {
				DataInputStream inFromServer = con.getInput();
				
				char kind = (char)inFromServer.read();
				
				if(kind != 'c') {
					throw new IOException("Invalid response from server");
				}
				
				ret = null;
			}
			
		} catch (IOException e) {
			removeConnection(con);
			throw new RemoteError(e);
		}
		
		if(cache.get(fullAddr) == null)
			synchronizedPut.put(fullAddr);
		
		long newDeathTime = System.currentTimeMillis() + timeLimit;

		con.finish();
		con.setCurrentDeathTime(newDeathTime);
		
		cache.get(fullAddr).add(con);
		
		WrappedConnection w = new WrappedConnection(newDeathTime, con);
		oldConnections.add(w);
		
		if(error != null) {
			throw error;
		}
		
		return ret;
	}

	/**
	 * Sends the request and uses the response as input for callback method
	 * @param host the host to send the data
	 * @param port the port to send the data
	 * @param msg the message to be sent
	 * @param callback callback object whose method will be called after the request
	 */
	protected void sendAndCallback(String host, int port, ByteArrayOutputStream msg, Callback callback) {
		try {
			ByteArrayInputStream inputStream = sendAndCache(host, port, msg, true);
			Object returnValue = this.marshaller.unmarshal(inputStream, Object.class);
			if(returnValue instanceof VoidObject)
				returnValue = null;
			callback.onResult(returnValue);
		} catch (MarshallerException e) {
			callback.onError(new RemoteError(e));
		} catch (RemoteError e) {
			callback.onError(e);
		}
	}
	
	/**
	 * Sends via UDP the message with no guarantee and does not throw any exception
	 * @param host the host to send the data
	 * @param port the port to send the data
	 * @param msg the message to be sent
	 */
	protected void sendUDP(String host, int port, ByteArrayOutputStream msg) {
		try {
			byte[] byteMsg = msg.toByteArray();
			InetAddress ipAddress = InetAddress.getByName(host);
			DatagramPacket packet = new DatagramPacket(byteMsg, byteMsg.length, ipAddress, port);
			DatagramSocket udpSocket = new DatagramSocket();
			udpSocket.send(packet);
			udpSocket.close();
		} catch(Exception e) { }
	}
	
	/**
	 * Sends the request and stores the response in the pollObject
	 * @param host the host to send the data
	 * @param port the port to send the data
	 * @param msg the message to be sent
	 * @param pollObject the pollObject to store the response
	 */
	protected void sendAndPollObject(String host, int port, ByteArrayOutputStream msg, PollObject pollObject) {
		try {
			ByteArrayInputStream inputStream = sendAndCache(host, port, msg, true);
			try {
				Class<?> expectedType = pollObject.getResultType();
				if (expectedType == null)
					expectedType = Object.class;
					
				Object returnValue = this.marshaller.unmarshal(inputStream, expectedType);
				if(returnValue instanceof VoidObject)
					returnValue = null;
				pollObject.storeResult(returnValue);
			} catch(MarshallerException e) {
				throw new RemoteError(e);
			}
		} catch (RemoteError e) {
			pollObject.onError(e);
		}
	}
	
	/**
	 * Stop all threads and closes sockets
	 */
	public void shutdown() {
		tasksExecutor.shutdownNow();
		while(!allConnections.isEmpty()) {
			try {
				Connection con;
				synchronized(allConnections) {
					con = allConnections.iterator().next();
				}
				removeConnection(con);
			} catch(Exception e) { }
		}
	}
	
	/**
	 * Closes connection
	 * @param con the connection to close
	 */
	protected void removeConnection(Connection con) {
		if(con == null)
			return;
		try {
			con.getOutput().writeByte((byte)'d');
		} catch(IOException e) { }
		try {
			con.close();
		} catch(IOException e) { }
		allConnections.remove(con);
	}
	
	/*
	 * Delete connections that have not been used after timeLimit milliseconds
	 */
	protected void deleteOldConnections() {
		while(!Thread.interrupted()) {
			WrappedConnection w = oldConnections.peek();
			if(w == null) {
				try {
					Thread.sleep(timeLimit);
				} catch (InterruptedException e) {
					break;
				}
				continue;
			}
			long now = System.currentTimeMillis();
			if(w.getDeathTime() < now) {
				w = oldConnections.poll();
				if(w != null) {
					Connection con = w.getConnection();
					if(!con.isUsed() && con.getCurrentDeathTime() < now) {
						if(!con.use()) {
							continue;
						}
						removeConnection(con);
					}
				}
			} else {
				try {
					Thread.sleep(w.getDeathTime() - now);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}
	
	/**
	 * Wraps connections with deathTime
	 * 
	 * @author victoragnez
	 *
	 */
	protected static class WrappedConnection {
		protected final long deathTime;
		protected final Connection connection;
		
		public WrappedConnection(long deathTime, Connection connection) {
			this.deathTime = deathTime;
			this.connection = connection;
		}
		
		public long getDeathTime() {
			return deathTime;
		}
		
		public Connection getConnection() {
			return connection;
		}
	}
	
	/**
	 * Wraps the put method to be synchronized
	 * @author victoragnez
	 */
	protected class WrappedPut {
		public synchronized void put(String key) {
			if(cache.get(key) == null) {
				cache.put(key, new ConcurrentLinkedQueue<Connection>());
			}
		}
	}
	
	/**
	 * Trivial way to send the data, which doesn't create new threads nor caches connection
	 * 
	 * @param host the hostname to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @return the server reply
	 * @throws RemoteError 
	 */
	@Deprecated
	public ByteArrayInputStream singleSocketSend(String host, int port, ByteArrayOutputStream msg) throws RemoteError {
		
		byte[] byteMsg = msg.toByteArray();

		try {
			Socket client;
			DataOutputStream outToServer;
			DataInputStream inFromServer;
						
			client = new Socket(host, port);
			outToServer = new DataOutputStream(client.getOutputStream());
			
			outToServer.writeInt(byteMsg.length);
			outToServer.write(byteMsg);
			
			inFromServer = new DataInputStream(client.getInputStream());
			int length = inFromServer.readInt();
			byte[] byteAns = new byte[length];
			
			inFromServer.readFully(byteAns, 0, byteAns.length);
			ByteArrayInputStream ret = new ByteArrayInputStream(byteAns);
			
			client.close();
			
			return ret;
			
		} catch (IOException e1 ) {
			throw new RemoteError(e1);
		}
	}

}
