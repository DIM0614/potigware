package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.impl;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
 * @see
 */
public class LifecycleManagerImpl implements LifecycleManager {

	private final StaticLifecycle staticLifecycle;
	private final PerRequestLifecycle perRequestLifeCycle;
	private final Lookup defaultLookup;

	private Map<AbsoluteObjectReference, Class<? extends Invoker>> loadedInvokerClasses;

	/*
	 * Instantiates a new lifecycle management.
	 */
	public LifecycleManagerImpl() throws RemoteError {
		staticLifecycle = new StaticLifecycleManager();
		perRequestLifeCycle = new PerRequestLifecycleManager();
		defaultLookup = DefaultLookup.getInstance();
		loadedInvokerClasses = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Invoker getInvoker(AbsoluteObjectReference aor) throws RemoteError, IOException, ClassNotFoundException {

		Class<? extends Invoker> implInvokerClass = null;

		if (loadedInvokerClasses.containsKey(aor)) {
			implInvokerClass = loadedInvokerClasses.get(aor);

		} else {
			implInvokerClass = defaultLookup.findAndLocallyInstall(aor.getObjectId());
			loadedInvokerClasses.put(aor, implInvokerClass);
			registerInvoker(aor, implInvokerClass);
		}

		if (implInvokerClass != null) {
		    LifecycleManager lf = getManager(implInvokerClass);
		    return lf.getInvoker(aor);
		} else
		    throw new RemoteError();
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
