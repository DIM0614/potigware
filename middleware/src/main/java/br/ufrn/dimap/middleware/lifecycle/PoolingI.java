package br.ufrn.dimap.middleware.lifecycle;

/**
 * This is an interface created for pooling pattern to be used by the
 * Per-request instances.
 *
 * @author Gabriel Victor
 * @version 1.0
 */
public interface PoolingI<T extends PerRequest> {
    /**
     * This method will access a pool of objects and return a free object if it
     * is the case.
     *
     * @return to be implemented
     */
    public T getFreeInstance();

    /**
     * This method should implement the return of the used object to the pool;
     *
     * @param pooledServant used object to be returned to the pool;
     */
    public boolean putBackToPool(Servant pooledServant);

}
