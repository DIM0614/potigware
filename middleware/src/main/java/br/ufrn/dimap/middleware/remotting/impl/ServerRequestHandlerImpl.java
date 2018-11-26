package br.ufrn.dimap.middleware.remotting.impl;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.dimap.middleware.MiddlewareConfig;
import br.ufrn.dimap.middleware.extension.interfaces.ResponseHandler;
import br.ufrn.dimap.middleware.extension.interfaces.ServerProtocolPlugin;
import br.ufrn.dimap.middleware.remotting.interfaces.ServerRequestHandler;

/**
 * Class to handle connections from clients
 * 
 * @author victoragnez
 *
 */
public class ServerRequestHandlerImpl implements ServerRequestHandler {
	/**
	 * port to listen
	 */
	private final int port;
	
	/**
	 * default protocol plugin
	 */
	private final ServerProtocolPlugin defaultProtocol;
	
	/**
	 * Extra communication protocols
	 */
	private final List<ServerProtocolPlugin> protocols;
	
	/**
	 * The response handler
	 */
	private final ResponseHandler responseHandler;
	
	private List<Thread> listenThreads;
	
	/**
	 * Constructors, set port and protocol to default ones when not specified
	 * @throws RemoteError if any error occurs
	 */
	public ServerRequestHandlerImpl() throws RemoteError {
		this(defaultPort);
	}
	
	public ServerRequestHandlerImpl(int port) throws RemoteError {
		defaultProtocol = new DefaultServerProtocolTCP(); 
		
		protocols = new ArrayList<>();
		protocols.addAll(MiddlewareConfig.ProtocolPlugins.getInstance().getServerProtocolPlugins());
		
		listenThreads = new ArrayList<>();
		
		this.port = port;
		this.responseHandler = new ResponseHandlerImpl();
	}	
	
	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ServerRequestHandler#init()
	 */
	@Override
	public void init() throws RemoteError {
		Thread defaultListenThread = new Thread(() -> {
			try {
				defaultProtocol.listen(port, responseHandler);
			} catch (RemoteError e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});
		defaultListenThread.start();
		listenThreads.add(defaultListenThread);
		
		for (ServerProtocolPlugin spp : protocols) {
			Thread listenThread = new Thread(() -> {
				try {
					spp.listen(spp.getPort(), responseHandler);
				} catch (RemoteError e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			});
			listenThread.start();
			listenThreads.add(listenThread);
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ServerRequestHandler#shutdown()
	 */
	@Override
	public void shutdown() throws RemoteError {
		defaultProtocol.shutdown();
		for (ServerProtocolPlugin spp: protocols) {
			spp.shutdown();
		}
		
		try {
			for (Thread t: listenThreads) t.join();
		} catch (InterruptedException e) {
			throw new RemoteError(e);
		}
	}

}
