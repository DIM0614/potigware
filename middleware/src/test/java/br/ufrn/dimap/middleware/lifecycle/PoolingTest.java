package br.ufrn.dimap.middleware.lifecycle;

import br.ufrn.dimap.middleware.lifecycle.impl.PoolingManager;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public class PoolingTest implements Runnable{
	
	//private static PoolingManager<FakeServant> pool;

	@Override
	public void run() {
		
		for (int i=0; i<=10; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					FakeServant s0;
					try {
						//s0 = pool.getFreeInstance();
						Thread.sleep(5000);
						System.out.println(/*s0.getA()*/);
						//pool.putBackToPool(s0);
					} catch(Exception e) {
						e.printStackTrace();
					}
					
				}
			}).start();
		}
		
	}
	
	public static void main(String [] args) {
		try {
			//pool = new PoolingManager<>(FakeServant.class, 10);
			new PoolingTest().run();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
