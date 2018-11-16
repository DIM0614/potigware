package br.ufrn.dimap.middleware.infrastructure.lifecycleManagement;

import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.IStaticRegistry;

public class StaticRegistry implements IStaticRegistry
{
	HashMap<ObjectId, Invoker> staticRegistry;
	
	public Object getObj(ObjectId id)
	{
		Object obj = staticRegistry.get(id);
		
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
	}

}
