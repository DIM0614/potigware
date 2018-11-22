package br.ufrn.dimap.middleware.lifecycle;

public class PoolingTest implements Runnable{
	
	private static PoolingManager<FakeServant> pool = new PoolingManager<>(FakeServant.class, 10);

	@Override
	public void run() {
		
		for (int i=0; i<=10; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					FakeServant s0 = pool.getFreeInstance();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						Thread.interrupted();
					}
					
					System.out.println(s0.getA());
					pool.putBackToPool(s0);
				}
			}).start();
		}
		
	}
	
	public static void main(String [] args) {
		new PoolingTest().run();
	}
}
