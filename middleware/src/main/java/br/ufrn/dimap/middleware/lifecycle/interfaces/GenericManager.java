package br.ufrn.dimap.middleware.lifecycle.interfaces;

import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * Strategy that all lifecycle managers must implement
 * 
 * @author victoragnez
 *
 */
public interface GenericManager {
	/**
	 * Start the process of retrieving a servant from the pool of objects.
	 * 
	 * @param objectId Unique identifier of remote object.
	 */
	public Invoker InvocationArrived(ObjectId objectId) throws RemoteError;
	/**
	 * returns the servant for his deactivation and placement back in the pool.
	 * 
	 * @param pooledServant servant used by the client.
	 */
	public void InvocationDone(ObjectId objectId, Invoker pooledServant) throws RemoteError;
}
