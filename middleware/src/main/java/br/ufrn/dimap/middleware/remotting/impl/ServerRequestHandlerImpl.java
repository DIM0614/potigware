package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.remotting.interfaces.ServerProtocolPlugin;
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
	 * Communication protocol
	 */
	private final ServerProtocolPlugin protocol;
	
	/**
	 * Constructors, set port and protocol to default ones when not specified
	 */
	public ServerRequestHandlerImpl() {
		this(defaultPort, new DefaultServerProtocolTCP());
	}
	
	public ServerRequestHandlerImpl(int port) {
		this(port, new DefaultServerProtocolTCP());
	}
	
	public ServerRequestHandlerImpl(ServerProtocolPlugin protocol) {
		this(defaultPort, protocol);
	}
	
	public ServerRequestHandlerImpl(int port, ServerProtocolPlugin protocol) {
		this.port = port;
		this.protocol = protocol;
	}
	
	
	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ServerRequestHandler#init()
	 */
	@Override
	public void init() {
		protocol.listen(port);
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ServerRequestHandler#shutdown()
	 */
	@Override
	public void shutdown() throws RemoteError {
		protocol.shutdown();
	}

}
