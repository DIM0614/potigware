package br.ufrn.dimap.middleware.lifecycle;

import jdk.nashorn.internal.ir.RuntimeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PoolingManager<T extends PerRequest> implements PoolingI<T> {

    private String type;
    private int size;
    private Queue<T> pool = new LinkedList<>();

    /**
     * Default constructor
     *
     * @param type the Fully Qualified Name of the class
     * @param size the size of the pool
     */
    public PoolingManager(String type, int size) {
        this.type = type;
        this.size = size;

        Class<?> aClass;
        try {
            aClass = Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Classe não existe - jumento!", e);
        }

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
                    //FIXME: Má prática
                    Thread.interrupted();
                }
            }

            return pool.poll();
        }
    }

    @Override
    public boolean putBackToPool(T pooledServant) {
        boolean success = addPoolInstance(pooledServant);

        if (success) {
            pool.notifyAll();
        }
        return success;
    }

    //TODO: putBackToPool private with synchronized access
    private boolean putBackToPoolP(T pooledServant) {
        return false;
    }
}
