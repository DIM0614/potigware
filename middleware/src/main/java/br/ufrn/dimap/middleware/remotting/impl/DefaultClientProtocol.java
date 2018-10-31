package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
 * the time which sockets will cache connections
 * 
 * @author victoragnez
 */
public class DefaultClientProtocol implements ClientProtocolPlugin {
	
	/**
	 * ExecutorService to limit number of threads connecting to server
	 */
	private final ExecutorService tasksExecutor;
	
	private final Map<String, Queue<Connection> > cache = new ConcurrentHashMap<String, Queue<Connection> >();
	
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
		this(maxConnections, 10000000000L);
	}
	
	public DefaultClientProtocol(int maxConnections, long timeLimit) {
		tasksExecutor = Executors.newFixedThreadPool(maxConnections);
		this.timeLimit = timeLimit; 
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
		return null;
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
			
		} catch (Exception e1 ) {
			throw new RemoteError(e1);
		}
	}
}
