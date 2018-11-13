package br.ufrn.dimap.middleware.identification.lookup;

import java.util.concurrent.ConcurrentHashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
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

 * The DefaultLookup class allows clients to bind name properties 
 * to implicitly constructed absolute object references (AORs), 
 * made up of a object instances along with host and port information 
 * provided by the user.
 *  
 * Users might use this class later to retrieve AORs
 * by their respective name properties, and get object instances 
 * according to their respective object IDs as well.
 * The singleton pattern is used to implement this class.
 * 
 * @author ireneginani thiagolucena
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
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#bind(String, Object, String, int)
	 */
	public void bind(String name, Object remoteObject, String host, int port) throws RemoteError, IOException {
		ObjectOutputStream output = new ObjectOutputStream(createClient().getOutputStream());
		String data  = "bind " + remoteObject +" "+ host +" "+ port;
		output.writeObject(data);
		output.flush();
		output.close();
		
	}
  
  /**
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#find(String)
	 */
	public AbsoluteObjectReference find(String name) throws RemoteError, UnknownHostException, IOException, ClassNotFoundException {
		Socket client = createClient();
		ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
		ObjectInputStream input = new ObjectInputStream(client.getInputStream());
		String data  = "find " + name;
		output.writeObject(data);
		output.flush();
		output.close();
		return (AbsoluteObjectReference) input.readObject();
	}
  
  /**
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#findById(ObjectId)
	 */	
	public Object findById(ObjectId ObjectId) throws RemoteError, UnknownHostException, IOException, ClassNotFoundException {
		Socket client = createClient();
		ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
		ObjectInputStream input = new ObjectInputStream(client.getInputStream());
		String data  = "findById " + ObjectId;
		output.writeObject(data);
		output.flush();
		output.close();
		return input.readObject();
	}

}