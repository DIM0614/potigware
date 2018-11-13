package br.ufrn.dimap.middleware.identification.lookup;

import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.NameServer;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * The Lookup class allows clients to bind name properties 
 * to absolute object references (AORs) and retrieve AORs
 * later with the respective name properties.
 * The singleton pattern is used to implement this class.
 * 
 * @author ireneginani
 */
public class DefaultLookup implements Lookup {
	
	private NameServer nameServer;
	private String host;
	private int port;
	
	
	/**
	 * Wraps the instance
	 */
	private static Wrapper<Lookup> instanceWrapper;
	
	/**
	 * Returns single instance of the lookup class, creating instance if needed
	 * 
	 * @author victoragnez
	 */
	
	public static Lookup getInstance() {
		Wrapper<Lookup> w = instanceWrapper;
        if (w == null) { // check 1
        	synchronized (DefaultLookup.class) {
        		w = instanceWrapper;
        		if (w == null) { // check 2
        			w = new Wrapper<Lookup>(new DefaultLookup());
        			instanceWrapper = w;
        		}
        	}
        }
        
        return w.getInstance();
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
	
	protected Socket createClient() throws UnknownHostException, IOException {
		Socket client = new Socket(host, port);
		return client;
	}
	
	/**
	 * The lookup data structure used to register absolute object references along with their name properties.
	 * @throws IOException 
	 */
	
	public void bind(String name, Object remoteObject, String host, int port) throws RemoteError, IOException {
		ObjectOutputStream output = new ObjectOutputStream(createClient().getOutputStream());
		String data  = "bind " + remoteObject +" "+ host +" "+ port;
		output.writeObject(data);
		output.flush();
		output.close();
		
	}

	public AbsoluteObjectReference find(String name) throws RemoteError, UnknownHostException, IOException {
		ObjectOutputStream output = new ObjectOutputStream(createClient().getOutputStream());
		String data  = "find " + name;
		output.writeObject(data);
		output.flush();
		output.close();
		return null;
	}

	public Object findById(ObjectId ObjectId) throws RemoteError, UnknownHostException, IOException {
		ObjectOutputStream output = new ObjectOutputStream(createClient().getOutputStream());
		String data  = "findById " + ObjectId;
		output.writeObject(data);
		output.flush();
		output.close();
		return null;
	}

}