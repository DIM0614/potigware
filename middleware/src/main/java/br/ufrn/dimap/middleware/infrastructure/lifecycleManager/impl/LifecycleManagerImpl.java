package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.impl;


import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.LifecycleManager;

/**
 * The Class LifecycleManagement.
 * 
 * @author
 * @version 1.0
 * @see Lifecycle Management
 */
public class LifecycleManagerImpl implements LifecycleManager {

	StaticRegistryImpl staticRegistry;
	PerRequestLifeCycle perRequestLifeCycle;
	/*
	 * Instantiates a new lifecycle management.
	 */
	public LifecycleManagerImpl() {
		staticRegistry = new StaticRegistryImpl();
		perRequestLifeCycle = new PerRequestLifeCycle();
	}

	public Invoker getInvoker(AbsoluteObjectReference aor) throws Exception // Change to specific Exception.
	{		
		// Verify type of object requested (Static or Per-Request)
		boolean isStatic, isPerRequest;
		
		try
		{
			ObjectId objectId = aor.getObjectId();
		
			DefaultLookup defaultLookup = getContext().getDefaultLookup(); // How get a defaultLookup ?
			
			Invoker obj = defaultLookup.findByID(objectId); // Return a .class */
			
			if( obj != null )
			{
				if( isStatic )
				{
					obj = staticRegistry.getObj(id);
				}
				else if( isPerRequest )
				{
					obj = perRequestLifeCycle.getObj();
				}
			}
			else
				throw new Exception();
			
			/* return lifeCycleStatic.getObj(class.id); */
		}
		catch(Exception e)
		{
			throw e;
		}
		
		
		return obj;
	}

	public void invocationDone( AbsoluteObjectReference aor,  Invoker obj)
	{
		boolean isStatic, isPerRequest;
		
		if( isPerRequest )
		{
			 perRequestLifeCycle.invocationDone(obj);
		}
	}
	
}
