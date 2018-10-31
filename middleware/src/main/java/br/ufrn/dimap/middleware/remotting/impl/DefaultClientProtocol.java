package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugin;

/**
 * Represents the default protocol to the Client Request Handler,
 * which handles networking synchronous communication
 * inside client applications
 * 
 * Allows to set limit the number of threads connecting to server and
 * the time which connections will be cached
 * 
 * @author victoragnez
 */
public class DefaultClientProtocol implements ClientProtocolPlugin {
	
	/**
	 * ExecutorService to limit number of threads connecting to server
	 */
	private final ExecutorService tasksExecutor;
	
	private final Map<String, Queue<Connection> > cache = new ConcurrentHashMap<String, Queue<Connection> >();
	
	private final Queue<Wrapper> oldConnections = new ConcurrentLinkedQueue<Wrapper>();
	
	private final long timeLimit;
	
	/**
	 * Default constructor with maximum number of threads set to 1000
	 */
	public DefaultClientProtocol() {
		this(1000);
	}
	
	/**
	 * Creates the client protocol with maximum number of threads and
	 * sets time limit of caching connections to 10s
	 * @param maxConnections maximum number of threads
	 */
	public DefaultClientProtocol(int maxConnections) {
		this(maxConnections, 10000000L);
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
		tasksExecutor = Executors.newFixedThreadPool(maxConnections + 1);
		tasksExecutor.submit(() -> deleteOldConnections());
		this.timeLimit = timeLimit; 
	}
	
	/*
	 * Delete connections that have not been used after timeLimit milliseconds
	 */
	private void deleteOldConnections() {
		while(true) {
			Wrapper w = oldConnections.peek();
			if(w == null) {
				try {
					Thread.sleep(timeLimit);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			long now = System.currentTimeMillis();
			if(w.getDeathTime() < now) {
				w = oldConnections.poll();
				if(w != null) {
					Connection con = w.getConnection();
					if(!con.isUsed() && con.getCurrentDeathTime() < now) {
						try {
							con.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				try {
					Thread.sleep(w.getDeathTime() - now);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sends the data using TCP protocol
	 */
	@Override
	public ByteArrayInputStream send(String host, int port, ByteArrayOutputStream msg) throws RemoteError {
		try {
			return tasksExecutor.submit(() -> sendAndCache(host, port, msg) ).get();
		} catch (InterruptedException | ExecutionException e1) {
			throw new RemoteError(e1);
		}
	}
	
	private ByteArrayInputStream sendAndCache(String host, int port, ByteArrayOutputStream msg) throws RemoteError {
		//TODO implement
		return null;
	}
	
	/**
	 * Stop all threads and closes sockets
	 * @throws IOException exception when trying to close some Socket
	 */
	public void shutdown() throws IOException {
		tasksExecutor.shutdownNow();
		while(!oldConnections.isEmpty()) {
			Wrapper current = oldConnections.poll();
			if(current != null)
				current.getConnection().close();
		}
	}
	
	private static class Wrapper implements Comparable<Wrapper> {
		private final long deathTime;
		private final Connection connection;
		
		public Wrapper(long deathTime, Connection connection) {
			this.deathTime = deathTime;
			this.connection = connection;
		}
		
		@Override
		public int compareTo(Wrapper o) {
			long dif = this.deathTime - o.getDeathTime(); 
			return dif > 0 ? 1 : (dif < 0 ? -1 : 0);
		}
		
		public long getDeathTime() {
			return deathTime;
		}
		
		public Connection getConnection() {
			return connection;
		}
	}
	
	/**
	 * Trivial way to send the data, which doesn't create new threads nor cache connection
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
			OutputStream outToServer;
			InputStream inFromServer;
						
			client = new Socket(host, port);
			outToServer = client.getOutputStream();
			inFromServer = client.getInputStream();
			
			outToServer.write(byteMsg);
			
			ByteArrayInputStream ret = new ByteArrayInputStream(inFromServer.readAllBytes());
			
			client.close();
			
			return ret;
			
		} catch (IOException e1 ) {
			throw new RemoteError(e1);
		}
	}
}
