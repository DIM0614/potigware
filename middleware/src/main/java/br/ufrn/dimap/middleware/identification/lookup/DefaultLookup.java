package br.ufrn.dimap.middleware.identification.lookup;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
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
 * @author thiagolucena
 */
public class DefaultLookup implements Lookup {
	/**
	 * Wraps the instance
	 */
	private static Wrapper<Lookup> instanceWrapper;

	/**
	 * The lookup data structure used to register absolute object references along with their name properties.
	 */
	private Map<String, AbsoluteObjectReference> lookupMapping;
	
	/**
	 * The lookup data structure used to register object IDs along with their referred objects.
	 */
	private Map<ObjectId, Object> objectMapping;
	
	/**
	 * Private constructor
	 */
	protected DefaultLookup() {
		this.lookupMapping = new ConcurrentHashMap <String, AbsoluteObjectReference>();
		this.objectMapping = new ConcurrentHashMap <ObjectId, Object>();
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
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#bind(String, Object, String, int)
	 */
	public void bind(String name, Object remoteObject, String host, int port) throws RemoteError {
		ObjectId objectId = new ObjectId();
		AbsoluteObjectReference aor = new AbsoluteObjectReference(objectId, host, port);
		this.lookupMapping.put(name, aor);
		this.objectMapping.put(objectId, remoteObject);
	}
	
	/**
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#find(String)
	 */
	public AbsoluteObjectReference find(String name) throws RemoteError {
		if (!lookupMapping.containsKey(name)) {
			throw new RemoteError("Error on lookup find by name! No absolute object reference was registered with this name property.");
		}
		
		return lookupMapping.get(name);
	}
	
	/**
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#findById(ObjectId)
	 */	
	public Object findById(ObjectId objectId) throws RemoteError {
		if (!objectMapping.containsKey(objectId)) {
			throw new RemoteError("Error on lookup find by ID! No object was registered with this object ID.");
		}
		
		return objectMapping.get(objectId);
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