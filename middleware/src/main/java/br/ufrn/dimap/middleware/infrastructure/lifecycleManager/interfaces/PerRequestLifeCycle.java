package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces;

import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

@Deprecated
public interface PerRequestLifeCycle {

	void invocationDone(ObjectId objectId, Invoker obj);

	Invoker getObj(ObjectId objectId);

}
