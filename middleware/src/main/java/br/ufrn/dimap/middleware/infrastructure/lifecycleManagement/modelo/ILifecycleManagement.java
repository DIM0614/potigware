package br.ufrn.dimap.middleware.infrastructure.lifecycleManagement.modelo;



/**
 * The Interface ILifecycleManagement.
 * 
 * @author
 * @version 1.0
 * @see LifecycleManagement Interface
 */
public interface ILifecycleManagement {

	public Object getInvoker( AbsoluteObjectReference id);
	public void invocationDone( AbsoluteObjectReference id);
	
}