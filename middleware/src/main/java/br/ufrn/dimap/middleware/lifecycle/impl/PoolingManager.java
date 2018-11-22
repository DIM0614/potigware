package br.ufrn.dimap.middleware.lifecycle.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import br.ufrn.dimap.middleware.lifecycle.interfaces.Pooling;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * Implements the pooling manager to provide servants to the Lifecycle Manager.
 * 
 * @author André Winston
 * @author Carlos Antônio
 * @author Gabriel Victor
 * @author Pedro Henrique
 * @author Wilson Farias
 * @author victoragnez
 * @version 1.1
 *
 * @param <T>
 */
public class PoolingManager implements Pooling{
	
    private final BlockingQueue<Invoker> pool = new LinkedBlockingQueue<>();

    /**
     * Default constructor
     *
     * @param type class of servant
     * @param size the size of the pool
     */
    public PoolingManager(Class <? extends Invoker> type, int size) throws RemoteError {
        try {
            for (int i = 0; i < size; i++) {
                Object instance = type.getDeclaredConstructor().newInstance();
                putBackToPool(type.cast(instance));
            }
        } catch (InstantiationException | IllegalAccessException | 
        		IllegalArgumentException | InvocationTargetException | 
        		NoSuchMethodException | SecurityException e) {
            throw new RemoteError(e);
        }
    }
    
    /**
     * This method gets an instance from the pool.
     * 
     * @return A servant
     * @throws RemoteError  if any error occurs
     */
    @Override
    public Invoker getFreeInstance() throws RemoteError {
    	try {
			return pool.take();
		} catch (InterruptedException e) {
			throw new RemoteError(e);
		}
    }

    /**
     * Public method used by the LifecycleManager to put a servant back into the pool
     * 
     * @param pooledServant will be the servant to be placed back to the pool.
     * @throws RemoteError  if any error occurs
     */
    
    @Override
    public void putBackToPool(Invoker pooledServant) throws RemoteError {
    	try {
    		pool.add(pooledServant);
    	} catch(IllegalStateException | ClassCastException |
    			NullPointerException | IllegalArgumentException e) {
    		throw new RemoteError(e);
    	}
    }
}
