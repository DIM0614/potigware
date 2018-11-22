package br.ufrn.dimap.middleware.lifecycle.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.lifecycle.interfaces.Activate;
import br.ufrn.dimap.middleware.lifecycle.interfaces.Deactivate;
import br.ufrn.dimap.middleware.lifecycle.interfaces.PerRequest;
import br.ufrn.dimap.middleware.lifecycle.interfaces.PerRequestLifecycle;
import br.ufrn.dimap.middleware.lifecycle.interfaces.Pooling;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * Implementation of the PerRequest manager.
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
		
		Invoker ret = pools.get(objectId).getFreeInstance();
		activate(ret);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.lifecycle.interfaces.PerRequestLifecycle#InvocationDone(br.ufrn.dimap.middleware.identification.ObjectId, br.ufrn.dimap.middleware.remotting.interfaces.Invoker)
	 */
	@Override
	public void InvocationDone(ObjectId objectId, Invoker pooledServant) throws RemoteError {
		deactivate(pooledServant);
		pools.get(objectId).putBackToPool(pooledServant);
	}
	
	/**
	 * Executes methods with Activate annotation
	 * @param obj the object to be activated
	 */
	private void activate(Invoker obj) {
		executeMethodsWithAnnotation(obj, Activate.class);
	}

	/**
	 * Executes methods with Deactivate annotation
	 * @param obj the object to be deactivated
	 */
	private void deactivate(Invoker obj) {
		executeMethodsWithAnnotation(obj, Deactivate.class);
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.lifecycle.interfaces.PerRequestLifecycle#RegisterPerRequestInstancePool(br.ufrn.dimap.middleware.identification.ObjectId, java.lang.Class, int)
	 */
	@Override
	public void RegisterPerRequestInstancePool(ObjectId objectId, Class<? extends Invoker> type) throws RemoteError {
		PerRequest perRequest = type.getAnnotation(PerRequest.class);
		pools.put(objectId, new PoolingManager(type, perRequest.poolSize()));
	}
	
	/**
	 * Executes all methods with specified annotation
	 * @param obj the object to invoke the methods
	 * @param annotation the annotation to verify
	 */
	private void executeMethodsWithAnnotation(Invoker obj, Class<? extends Annotation> annotation) {
		Class<?> curClass = obj.getClass();
		
		while (curClass != Invoker.class) {
			List<Method> allMethods = new ArrayList<Method>(Arrays.asList(curClass.getDeclaredMethods()));       
			for (Method method : allMethods) {
				if (method.isAnnotationPresent(annotation)) {
					try {
						method.invoke(obj);
					} catch (IllegalAccessException | IllegalArgumentException | 
							InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			curClass = curClass.getSuperclass();
		}
	}

}
