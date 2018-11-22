package br.ufrn.dimap.middleware.infrastructure.impl;


import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.identification.lookup.Lookup;
import br.ufrn.dimap.middleware.infrastructure.interfaces.LifecycleManager;
import br.ufrn.dimap.middleware.infrastructure.interfaces.LifecycleManagerRegistry;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * The Class PerRequestLifecycleManager.
 * 
 * @author
 * @version 1.0
 * @see Lifecycle Management
 */
public class PerRequestLifecycleManager implements LifecycleManager
{
	private Invoker invoker;
	/*
	 * Instantiates a new lifecycle management.
	 */
	public PerRequestLifecycleManager() {
		invoker = null;
	}
	
	public void registerPerCallInstance(AbsoluteObjectReference aor)
	{
		ObjectId objectId = aor.getObjectId();
		LifecycleManagerRegistry lyfecycleManagerRegistry = LifecycleManagerRegistryImpl.getInstance();
		
		lyfecycleManagerRegistry.registerId(objectId, this);
	}
	
	public Invoker invocationArrived(AbsoluteObjectReference aor) throws Exception // Change to specific Exception.
	{						
		try
		{
			ObjectId objectId = aor.getObjectId();
		
			Lookup defaultLookup = DefaultLookup.getInstance();
			
			invoker = (Invoker) defaultLookup.findById(objectId); // Return a .class */
			
			// Instantiate appropriate invoker
		}
		catch(Exception e)
		{
			throw e;
		}
		
		
		return invoker;
	}

	public void invocationDone( Invoker obj )
	{
		this.invoker = null;
	}
	
}
