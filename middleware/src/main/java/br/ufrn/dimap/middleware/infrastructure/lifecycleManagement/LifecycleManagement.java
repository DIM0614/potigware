package br.ufrn.dimap.middleware.infrastructure.lifecycleManagement;


import br.ufrn.dimap.middleware.infrastructure.lifecycleManagement.modelo.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManagement.modelo.ILifecycleManagement;

/**
 * The Class LifecycleManagement.
 * 
 * @author
 * @version 1.0
 * @see Lifecycle Management
 */
public class LifecycleManagement implements ILifecycleManagement {

	StaticRegistry staticRegistry;
	PerRequestLifeCycle perRequestLifeCycle;
	/*
	 * Instantiates a new lifecycle management.
	 */
	public LifecycleManagement() {
		staticRegistry = new StaticRegistry();
		perRequestLifeCycle = new PerRequestLifeCycle();
	}

	public Object getInvoker(AbsoluteObjectReference aor) throws Exception // Change to specific Exception.
	{		
		// Verify type of object requested (Static or Per-Request)
		boolean isStatic, isPerRequest;
		
		try
		{
			ObjectId objectId = aor.getObjectId();
		
			DefaultLookup defaultLookup = getContext().getDefaultLookup(); // How get a defaultLookup ?
			
			Object obj = defaultLookup.findByID(objectId); // Return a .class */
			
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

	public void invocationDone(Object obj)
	{
		boolean isStatic, isPerRequest;
		
		if( isPerRequest )
		{
			 perRequestLifeCycle.invocationDone(obj);
		}
	}

}
