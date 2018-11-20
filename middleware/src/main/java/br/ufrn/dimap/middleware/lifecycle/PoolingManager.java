package br.ufrn.dimap.middleware.lifecycle;

import java.util.ArrayList;
import java.util.List;

public class PoolingManager<T>{
	
	private String type;
	private int size;
	private List<Integer> pool;
	
	public PoolingManager(String type, int size) {
		this.type = type;
		this.size = size;
		pool = new ArrayList<Integer>(size);
		for (int i=0; i<size; i++) {
			pool.add(i);
		}
	}
	
	public int removeFromPool(int pooledSrvt) throws InterruptedException {
		synchronized(pool) {
			while(pool.size() <= 0) {
				//pool.wait();
			}
			return pool.remove(pooledSrvt);
		}	
	}
	
	public void putBackToPool(int pooledSrvt) {
		synchronized(pool) {
			pool.add(pooledSrvt);
			//pool.notify();
			System.out.println("Tamanho: " + pool.size());
		}
	}
	
	

}
