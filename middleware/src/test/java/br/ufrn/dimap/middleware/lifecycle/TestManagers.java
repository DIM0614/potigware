/**
 * 
 */
package br.ufrn.dimap.middleware.lifecycle;

import java.io.IOException;
import java.util.ArrayList;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.NameServerMain;
import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.identification.lookup.Lookup;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.impl.LifecycleManagerImpl;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.LifecycleManager;
import br.ufrn.dimap.middleware.lifecycle.interfaces.Activate;
import br.ufrn.dimap.middleware.lifecycle.interfaces.Deactivate;
import br.ufrn.dimap.middleware.lifecycle.interfaces.PerRequest;
import br.ufrn.dimap.middleware.lifecycle.interfaces.Static;
import br.ufrn.dimap.middleware.remotting.impl.Invocation;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * 
 * @author victoragnez
 *
 */
public class TestManagers {
	public static class StaticClass1 implements Invoker {
		
		private int contador;
		private final Object lck = new Object();
		
		public StaticClass1() {
			contador = 0;
			System.out.println("Static1 created");
		}
		
		@Override
		public Object invoke(Invocation invocation) throws RemoteError, IOException, ClassNotFoundException {
			System.out.println("Invoke Static1");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (lck) {
				contador++;
			}
			System.out.println("Static1: " + contador);
			return contador;
		}
		
	}
	
	@Static
	public static class StaticClass2 implements Invoker {
		
		private int contador;
		private final Object lck = new Object();
		
		public StaticClass2() {
			contador = 0;
			System.out.println("Static2 created");
		}
		
		@Override
		public Object invoke(Invocation invocation) throws RemoteError, IOException, ClassNotFoundException {
			System.out.println("Invoke Static2");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (lck) {
				contador++;
			}
			System.out.println("Static2: " + contador);
			return contador;
		}
		
	}
	
	@PerRequest(poolSize = 2)
	public static class PerRequest1 implements Invoker {
		
		private int contador;
		private final Object lck = new Object();
		
		@Activate
		public void init() {
			contador = 0;
			System.out.println("Zero");
		}
		
		@Activate
		public void initialize() {
			System.out.println("Init");
		}
		
		@Deactivate
		public void destroy() {
			contador = Integer.MIN_VALUE;
			System.out.println("Destroyed");
		}
		
		@Override
		public Object invoke(Invocation invocation) throws RemoteError, IOException, ClassNotFoundException {
			System.out.println("Invoke PerRequest1");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (lck) {
				contador++;
			}
			System.out.println("PerRequest1: " + contador);
			return contador;
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		new Thread(() -> {
			NameServerMain.main(null);
		}).start();
		Thread.sleep(2000);
		Lookup server = DefaultLookup.getInstance();
		//server.bind("s1", StaticClass1.class, "", 0);
		System.out.println("s1 binded");
		//server.bind("s2", StaticClass2.class, "", 0);
		System.out.println("s2 binded");
		//server.bind("p1", PerRequest1.class, "", 0);
		System.out.println("p1 binded");
		AbsoluteObjectReference s1 = server.find("s1");
		System.out.println("s1 found");
		AbsoluteObjectReference s2 = server.find("s2");
		System.out.println("s2 found");
		AbsoluteObjectReference p1 = server.find("p1");
		System.out.println("p1 found");
		
		LifecycleManager manager = new LifecycleManagerImpl();
		ArrayList<Thread> list = new ArrayList<>();
		
		for(int i = 0; i < 5; i++) {
			list.add(new Thread(()-> {
				try {
					manager.getInvoker(s1).invoke(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} ));
		}
		
		for(int i = 0; i < 5; i++) {
			list.add(new Thread(()-> {
				try {
					manager.getInvoker(s2).invoke(null);
				} catch (ClassNotFoundException | RemoteError | IOException e) {
					e.printStackTrace();
				}
			} ));
		}
		
		for(int i = 0; i < 5; i++) {
			list.add(new Thread(()-> {
				try {
					manager.getInvoker(p1).invoke(null);
				} catch (ClassNotFoundException | RemoteError | IOException e) {
					e.printStackTrace();
				}
			} ));
		}
		
		for(Thread t : list) {
			t.start();
		}
		
		for(Thread t : list) {
			t.join();
		}
		
		System.out.println("End");
		
	}
	
}