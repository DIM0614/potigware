package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugin;

/**
 * Represents the default protocol to the Client Request Handler,
 * which handles networking synchronous communication
 * inside client applications
 * 
 * @author victoragnez
 */

public class DefaultClientProtocol implements ClientProtocolPlugin {
	
	/**
	 * ExecutorService to limit number of threads connecting to server
	 */
	private final ExecutorService tasksExecutor;
	
	/**
	 * Default constructor with maximum number of connections and threads set to 1000
	 */
	public DefaultClientProtocol() {
		this(1000);
	}
	
	/**
	 * Creates the client protocol with maximum number of threads and sockets connections
	 * @param maxConnections maximum number of sockets and connections alive
	 */
	public DefaultClientProtocol(int maxConnections) {
		tasksExecutor = Executors.newFixedThreadPool(maxConnections);
	}
	
	/**
	 * Sends the data using TCP protocol
	 */
	@Override
	public ByteArrayInputStream send(String host, int port, ByteArrayOutputStream msg) throws RemoteError {
		try {
			return tasksExecutor.submit(() -> singleSocketSend(host, port, msg) ).get();
		} catch (InterruptedException | ExecutionException e1) {
			throw new RemoteError(e1);
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
