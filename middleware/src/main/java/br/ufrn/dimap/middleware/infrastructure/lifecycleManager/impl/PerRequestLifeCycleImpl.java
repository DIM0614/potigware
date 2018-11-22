package br.ufrn.dimap.middleware.infrastructure.lifecycleManager.impl;

import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.PerRequestLifeCycle;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

@Deprecated
public class PerRequestLifeCycleImpl implements PerRequestLifeCycle {
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.PerRequestLifeCycle#invocationDone(br.ufrn.dimap.middleware.identification.ObjectId, br.ufrn.dimap.middleware.remotting.interfaces.Invoker)
	 */
	@Override
	public void invocationDone(ObjectId objectId, Invoker obj) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.PerRequestLifeCycle#getObj(br.ufrn.dimap.middleware.identification.ObjectId)
	 */
	@Override
	public Invoker getObj(ObjectId objectId) {
		// TODO Auto-generated method stub
		return null;
	}

}
