package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * The Interface ILifecycleManagement.
 * 
 * @author
 * @version 1.0
 * @see LifecycleManagerImpl Interface
 */
public interface LifecycleManager {

	public Invoker getInvoker( AbsoluteObjectReference aor) throws RemoteError;
	public void invocationDone( AbsoluteObjectReference aor,  Invoker obj) throws RemoteError;
	public void registerInvoker(AbsoluteObjectReference aor, Class<? extends Invoker> type) throws RemoteError;
}
