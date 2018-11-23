package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.StaticRegistry;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

@Deprecated
public class StaticRegistryImpl implements StaticRegistry
{
	private final Map<ObjectId, Invoker> staticRegistry = new ConcurrentHashMap<ObjectId, Invoker>();
	
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
		Invoker obj = null; // TODO
		
		staticRegistry.put(id, obj);
		
		
		return obj;
	}

}
