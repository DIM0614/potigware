package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces;



/**
 * The Interface ILifecycleManagement.
 * 
 * @author
 * @version 1.0
 * @see LifecycleManagement Interface
 */
public interface ILifecycleManagement {

	public Object getInvoker( AbsoluteObjectReference aor);
	public void invocationDone( Object obj);
	
}
