package br.ufrn.dimap.middleware.lifecycle.interfaces;

import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * Implements the Lifecycle manager for the per-request pattern in the middleware.
 * This service will manage invocations from the Invoker and access/create pools of objects when
 * necessary.
 * 
 * @author Gabriel Victor de Assis Azevedo 
 * @version 1.0
 */
public interface PerRequestLifecycle extends GenericManager {
	/**
	 * Start the process of retrieving a servant from the pool of objects.
	 * 
	 * @param objectId Unique identifier of remote object.
	 * @param obj is the object created on server 
	 */
	@Override
	public Invoker InvocationArrived(ObjectId objectId) throws RemoteError;
	
	/**
	 * returns the servant for his deactivation and placement back in the pool.
	 * 
	 * @param pooledServant servant used by the client.
	 */
	@Override
	public void InvocationDone(ObjectId objectId, Invoker pooledServant) throws RemoteError;
	/**
	 * Create a pool of servants that will be created to available for the client.
	 * 
	 * @param pooledServant servant used by the client.
	 */
	public void RegisterPerRequestInstancePool(ObjectId objectId, Class<? extends Invoker> type) throws RemoteError;
	
}
