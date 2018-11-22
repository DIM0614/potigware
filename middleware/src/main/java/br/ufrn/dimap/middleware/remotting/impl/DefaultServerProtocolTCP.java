package br.ufrn.dimap.middleware.remotting.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import br.ufrn.dimap.middleware.remotting.interfaces.ResponseHandler;
import br.ufrn.dimap.middleware.remotting.interfaces.ServerProtocolPlugin;

/**
 * Default Server Protocol Plug-in to handle TCP connections. The default format of messages is:
 * 
 * First, one byte for a character which tells the type of the message
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
 *
 */
public class DefaultServerProtocolTCP implements ServerProtocolPlugin {

	private final ThreadPoolExecutor tasksExecutor;
	private final Set<Socket> activeSockets = ConcurrentHashMap.newKeySet();
	private final ResponseHandler responseHandler;
	private Thread listenThread;
	private ServerSocket server;
	
	/**
	 * Constructors set default maximum number of 
	 * threads to 2000 and uses the ResponseHandlerImpl class
	 * when not specified.
	 */
	public DefaultServerProtocolTCP() {
		this(2000, new ResponseHandlerImpl());
	}
	
	public DefaultServerProtocolTCP(int port) {
		this(port, new ResponseHandlerImpl());
	}
	
	public DefaultServerProtocolTCP(ResponseHandler responseHandler) {
		this(2000, responseHandler);
	}
	
	/**
	 * Creates the tasks Executor
	 * @param maxTasks
	 */
	public DefaultServerProtocolTCP(int maxTasks, ResponseHandler responseHandler) {
		this.tasksExecutor = new ThreadPoolExecutor(maxTasks, maxTasks, 1000, 
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		this.responseHandler = responseHandler;
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ServerProtocolPlugin#init(int)
	 */
	@Override
	public void listen(int port) {
		listenThread = new Thread(() -> doListen(port));
		listenThread.start();
	}
	
	/**
	 * Method to listen in a specific port
	 * @param port the port to listen
	 */
	private void doListen(int port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		while(!Thread.interrupted() && !server.isClosed()) {
			Socket clientSocket;
			try {
				clientSocket = server.accept();
			} catch (IOException e) {
				continue;
			}
			tasksExecutor.submit(() -> handleConnection(clientSocket));
		}
	}
	
	/**
	 * handles connection with a new client
	 * @param clientSocket
	 */
	private void handleConnection(Socket client) {
		try {
			activeSockets.add(client);
			DataInputStream in = new DataInputStream(client.getInputStream());
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			while(!Thread.interrupted() && !client.isClosed()) {
				char kind = (char)in.read();
				if(kind == 'd' || (kind != 'a' && kind != 'q')) {
					break;
				}
				int inSize = in.readInt();
				byte[] msg = new byte[inSize];
				in.readFully(msg);
				if(kind == 'a') {
					out.writeByte((byte)'c');
				}
				out.flush();
				try {
					byte[] ans = responseHandler.handleResponse(msg, kind == 'q');
					if(kind == 'q') {
						out.writeByte((byte)'r');
						out.writeInt(ans.length);
						out.write(ans);
						out.flush();
					}
				} catch(RemoteError e) {
					if(kind == 'q') {
						String errorMessage = e.getMessage();
						if(errorMessage == null) {
							errorMessage = "Remote Error";
						}
						byte[] ans = new byte[errorMessage.length()];
						for(int i = 0; i < ans.length; i++) {
							ans[i] = (byte)errorMessage.charAt(i);
						}
						out.writeByte((byte)'e');
						out.writeInt(ans.length);
						out.write(ans);
						out.flush();
					}
				}
			}
		}
		catch(IOException e) {
			
		}
		finally {
			try {
				client.close();
			} catch(IOException e) { }
			activeSockets.remove(client);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ServerProtocolPlugin#shutdown()
	 */
	@Override
	public void shutdown() {
		tasksExecutor.shutdownNow();
		try {
			server.close();
		} catch (Exception e) { }
		while(!activeSockets.isEmpty()) {
			try {
				Socket s;
				synchronized (activeSockets) {
					s = activeSockets.iterator().next();
				}
				s.close();
			} catch (Exception e) { }
		}
	}

}
