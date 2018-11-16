package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces;

import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

public interface StaticRegistry
{
	public Invoker getObj(ObjectId id);
}
