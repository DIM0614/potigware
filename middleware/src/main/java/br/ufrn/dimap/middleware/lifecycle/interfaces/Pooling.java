package br.ufrn.dimap.middleware.lifecycle.interfaces;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * This is an interface created for pooling pattern to be used by the
 * Per-request instances.
 *
 * @author Gabriel Victor
 * @version 1.0
 */
public interface Pooling {
    /**
     * This method will access a pool of objects and return a free object if it
     * is the case.
     *
     * @return an instance of the servant
     */
    public Invoker getFreeInstance() throws RemoteError;

    /**
     * This method should implement the return of the used object to the pool;
     *
     * @param pooledServant used object to be returned to the pool;
     */
    public void putBackToPool(Invoker pooledServant) throws RemoteError;

}
