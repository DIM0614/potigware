package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.impl;

import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.StaticRegistry;

public class StaticRegistryImpl implements StaticRegistry
{
	HashMap<ObjectId, Invoker> staticRegistry;
	
	public Invoker getObj(ObjectId id)
	{
		Invoker obj = staticRegistry.get(id);
		
		if( obj == null )
		{
			obj = createObj(id);
		}
		
		return obj;
	}
	
	private Invoker createObj(ObjectId id)
	{
		// Verify type of object for instantiate.
		Invoker obj = new ObjectType();
		
		staticRegistry.put(id, obj);
		
		
		return obj;
	}

}
