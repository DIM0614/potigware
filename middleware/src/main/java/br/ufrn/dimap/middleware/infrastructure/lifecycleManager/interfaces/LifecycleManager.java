package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces;



/**
 * The Interface ILifecycleManagement.
 * 
 * @author
 * @version 1.0
 * @see LifecycleManagerImpl Interface
 */
public interface LifecycleManager {

	public Invoker getInvoker( AbsoluteObjectReference aor) throws Exception;
	public void invocationDone( AbsoluteObjectReference aor,  Invoker obj);
	
}
