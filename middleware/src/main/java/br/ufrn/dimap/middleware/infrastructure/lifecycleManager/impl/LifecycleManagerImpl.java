package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.impl;


import java.io.IOException;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.identification.lookup.Lookup;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.LifecycleManager;
import br.ufrn.dimap.middleware.lifecycle.impl.PerRequestLifecycleManager;
import br.ufrn.dimap.middleware.lifecycle.impl.StaticLifecycleManager;
import br.ufrn.dimap.middleware.lifecycle.interfaces.PerRequest;
import br.ufrn.dimap.middleware.lifecycle.interfaces.PerRequestLifecycle;
import br.ufrn.dimap.middleware.lifecycle.interfaces.Static;
import br.ufrn.dimap.middleware.lifecycle.interfaces.StaticLifecycle;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * The Class LifecycleManagement.
 * 
 * @author
 * @author victoragnez
 * 
 * @version 1.0
 * @see Lifecycle Management
 */
public class LifecycleManagerImpl implements LifecycleManager {

	private final StaticLifecycle staticLifecycle;
	private final PerRequestLifecycle perRequestLifeCycle;
	private final Lookup defaultLookup;

	/*
	 * Instantiates a new lifecycle management.
	 */
	public LifecycleManagerImpl() throws RemoteError {
		staticLifecycle = new StaticLifecycleManager();
		perRequestLifeCycle = new PerRequestLifecycleManager();
		defaultLookup = DefaultLookup.getInstance();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Invoker getInvoker(AbsoluteObjectReference aor) throws RemoteError {		
		Class<? extends Invoker> obj;
		
		ObjectId objectId = aor.getObjectId();
	
		
		try {
			obj = (Class<? extends Invoker>) defaultLookup.findById(objectId);
		} catch (ClassNotFoundException | IOException e) {
			throw new RemoteError(e);
		}
		
		Invoker ret = null;

		
		LifecycleManager manager = getManager(obj);
		
		ret = manager.getInvoker(aor);
		
		return ret;
	}

	@Override
	public void invocationDone( AbsoluteObjectReference aor,  Invoker obj) {
		try {
			getManager(obj.getClass()).invocationDone(aor, obj);
		} catch (RemoteError e) {
			e.printStackTrace();
		}
	}

	@Override
	public void registerInvoker(AbsoluteObjectReference aor, Class<? extends Invoker> type) throws RemoteError {
		getManager(type).registerInvoker(aor, type);
	}
	
	/**
	 * returns the lifecycle manager for the given object
	 * @param obj the object
	 * @return the manager
	 */
	private LifecycleManager getManager(Class<? extends Invoker> obj) {
		boolean isPerRequest = obj.getClass().isAnnotationPresent(PerRequest.class);
		
		boolean isStatic = obj.getClass().isAnnotationPresent(Static.class) || !isPerRequest; //TODO
		
		if(isStatic) {
			return staticLifecycle;
		}
		else {
			return perRequestLifeCycle;
		}
	}
	
}
