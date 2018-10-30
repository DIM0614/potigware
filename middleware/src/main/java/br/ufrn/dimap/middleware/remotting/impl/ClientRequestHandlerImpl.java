package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugin;
import br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler;

/**
 * The Client Request Handle is responsible for sending data to the server.
 * Follows Singleton pattern.
 * 
 * @author victoragnez
 */
public final class ClientRequestHandlerImpl implements ClientRequestHandler {
	
	/**
	 * port to be used
	 */
	private int port;
	
	/**
	 * protocol of communication
	 */
	private ClientProtocolPlugin protocol;
	
	/**
	 * Private constructor which sets default values
	 */
	private ClientRequestHandlerImpl() {
		setPort(defaultPort);
		setProtocol(new DefaultClientProtocol());
	}
	
	/**
	 * Wraps the instance
	 */
	private static Wrapper<ClientRequestHandler> wrapper;
	
	/**
	 * Creates a single instance, guarantees safe publication
	 * @return
	 */
	public static ClientRequestHandler getInstance () {
		Wrapper<ClientRequestHandler> w = wrapper;
        if (w == null) { // check 1
        	synchronized (ClientRequestHandlerImpl.class) {
        		w = wrapper;
        		if (w == null) { // check 2
        			w = new Wrapper<ClientRequestHandler>(new ClientRequestHandlerImpl());
        			wrapper = w;
        		}
        	}
        }
        
        return w.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#send(java.lang.String, int, java.io.ByteArrayOutputStream)
	 */
	@Override
	public ByteArrayInputStream send(String host, int port, ByteArrayOutputStream msg) throws RemoteError {
		return getProtocol().send(host, port, msg);
	}

	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#getProtocol()
	 */
	@Override
	public ClientProtocolPlugin getProtocol() {
		return protocol;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#setProtocol(br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugin)
	 */
	@Override
	public void setProtocol(ClientProtocolPlugin protocol) {
		this.protocol = protocol;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#getPort()
	 */
	@Override
	public int getPort() {
		return port;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#setPort(int)
	 */
	@Override
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * 
	 * Wraps the instance to allow final modifier
	 * 
	 * @author victoragnez
	 * 
	 * @param <T> the type to be wrapped
	 */
	private static class Wrapper<T> {
		private final T instance;
	    public Wrapper(T service) {
	        this.instance = service;
	    }
	    public T getInstance() {
	        return instance;
	    }
	}

}
