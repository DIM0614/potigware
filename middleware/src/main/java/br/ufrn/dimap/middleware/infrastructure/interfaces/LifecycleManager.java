package br.ufrn.dimap.middleware.infrastructure.interfaces;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * The Interface ILifecycleManagement.
 * 
 * @author
 * @version 1.0
 * @see PerRequestLifecycleManager Interface
 */
public interface LifecycleManager {

	public Invoker invocationArrived( AbsoluteObjectReference aor) throws Exception;
	public void invocationDone( Invoker obj);
	
}
