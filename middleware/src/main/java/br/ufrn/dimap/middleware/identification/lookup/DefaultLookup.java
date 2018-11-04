package br.ufrn.dimap.middleware.identification.lookup;

import java.util.HashMap;
import java.util.Map;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * The Lookup class allows clients to bind name properties 
 * to absolute object references (AORs) and retrieve AORs
 * later with the respective name properties.
 * The singleton pattern is used to implement this class.
 * 
 * @author thiagolucena
 */
public class DefaultLookup implements Lookup {
	
	private String name;
	private AbsoluteObjectReference aor;
	
	/**
	 * Wraps the instance
	 */
	private static Wrapper<Lookup> instanceWrapper;

	/**
	 * The lookup data structure used to register absolute object references along with their name properties.
	 */
	private Map<String, AbsoluteObjectReference> lookupMapping;
	
	/**
	 * Private constructor
	 */
	protected DefaultLookup() {
		this.lookupMapping = new HashMap<String, AbsoluteObjectReference>();
	}

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
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#bind(String, AbsoluteObjectReference)
	 */
	synchronized public void bind(String name, AbsoluteObjectReference aor) throws RemoteError {
		if (lookupMapping.containsKey(name)) {
			throw new RemoteError("Error on lookup binding! There already exists an absolute object reference for this name property.");
		}
		this.name = name;
		this.aor = aor;
		lookupMapping.put(name, aor);
	}
	
	/**
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#find(String)
	 */
	synchronized public AbsoluteObjectReference find(String name) throws RemoteError {
		if (!lookupMapping.containsKey(name)) {
			throw new RemoteError("Error on lookup finding! No absolute object reference was registered with this name property.");
		}
		this.name = name;
		this.aor = aor;
		return lookupMapping.get(name);
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
