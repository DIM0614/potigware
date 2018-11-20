package br.ufrn.dimap.middleware.lifecycle;

import jdk.nashorn.internal.ir.RuntimeNode;

import java.util.ArrayList;
import java.util.List;

public class PoolingManager<T extends PerRequest> implements PoolingI<T> {

    private String type;
    private int size;
    private List<T> pool;

    public PoolingManager(String type, int size) {
        this.type = type;
        this.size = size;
        pool = new ArrayList<>(size);

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

    public boolean removeFromPool() throws InterruptedException {
//        synchronized (pool) {
//            while (pool.size() <= 0) {
//                //pool.wait();
//            }
//            return pool.remove(pooledSrvt);
//        }
        return false;
    }

    public void putBackToPool(int pooledSrvt) {
//        synchronized (pool) {
//            pool.add(pooledSrvt);
//            //pool.notify();
//            System.out.println("Tamanho: " + pool.size());
//        }
    }

    @Override
    public T getFreeInstance() {
        return null;
    }

    /**
     * This method will retrieve a servant from the pool to be used by the
     * client.
     *
     * @param pooledServant servant to be removed and used.
     */
//    private boolean removeFromPool(T pooledServant) {
//        return false;
//    }
    @Override
    public boolean putBackToPool(Servant pooledServant) {
        return false;
    }
}
