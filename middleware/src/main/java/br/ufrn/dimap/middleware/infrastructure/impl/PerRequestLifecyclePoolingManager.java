package br.ufrn.dimap.middleware.infrastructure.impl;

import java.io.IOException;
import java.net.UnknownHostException;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.identification.lookup.Lookup;
import br.ufrn.dimap.middleware.infrastructure.interfaces.LifecycleManager;
import br.ufrn.dimap.middleware.infrastructure.interfaces.LifecycleManagerRegistry;
import br.ufrn.dimap.middleware.lifecycle.PoolingManager;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

public class PerRequestLifecyclePoolingManager implements LifecycleManager {
	
	PoolingManager<Invoker> pool;
	
	public PerRequestLifecyclePoolingManager() {
		pool = null;
	}
	
	@Override
	public Invoker invocationArrived(AbsoluteObjectReference aor){
		return pool.getFreeInstance();
	}
	
	public void registerPerRequestInstancePool(AbsoluteObjectReference aor, int poolSize) throws UnknownHostException, ClassNotFoundException, RemoteError, IOException
	{
		ObjectId objectId = aor.getObjectId();
		LifecycleManagerRegistry lyfecycleManagerRegistry = LifecycleManagerRegistryImpl.getInstance();
		lyfecycleManagerRegistry.registerId(objectId, this);

		Lookup defaultLookup = DefaultLookup.getInstance();
		
		Invoker obj = (Invoker) defaultLookup.findById(objectId); // Return a .class */
		
		pool = new PoolingManager<>(obj.getClass().getName(), poolSize);
	}

	@Override
	public void invocationDone(Invoker pooledServant) {
		pool.putBackToPool(pooledServant);
	}

}
