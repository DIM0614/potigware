package br.ufrn.dimap.middleware.lifecycle;

public interface PoolingI {
	public static Servant getFreeInstance() {
		return null;
	}
	
	public void addPoolInstance(Servant pooledServant);
	
	public void removeFromPool(Servant pooledServant);
	
	public static void putBackToPool(Servant pooledServant) {
		// TODO Auto-generated method stub
		
	}

}
