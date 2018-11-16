package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces;

public interface StaticRegistry
{
	public Invoker getObj(ObjectId id);
}
