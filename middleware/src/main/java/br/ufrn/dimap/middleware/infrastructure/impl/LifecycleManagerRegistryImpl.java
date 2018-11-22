package br.ufrn.dimap.middleware.infrastructure.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.infrastructure.interfaces.LifecycleManager;
import br.ufrn.dimap.middleware.infrastructure.interfaces.LifecycleManagerRegistry;

public class LifecycleManagerRegistryImpl implements LifecycleManagerRegistry{

	private static ConcurrentMap<ObjectId, LifecycleManager> map;
	
	/**
	 * Wraps the instance
	 */
	private static Wrapper<LifecycleManagerRegistry> instanceWrapper;

	/**
	 * Returns single instance of the lifecycle manager registry class, creating instance if needed
	 * 
	 * @author victoragnez
	 */
  
	public static LifecycleManagerRegistry getInstance() {
		Wrapper<LifecycleManagerRegistry> w = instanceWrapper;
        if (w == null) { // check 1
        	synchronized (LifecycleManagerRegistryImpl.class) {
        		w = instanceWrapper;
        		if (w == null) { // check 2
        			w = new Wrapper<LifecycleManagerRegistry>(new LifecycleManagerRegistryImpl());
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
	
	public LifecycleManagerRegistryImpl(){
		LifecycleManagerRegistryImpl.map = new ConcurrentHashMap<ObjectId, LifecycleManager>();
	}
	
	public LifecycleManager getLifecycleManager(ObjectId id) {
		return LifecycleManagerRegistryImpl.map.get(id);
	}

	public boolean registerId(ObjectId id, LifecycleManager lifecycleManager) {
		return ( LifecycleManagerRegistryImpl.map.putIfAbsent(id, lifecycleManager) != null ? true : false);
	}
	
	public boolean removeId(ObjectId id) {
		return ( LifecycleManagerRegistryImpl.map.remove(id) != null ? true : false);
	}
	
	public void clearIds(){
		LifecycleManagerRegistryImpl.map.clear();
	}

}
