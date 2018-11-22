package br.ufrn.dimap.middleware.infrastructure.impl;

import java.io.IOException;
import java.net.UnknownHostException;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.identification.lookup.Lookup;
import br.ufrn.dimap.middleware.infrastructure.interfaces.LifecycleManager;
import br.ufrn.dimap.middleware.infrastructure.interfaces.LifecycleManagerRegistry;
import br.ufrn.dimap.middleware.lifecycle.PoolingManager;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

public class StaticLifecycleManager implements LifecycleManager {
	Invoker staticInvoker;
	
	@Override
	public Invoker invocationArrived(AbsoluteObjectReference aor){
		return staticInvoker;
	}
	
	public void registerStaticInstance(AbsoluteObjectReference aor) throws UnknownHostException, ClassNotFoundException, RemoteError, IOException
	{
		ObjectId objectId = aor.getObjectId();
		LifecycleManagerRegistry lyfecycleManagerRegistry = LifecycleManagerRegistryImpl.getInstance();
		
		lyfecycleManagerRegistry.registerId(objectId, this);

		Lookup defaultLookup = DefaultLookup.getInstance();
		
		staticInvoker = (Invoker) defaultLookup.findById(objectId); // Return a .class */
	}

	@Override
	public void invocationDone(Invoker staticObj) {
		// TODO: Write a log or something here.
	}

}
