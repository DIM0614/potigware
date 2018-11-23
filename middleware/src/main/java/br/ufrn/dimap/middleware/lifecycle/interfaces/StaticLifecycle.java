package br.ufrn.dimap.middleware.lifecycle.interfaces;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.LifecycleManager;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * Static instances lifecycle manager
 * 
 * @author victoragnez
 * @version 1.0
 */
public interface StaticLifecycle extends LifecycleManager {
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.LifecycleManager#getInvoker(br.ufrn.dimap.middleware.identification.AbsoluteObjectReference)
	 */
	@Override
	public Invoker getInvoker(AbsoluteObjectReference aor) throws RemoteError;
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.LifecycleManager#invocationDone(br.ufrn.dimap.middleware.identification.AbsoluteObjectReference, br.ufrn.dimap.middleware.remotting.interfaces.Invoker)
	 */
	@Override
	public void invocationDone(AbsoluteObjectReference aor,  Invoker obj);
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.LifecycleManager#registerInvoker(br.ufrn.dimap.middleware.identification.AbsoluteObjectReference, java.lang.Class)
	 */
	@Override
	public void registerInvoker(AbsoluteObjectReference aor, Class<? extends Invoker> type) throws RemoteError;
	
}
