package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.ufrn.dimap.middleware.remotting.interfaces.*;

/**
 * The Client Request Handle is responsible for sending data to the server.
 * Follows Singleton pattern.
 * 
 * @author victoragnez
 */
public final class ClientRequestHandlerImpl implements ClientRequestHandler {
	
	/**
	 * Default protocol of communication
	 */
	private ClientProtocolPlugin defaultProtocol;
	
	/**
	 * alternatives protocol plug-ins, depending on the route.
	 */
	private final Map<String, ClientProtocolPlugin> alternativePlugins;
	
	/**
	 * Private constructor which sets default values
	 */
	private ClientRequestHandlerImpl() {
		this.defaultProtocol = new DefaultClientProtocol();
		alternativePlugins = new ConcurrentHashMap<String,ClientProtocolPlugin>();
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
		ClientProtocolPlugin protocol = findProtocol(host, port);
		return protocol.send(host, port, msg);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#send(java.lang.String, int, java.io.ByteArrayOutputStream, br.ufrn.dimap.middleware.remotting.interfaces.Callback)
	 */
	@Override
	public void send(String host, int port, ByteArrayOutputStream msg, Callback callback) throws RemoteError {
		ClientProtocolPlugin protocol = findProtocol(host, port);
		protocol.send(host, port, msg, callback);
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#send(java.lang.String, int, java.io.ByteArrayOutputStream, br.ufrn.dimap.middleware.remotting.interfaces.InvocationAsynchronyPattern)
	 */
	@Override
	public void send(String host, int port, ByteArrayOutputStream msg, InvocationAsynchronyPattern pattern) throws RemoteError {
		ClientProtocolPlugin protocol = findProtocol(host, port);
		protocol.send(host, port, msg, pattern);
	}
	
	/**
	 * Finds the protocol to be used for specified host
	 * @param host
	 * @param port
	 * @return
	 * @throws RemoteError
	 */
	private ClientProtocolPlugin findProtocol(String host, Integer port) throws RemoteError {
		InetAddress addr;
		
		try {
			addr = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			throw new RemoteError(e);
		}
		
		String key = addr.getHostAddress();
		ClientProtocolPlugin ret;
		
		ret = alternativePlugins.get(key + ":" + port);
		
		if(ret == null) {
			ret = alternativePlugins.get(key);
		}
		
		if(ret == null) {
			ret = defaultProtocol;
		}
		
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#getDefaultProtocol()
	 */
	@Override
	public ClientProtocolPlugin getDefaultProtocol() {
		return defaultProtocol;
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#setDefaultProtocol(br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugin)
	 */
	@Override
	public void setDefaultProtocol(ClientProtocolPlugin protocol) throws RemoteError {
		if(this.defaultProtocol != null) {
			this.defaultProtocol.shutdown();
		}
		this.defaultProtocol = protocol;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#getProtocol(java.lang.String)
	 */
	@Override
	public ClientProtocolPlugin getProtocol(String host) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#setProtocol(java.lang.String, br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugin)
	 */
	@Override
	public void setProtocol(String host, ClientProtocolPlugin protocol) throws RemoteError, UnknownHostException {

	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#getProtocol(java.lang.String, int)
	 */
	@Override
	public ClientProtocolPlugin getProtocol(String host, int port) {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#setProtocol(java.lang.String, int, br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugin)
	 */
	@Override
	public void setProtocol(String host, int port, ClientProtocolPlugin protocol) throws RemoteError, UnknownHostException {

	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#shutdown()
	 */
	@Override
	public void shutdown() {

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
