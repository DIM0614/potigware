package br.ufrn.dimap.middleware.lifecycle.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.lifecycle.interfaces.PerRequestLifecycle;
import br.ufrn.dimap.middleware.lifecycle.interfaces.Pooling;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * The early implementation of the PerRequest manager.
 * 
 * @author Gabriel Victor
 * @author victoragnez
 * @version 0.1
 */

public class PerRequestLifecycleManager implements PerRequestLifecycle{
	
	private final Map<ObjectId, Pooling> pools = new ConcurrentHashMap<ObjectId, Pooling>();
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.lifecycle.interfaces.PerRequestLifecycle#InvocationArrived(br.ufrn.dimap.middleware.identification.ObjectId)
	 */
	@Override
	public Invoker InvocationArrived(ObjectId objectId) throws RemoteError {
		return pools.get(objectId).getFreeInstance();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.lifecycle.interfaces.PerRequestLifecycle#InvocationDone(br.ufrn.dimap.middleware.identification.ObjectId, br.ufrn.dimap.middleware.remotting.interfaces.Invoker)
	 */
	@Override
	public void InvocationDone(ObjectId objectId, Invoker pooledServant) throws RemoteError {
		pools.get(objectId).putBackToPool(pooledServant);
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.lifecycle.interfaces.PerRequestLifecycle#RegisterPerRequestInstancePool(br.ufrn.dimap.middleware.identification.ObjectId, java.lang.Class, int)
	 */
	@Override
	public void RegisterPerRequestInstancePool(ObjectId objectId, Class<? extends Invoker> type, int size) throws RemoteError {
		pools.put(objectId, new PoolingManager(type, size));
	}

}
