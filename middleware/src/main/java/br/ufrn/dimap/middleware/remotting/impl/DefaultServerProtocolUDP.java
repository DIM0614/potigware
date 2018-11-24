package br.ufrn.dimap.middleware.remotting.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.ufrn.dimap.middleware.extension.interfaces.ResponseHandler;
import br.ufrn.dimap.middleware.extension.interfaces.ServerProtocolPlugin;
import br.ufrn.dimap.middleware.remotting.exec.MiddlewareMain;

/**
 * Default Server Protocol Plug-in to handle UDP requests.
 * Receives a Invocation Data, invokes and don't returns the result,
 * Created to allow the FireAndForget pattern
 * 
 * @author victoragnez
 *
 */
public class DefaultServerProtocolUDP implements ServerProtocolPlugin {

	private final ThreadPoolExecutor tasksExecutor;
	private final Logger logger = Logger.getLogger(MiddlewareMain.class.getName());
	private DatagramSocket server;
	
	/**
	 * Constructors set default maximum number of 
	 * threads to 2000
	 */
	public DefaultServerProtocolUDP() {
		this(2000);
	}
	
	/**
	 * Creates the tasks Executor
	 * @param maxTasks
	 */
	public DefaultServerProtocolUDP(int maxTasks) {
		this.tasksExecutor = new ThreadPoolExecutor(maxTasks, maxTasks, 1000, 
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ServerProtocolPlugin#init(int)
	 */
	@Override
	public void listen(int port, ResponseHandler responseHandler) {

		try {
			server = new DatagramSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		logger.log(Level.INFO, "UDP Server ready...");
		
		while(!Thread.interrupted() && !server.isClosed()) {
			try {
				byte[] buf = new byte[4096];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				server.receive(packet);
				
				logger.log(Level.INFO, "New packet received...");
				
				tasksExecutor.submit(() -> {
					try {
						responseHandler.handleResponse(packet.getData());
						
						logger.log(Level.INFO, "Successful request...");
						
					} catch(RemoteError e) {
						logger.log(Level.INFO, "Request resulted in exception...");
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	}

}
