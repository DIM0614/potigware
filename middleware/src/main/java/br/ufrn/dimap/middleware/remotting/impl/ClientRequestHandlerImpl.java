package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.ufrn.dimap.middleware.extension.interfaces.ClientProtocolPlugIn;
import br.ufrn.dimap.middleware.infrastructure.qos.BasicRemotingPatterns;
import br.ufrn.dimap.middleware.infrastructure.qos.QoSObserver;
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
	private ClientProtocolPlugIn defaultProtocol;

	/**
	 *
	 * QoS observer
	 *
	 */
	private final QoSObserver qosObserver;

	/**
	 * alternatives protocol plug-ins, depending on the route.
	 */
	private final Map<String, ClientProtocolPlugIn> alternativePlugins;
	
	/**
	 * Locks when replacing alternative plug-ins
	 */
	private final Object lock = new Object();
	
	/**
	 * Private constructor which sets default values
	 */
	private ClientRequestHandlerImpl() {
		this.defaultProtocol = new DefaultClientProtocol();
		alternativePlugins = new ConcurrentHashMap<String,ClientProtocolPlugIn>();
		this.qosObserver = new QoSObserver(BasicRemotingPatterns.ClientRequestHandler);
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
		ClientProtocolPlugIn protocol = findProtocol(host, port);
		return protocol.send(host, port, msg);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#send(java.lang.String, int, java.io.ByteArrayOutputStream, br.ufrn.dimap.middleware.remotting.interfaces.Callback)
	 */
	@Override
	public void send(String host, int port, ByteArrayOutputStream msg, Callback callback, Class<?> returnType) throws RemoteError {
		ClientProtocolPlugIn protocol = findProtocol(host, port);
		protocol.send(host, port, msg, callback, returnType);
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#send(java.lang.String, int, java.io.ByteArrayOutputStream, boolean)
	 */
	@Override
	public void send(String host, int port, ByteArrayOutputStream msg, boolean waitConfirmation) throws RemoteError {
		ClientProtocolPlugIn protocol = findProtocol(host, port);
		protocol.send(host, port, msg, waitConfirmation);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#send(java.lang.String, int, java.io.ByteArrayOutputStream, br.ufrn.dimap.middleware.remotting.interfaces.PollObject)
	 */
	@Override
	public void send(String host, int port, ByteArrayOutputStream msg, PollObject pollObject, Class<?> returnType) throws RemoteError {
		ClientProtocolPlugIn protocol = findProtocol(host, port);
		protocol.send(host, port, msg, pollObject, returnType);
	}
	
	/**
	 * Finds the protocol to be used for specified host
	 * @param host
	 * @param port
	 * @return
	 * @throws RemoteError
	 */
	private ClientProtocolPlugIn findProtocol(String host, Integer port) throws RemoteError {
		final InetAddress addr;
		
		try {
			addr = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			throw new RemoteError(e);
		}
		
		String key = addr.getHostAddress();
		ClientProtocolPlugIn ret;
		
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
	public ClientProtocolPlugIn getDefaultProtocol() {
		return defaultProtocol;
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#setDefaultProtocol(br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugIn)
	 */
	@Override
	public void setDefaultProtocol(ClientProtocolPlugIn protocol) throws RemoteError {
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
	public ClientProtocolPlugIn getProtocol(String host) {
		final InetAddress addr;
		
		try {
			addr = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			return null;
		}
		
		String key = addr.getHostAddress();
		ClientProtocolPlugIn ret;
		
		ret = alternativePlugins.get(key);

		if(ret == null) {
			ret = defaultProtocol;
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#setProtocol(java.lang.String, br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugIn)
	 */
	@Override
	public void setProtocol(String host, ClientProtocolPlugIn protocol) throws RemoteError, UnknownHostException {
		final InetAddress addr = InetAddress.getByName(host);;
		String key = addr.getHostAddress();
		ClientProtocolPlugIn old;
		
		synchronized(lock) {
			old = alternativePlugins.remove(key);
			alternativePlugins.put(key, protocol);
		}
		
		if(old != null) {
			old.shutdown();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#getProtocol(java.lang.String, int)
	 */
	@Override
	public ClientProtocolPlugIn getProtocol(String host, int port) {
		final InetAddress addr;
		
		try {
			addr = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			return null;
		}
		
		String key = addr.getHostAddress();
		ClientProtocolPlugIn ret;
		
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
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#setProtocol(java.lang.String, int, br.ufrn.dimap.middleware.remotting.interfaces.ClientProtocolPlugIn)
	 */
	@Override
	public void setProtocol(String host, int port, ClientProtocolPlugIn protocol) throws RemoteError, UnknownHostException {
		final InetAddress addr = InetAddress.getByName(host);;
		String key = addr.getHostAddress() + ":" + port;
		ClientProtocolPlugIn old;
		
		synchronized(lock) {
			old = alternativePlugins.remove(key);
			alternativePlugins.put(key, protocol);
		}
		
		if(old != null) {
			old.shutdown();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler#shutdown()
	 */
	@Override
	public void shutdown() throws RemoteError {
		defaultProtocol.shutdown();
		for(ClientProtocolPlugIn plugin : alternativePlugins.values()) {
			plugin.shutdown();
		}
	}

	@Override
	public QoSObserver getQosObserver() {
		return this.qosObserver;
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
