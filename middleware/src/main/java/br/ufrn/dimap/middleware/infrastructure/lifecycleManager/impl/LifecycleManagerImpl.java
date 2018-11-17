package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.impl;


import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.identification.lookup.Lookup;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.LifecycleManager;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.PerRequestLifeCycle;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.StaticRegistry;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * The Class LifecycleManagement.
 * 
 * @author
 * @version 1.0
 * @see Lifecycle Management
 */
public class LifecycleManagerImpl implements LifecycleManager {

	private final StaticRegistry staticRegistry;
	private final PerRequestLifeCycle perRequestLifeCycle;
	/*
	 * Instantiates a new lifecycle management.
	 */
	public LifecycleManagerImpl() {
		staticRegistry = new StaticRegistryImpl();
		perRequestLifeCycle = new PerRequestLifeCycleImpl();
	}

	public Invoker getInvoker(AbsoluteObjectReference aor) throws Exception // Change to specific Exception.
	{		
		// Verify type of object requested (Static or Per-Request)
		Boolean isStatic = null, isPerRequest = null; //TODO
		
		Invoker obj = null;
		
		try
		{
			ObjectId objectId = aor.getObjectId();
		
			Lookup defaultLookup = DefaultLookup.getInstance(); // How get a defaultLookup ?
			
			obj = (Invoker) defaultLookup.findById(objectId); // Return a .class */
			
			if( obj != null )
			{
				if( isStatic )
				{
					obj = staticRegistry.getObj(aor.getObjectId());
				}
				else if( isPerRequest )
				{
					obj = perRequestLifeCycle.getObj(aor.getObjectId());
				}
			}
			else
				throw new Exception(); //TODO fix it
			
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
		Boolean isStatic = null, isPerRequest = null;
		
		if( isPerRequest )
		{
			 perRequestLifeCycle.invocationDone(aor.getObjectId(), obj);
		}
	}
	
}
