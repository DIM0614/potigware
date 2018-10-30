package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugin;

/**
 * The Client Request Handle is responsible for sending data to the server.
 * Follows Singleton pattern.
 * 
 * @author victoragnez
 */
public final class ClientRequestHandler {
	
	/**
	 * default port 22334
	 */
	public static final int defaultPort = 22334;
	
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
	private ClientRequestHandler() {
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
        	synchronized (ClientRequestHandler.class) {
        		w = wrapper;
        		if (w == null) { // check 2
        			w = new Wrapper<ClientRequestHandler>(new ClientRequestHandler());
        			wrapper = w;
        		}
        	}
        }
        
        return w.getInstance();
	}
	
	/**
	 * Function used by the requestor to send the data,
	 * using the specific protocol
	 *  
	 * @param host the hostname to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @return the server reply
	 */
	public ByteArrayInputStream send(String host, int port, ByteArrayOutputStream msg) throws RemoteError {
		return protocol.send(host, port, msg);
	}

	/**
	 * @return the protocol
	 */
	public ClientProtocolPlugin getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(ClientProtocolPlugin protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
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
