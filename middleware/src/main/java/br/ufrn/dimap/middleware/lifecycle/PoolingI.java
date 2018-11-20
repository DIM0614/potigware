package br.ufrn.dimap.middleware.lifecycle;
/**
 * 
 * This is an interface created for pooling pattern to be used by the Per-request instances.
 * 
 * @author Gabriel Victor
 * @version 1.0
 *
 */
public interface PoolingI {
	/**
	 * This method will access a pool of objects and return a free object if it is the case.
	 * @return to be implemented
	 */
	public static Servant getFreeInstance() {
		return null;
	}
	
	/**
	 * This method should add an object to the pool.
	 * 
	 * @param pooledServant will be the servant to be placed in the pool.
	 */
	public void addPoolInstance(Servant pooledServant);
	
	/**
	 * This method will retrieve a servant from the pool to be used by the client.
	 * @param pooledServant servant to be removed and used.
	 */
	public void removeFromPool(Servant pooledServant);
	
	/**
	 * This method should implement the return of the used object to the pool;
	 * @param pooledServant used object to be returned to the pool;
	 */
	public static void putBackToPool(Servant pooledServant) {
		// TODO Auto-generated method stub
		
	}

}
