package br.ufrn.dimap.middleware.lifecycle;

import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * The early implementation of the PerRequest manager.
 * 
 * @author Gabriel Victor
 * @version 0.1
 */

public class PerRequestLifecycleManager implements PerRequestLifecycle{
	
	@Override
	public Servant InvocationArrived(ObjectId objectId, Invoker obj) {
		return null;
		
		// TODO
		//Servant pooledServant = Pooling.getFreeInstance();
		//pooledServant.activate();
		//return pooledServant;
	}

	@Override
	public void InvocationDone(Servant pooledServant) {
		//pooledServant.deactivate();
		//Pooling.putBackToPool(pooledServant);
		
	}

	@Override
	public void RegisterPerRequestInstancePool(PerRequest perCallInstance, int poolObjects) {
		// TODO Auto-generated method stub
		
	}

}
