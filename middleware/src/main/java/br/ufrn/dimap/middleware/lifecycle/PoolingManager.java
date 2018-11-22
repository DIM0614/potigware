package br.ufrn.dimap.middleware.lifecycle;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Implements the pooling manager to provide servants to the Lifecycle Manager.
 * 
 * @author André Winston
 * @author Carlos Antônio
 * @author Gabriel Victor
 * @author Pedro Henrique
 * @author Wilson Farias
 * @version 1.1
 *
 * @param <T>
 */
public class PoolingManager<T> implements Pooling<T>{
	
    private final Queue<T> pool = new LinkedBlockingQueue<>();

    /**
     * Default constructor
     *
     * @param type class of servant
     * @param size the size of the pool
     */
    public PoolingManager(Class <? extends T> type, int size) {
        Class<?> aClass = type;

        try {
            for (int i = 0; i < size; i++) {
                Object instance = aClass.newInstance();
                addPoolInstance((T) instance);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * This method should add an object to the pool.
     *
     * @param pooledServant will be the servant to be placed in the pool.
     */
    private boolean addPoolInstance(T pooledServant) {
        return pool.add(pooledServant);
    }

    /**
     * This method gets an instance from the pool.
     * 
     * @return A servant
     */
    @Override
    public T getFreeInstance() {
        return removeFromPool();
    }

    /**
     * This method will retrieve a servant from the pool to be used by the
     * client.
     *
     * @return An object from pool
     */
    private T removeFromPool() {
        synchronized (pool) {
            if (pool.isEmpty()) {
                try {
                    pool.wait();
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }

            return pool.poll();
        }
    }

    /**
     * Public method used by the LifecycleManager to put a servant back into the pool
     * 
     * @param pooledServant will be the servant to be placed back to the pool.
     */
    @Override
    public void putBackToPool(T pooledServant) {
    	new Thread(() -> {
    		putBackToPoolP(pooledServant);
    	}).start();
    }

    /**
     * Private method used to put a servant back into the pool and notify the requests waiting for a servant
     * @param pooledServant will be the servant to be placed back to the pool
     * @return true if successful, false if not
     */
    private boolean putBackToPoolP(T pooledServant) {
    	boolean success = false;
    	
    	synchronized (pool) {
    		success = addPoolInstance(pooledServant);

            if (success) {
                pool.notify();
            }
		}
        
        return success;
    }
}
