package br.ufrn.dimap.middleware.lifecycle;

import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

public class PerRequestLifeCycleManager implements PerRequestLifeCycleManagerI{

	@Override
	public Servant InvocationArrived(ObjectId objectId, Invoker obj) {
		Servant pooledServant = PoolingI.getFreeInstance();
		pooledServant.activate();
		
		return pooledServant;
	}

	@Override
	public void InvocationDone(Servant pooledServant) {
		pooledServant.deactivate();
		PoolingI.putBackToPool(pooledServant);
		
	}

	@Override
	public void RegisterPerRequestInstancePool(PerRequest perCallInstance, int poolObjects) {
		// TODO Auto-generated method stub
		
	}

}
