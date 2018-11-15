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

import br.ufrn.dimap.middleware.remotting.interfaces.ServerProtocolPlugin;

/**
 * Default Server Protocol Plug-in to handle TCP connections
 * 
 * @author victoragnez
 *
 */
public class DefaultServerProtocolTCP implements ServerProtocolPlugin {

	private final ThreadPoolExecutor tasksExecutor;
	private final Set<Socket> activeSockets = ConcurrentHashMap.newKeySet();
	private Thread listenThread;
	private ServerSocket server;
	
	/**
	 * Sets default maximum number of threads to 2000.
	 */
	public DefaultServerProtocolTCP() {
		this(2000);
	}
	
	/**
	 * Creates the tasks Executor
	 * @param maxTasks
	 */
	public DefaultServerProtocolTCP(int maxTasks) {
		this.tasksExecutor = new ThreadPoolExecutor(maxTasks, maxTasks, 1000, 
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ServerProtocolPlugin#init(int)
	 */
	@Override
	public void init(int port) {
		listenThread = new Thread(() -> listen(port));
		listenThread.start();
	}
	
	/**
	 * Method to listen in a specific port
	 * @param port the port to listen
	 */
	private void listen(int port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		while(!Thread.interrupted()) {
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
			while(!Thread.interrupted()) {
				int len = in.readInt();
				byte[] b = new byte[len];
				
				in.readFully(b, 0, len);
				
				out.writeInt(0);
				//out.write(c);
			}
		} catch(Exception e) {
			
		} finally {
			try {
				client.close();
			} catch(Exception e) { }
			activeSockets.remove(client);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ServerProtocolPlugin#shutdown()
	 */
	@Override
	public synchronized void shutdown() {
		tasksExecutor.shutdownNow();
		try {
			server.close();
		} catch (Exception e) { }
		for(Socket s : activeSockets) {
			try {
				s.close();
			} catch (Exception e) { }
		}
		activeSockets.clear();
	}

}
