/**
 * 
 */
package br.ufrn.dimap.middleware.lifecycle.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.lifecycle.interfaces.StaticLifecycle;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * Managers static instances
 * 
 * @author victoragnez
 *
 */
public class StaticLifecycleManager implements StaticLifecycle {

	private final Map<ObjectId, Class<? extends Invoker> > classes = new ConcurrentHashMap<ObjectId, Class<? extends Invoker> >();
	private final Map<ObjectId, Wrapper<Invoker> > instances = new ConcurrentHashMap<ObjectId, Wrapper<Invoker> >();
	
	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.lifecycle.interfaces.StaticLifecycle#getInvoker(br.ufrn.dimap.middleware.identification.AbsoluteObjectReference)
	 */
	@Override
	public Invoker getInvoker(AbsoluteObjectReference aor) throws RemoteError {
		Wrapper<Invoker> w = instances.get(aor.getObjectId());
        if (w == null) { // check 1
        	synchronized (instances) {
        		w = instances.get(aor.getObjectId());
        		if (w == null) { // check 2
        			Class<? extends Invoker> cl = classes.get(aor.getObjectId());
        			if(cl == null) {
        				throw new RemoteError("Tried to get instance from unregistred class");
        			}
        			Invoker invoker;
					try {
						invoker = cl.getDeclaredConstructor().newInstance();
					} catch (InstantiationException | IllegalAccessException | 
							IllegalArgumentException | InvocationTargetException |
							NoSuchMethodException | SecurityException e) {
						throw new RemoteError(e);
					}
        			w = new Wrapper<Invoker>(invoker);
        			instances.put(aor.getObjectId(), w);
        		}
        	}
        }
        
        return w.getInstance();
	}

	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.lifecycle.interfaces.StaticLifecycle#invocationDone(br.ufrn.dimap.middleware.identification.AbsoluteObjectReference, br.ufrn.dimap.middleware.remotting.interfaces.Invoker)
	 */
	@Override
	public void invocationDone(AbsoluteObjectReference aor, Invoker obj) {
		//Empty
	}

	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.lifecycle.interfaces.StaticLifecycle#registerInvoker(br.ufrn.dimap.middleware.identification.AbsoluteObjectReference, java.lang.Class)
	 */
	@Override
	public void registerInvoker(AbsoluteObjectReference aor, Class<? extends Invoker> type) throws RemoteError {
		classes.put(aor.getObjectId(), type);
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
