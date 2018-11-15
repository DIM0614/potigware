package br.ufrn.dimap.middleware.infrastructure.lifecycleManagement;


import br.ufrn.dimap.middleware.infrastructure.lifecycleManagement.modelo.ILifecycleManagement;

/**
 * The Class LifecycleManagement.
 * 
 * @author
 * @version 1.0
 * @see Lifecycle Management
 */
public class LifecycleManagement implements ILifecycleManagement {

	/*
	 * Instantiates a new lifecycle management.
	 */
	public LifecycleManagement() {
		
	}

	public Object getInvoker(Object id) {
		
		/* defaultLookup.findByID(ObjectId objectId); // Return a .class */
		/* return lifeCycleStatic.getObj(class.id); */
		
		return null;
	}

	
	public void invocationDone(int id) {
		/* lifeCycleStatic.getObj(int id); // Return invoker */
	}

}
