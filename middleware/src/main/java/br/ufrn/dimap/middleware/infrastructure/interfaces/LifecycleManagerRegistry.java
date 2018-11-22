package br.ufrn.dimap.middleware.infrastructure.interfaces;

import br.ufrn.dimap.middleware.identification.ObjectId;

public interface LifecycleManagerRegistry
{
	public LifecycleManager getLifecycleManager(ObjectId id);
	public boolean registerId(ObjectId id, LifecycleManager lifecycleManager);
	public boolean removeId(ObjectId id);
	public void clearIds();
}
