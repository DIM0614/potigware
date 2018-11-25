/**
 * 
 */
package br.ufrn.dimap.middleware;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import br.ufrn.dimap.middleware.extension.interfaces.ClientProtocolPlugIn;
import br.ufrn.dimap.middleware.extension.interfaces.InvocationInterceptorSerialized;
import br.ufrn.dimap.middleware.extension.interfaces.InvocationInterceptorUnserialized;
import br.ufrn.dimap.middleware.extension.interfaces.ServerProtocolPlugin;

/**
 * This static class serves as a facade for the configuration of the
 * middleware. Interceptors, plugins and other features are registered
 * here and are available for the whole  
 * 
 * @author Gustavo Carvalho
 *
 */
public final class MiddlewareConfig {
	
	/**
	 * This nested class holds the information regarding the interceptors of the framework
	 * 
	 * @author Gustavo Carvalho
	 */
	public static class Interceptors {
		private Interceptors() {}
		
		/**
		 * Wraps the instance
		 */
		private static Wrapper<Interceptors> wrapper;
		
		/**
		 * Creates a single instance, guarantees safe publication
		 * @return
		 */
		public static Interceptors getInstance () {
			Wrapper<Interceptors> w = wrapper;
	        if (w == null) { // check 1
	        	synchronized (ProtocolPlugins.class) {
	        		w = wrapper;
	        		if (w == null) { // check 2
	        			w = new Wrapper<Interceptors>(new Interceptors());
	        			wrapper = w;
	        		}
	        	}
	        }
	        
	        return w.getInstance();
		}
		
		private final Map<String, InvocationInterceptorUnserialized> invocationInterceptors = new ConcurrentHashMap<>(); 
		private final Map<String, InvocationInterceptorSerialized> requestInterceptors = new ConcurrentHashMap<>(); 
		
		private final Queue<InvocationInterceptorUnserialized> clientInvocationInteceptors = new ConcurrentLinkedQueue<>();
		private final Queue<InvocationInterceptorUnserialized> serverInvocationInteceptors = new ConcurrentLinkedQueue<>();
		private final Queue<InvocationInterceptorSerialized> clientRequestInteceptors = new ConcurrentLinkedQueue<>();
		private final Queue<InvocationInterceptorSerialized> serverRequestInteceptors = new ConcurrentLinkedQueue<>();
		
		/**
		 * Registers an invocation interceptor under the given name
		 * @param name
		 * @param interceptor
		 */
		
		public void registerInvocationInterceptor(String name, InvocationInterceptorUnserialized interceptor) {
			invocationInterceptors.put(name, interceptor);
		}
		
		/**
		 * Registers a request interceptor under the given name
		 * @param name
		 * @param interceptor
		 */
		public void registerRequestInterceptor(String name, InvocationInterceptorSerialized interceptor) {
			requestInterceptors.put(name, interceptor);
		}
		
		/**
		 * Adds a pre-registered invocation interceptor to the client
		 * invocation interceptor queue
		 * 
		 * @param name the interceptor identifier
		 * @throws MiddlewareConfigException 
		 */
		public void startClientInvocationInterceptor(String name) throws MiddlewareConfigException {
			InvocationInterceptorUnserialized iiu = invocationInterceptors.get(name);
			if (iiu == null) throw new MiddlewareConfigException("Interceptor not found");
			clientInvocationInteceptors.add(iiu);
		}
		
		/**
		 * Removes a pre-registered invocation interceptor to the client
		 * invocation interceptor queue
		 * 
		 * @param name the interceptor identifier
		 * @throws MiddlewareConfigException 
		 */
		public void stopClientInvocationInterceptor(String name) throws MiddlewareConfigException {
			InvocationInterceptorUnserialized iiu = invocationInterceptors.get(name);
			if (iiu == null) throw new MiddlewareConfigException("Interceptor not found");
			clientInvocationInteceptors.remove(iiu);
		}
		
		/**
		 * Adds a pre-registered invocation interceptor to the server
		 * invocation interceptor queue
		 * 
		 * @param name the interceptor identifier
		 * @throws MiddlewareConfigException 
		 */
		public void startServerInvocationInterceptor(String name) throws MiddlewareConfigException {
			InvocationInterceptorUnserialized iiu = invocationInterceptors.get(name);
			if (iiu == null) throw new MiddlewareConfigException("Interceptor not found");
			serverInvocationInteceptors.add(iiu);
		}
		
		/**
		 * Removes a pre-registered invocation interceptor to the server
		 * invocation interceptor queue
		 * 
		 * @param name the interceptor identifier
		 * @throws MiddlewareConfigException 
		 */
		public void stopServerInvocationInterceptor(String name) throws MiddlewareConfigException {
			InvocationInterceptorUnserialized iiu = invocationInterceptors.get(name);
			if (iiu == null) throw new MiddlewareConfigException("Interceptor not found");
			serverInvocationInteceptors.remove(iiu);
		}
		
		/**
		 * Adds a pre-registered request interceptor to the client
		 * request interceptor queue
		 * 
		 * @param name the interceptor identifier
		 * @throws MiddlewareConfigException 
		 */
		public void startClientRequestInterceptor(String name) throws MiddlewareConfigException {
			InvocationInterceptorSerialized iis = requestInterceptors.get(name);
			if (iis == null) throw new MiddlewareConfigException("Interceptor not found");
			clientRequestInteceptors.add(iis);
		}
	
		/**
		 * Removes a pre-registered request interceptor to the client
		 * request interceptor queue
		 * 
		 * @param name the interceptor identifier
		 * @throws MiddlewareConfigException 
		 */
		public void stopClientRequestInterceptor(String name) throws MiddlewareConfigException {
			InvocationInterceptorSerialized iis = requestInterceptors.get(name);
			if (iis == null) throw new MiddlewareConfigException("Interceptor not found");
			clientRequestInteceptors.remove(iis);
		}
		
		/**
		 * Adds a pre-registered request interceptor to the server
		 * request interceptor queue
		 * 
		 * @param name the interceptor identifier
		 * @throws MiddlewareConfigException 
		 */
		public void startServerRequestInterceptor(String name) throws MiddlewareConfigException {
			InvocationInterceptorSerialized iis = requestInterceptors.get(name);
			if (iis == null) throw new MiddlewareConfigException("Interceptor not found");
			serverRequestInteceptors.add(iis);
		}

		/**
		 * Removes a pre-registered request interceptor to the server
		 * request interceptor queue
		 * 
		 * @param name the interceptor identifier
		 * @throws MiddlewareConfigException 
		 */
		public void stopServerRequestInterceptor(String name) throws MiddlewareConfigException {
			InvocationInterceptorSerialized iis = requestInterceptors.get(name);
			if (iis == null) throw new MiddlewareConfigException("Interceptor not found");
			serverRequestInteceptors.remove(iis);
		}
		
		/**
		 * Returns a collection with the client invocation interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public Collection<InvocationInterceptorUnserialized> getClientInvocationInteceptors() {
			return clientInvocationInteceptors;
		}

		/**
		 * Returns a collection with the server invocation interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public Collection<InvocationInterceptorUnserialized> getServerInvocationInteceptors() {
			return serverInvocationInteceptors;
		}

		/**
		 * Returns a collection with the client request interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public Collection<InvocationInterceptorSerialized> getClientRequestInteceptors() {
			return clientRequestInteceptors;
		}

		/**
		 * Returns a collection with the server request interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public Collection<InvocationInterceptorSerialized> getServerRequestInteceptors() {
			return serverRequestInteceptors;
		}
	}
	
	/**
	 * This nested class holds the information regarding the protocolPlugins of the framework
	 * 
	 * @author Gustavo Carvalho
	 */
	public static class ProtocolPlugins {
			private ProtocolPlugins() {};
		
			/**
			 * Wraps the instance
			 */
			private static Wrapper<ProtocolPlugins> wrapper;
			
			/**
			 * Creates a single instance, guarantees safe publication
			 * @return
			 */
			public static ProtocolPlugins getInstance () {
				Wrapper<ProtocolPlugins> w = wrapper;
		        if (w == null) { // check 1
		        	synchronized (ProtocolPlugins.class) {
		        		w = wrapper;
		        		if (w == null) { // check 2
		        			w = new Wrapper<ProtocolPlugins>(new ProtocolPlugins());
		        			wrapper = w;
		        		}
		        	}
		        }
		        
		        return w.getInstance();
			}
			
			private final Map<String, ClientProtocolPlugIn> registeredClientProtocolPlugins = new ConcurrentHashMap<String, ClientProtocolPlugIn>();
			private final Map<String, ClientProtocolPlugIn> clientProtocolPlugins = new ConcurrentHashMap<String, ClientProtocolPlugIn>();
			private final Queue<ServerProtocolPlugin> serverProtocolPlugins = new ConcurrentLinkedQueue<>();

			/**
			 * Registers a new client protocol plugin under a specified name
			 *
			 * @param name the name which identifies the plugin
			 * @param protocolPlugin the plugin to be registered
			 */
			public void registerClientProtocolPlugin(String name, ClientProtocolPlugIn protocolPlugin) {
				registeredClientProtocolPlugins.put(name, protocolPlugin);
			}
			
			/**
			 * Sets a protocol plugin to be used for a specified
			 * route  
			 * 
			 * @param route the route to be set
			 * @param protocolName the name of the plugin to be used
			 */
			public void setClientProtocolPlugin(String route, String protocolName) throws MiddlewareConfigException {
				ClientProtocolPlugIn cpp = registeredClientProtocolPlugins.get(protocolName);
				if (cpp == null) throw new MiddlewareConfigException("The specified protocol plugin doesn't exist");
				clientProtocolPlugins.put(route, cpp);
			}
			
			/**
			 * Registers a new protocol plugin to be used when serving by the server
			 *
			 * @param protocolPlugin the plugin to be registered
			 */
			public void addServerProtocolPlugin(ServerProtocolPlugin protocolPlugin) {
				serverProtocolPlugins.add(protocolPlugin);
			}
			
			/**
			 * Returns the protocol plugin to be used when acting as a client
			 * for the object with the specified route 
			 * 
			 * @param route the route identifier of the object
			 * @return the protocol plugin if registered, else null
			 */
			public ClientProtocolPlugIn getClientProtocolPlugin(String route) {
				return clientProtocolPlugins.get(route);
			}
			
			/**
			 * Returns the protocol plugins to be used when acting as a server 
			 * 
			 * @return the protocol plugins
			 */
			public Collection<ServerProtocolPlugin> getServerProtocolPlugins() {
				return serverProtocolPlugins;
			}
	}
	
	/**
	 * 
	 * Wraps the instance to allow final modifier
	 * 
	 * @author victoragnez
	 * 
	 * @param <T> the type to be wrapped
	 */
	private static class Wrapper<T> {
		private final T instance;
	    public Wrapper(T service) {
	        this.instance = service;
	    }
	    public T getInstance() {
	        return instance;
	    }
	}
	
	/**	
	 * Starts the server middleware server and listens for further commands
	 * 
	 */
	public static void startServer() {
		// TODO: Start server 
		
		Scanner scan = new Scanner(System.in);
		
		while(true) {
	        System.out.println("What do you want to do?");
	        System.out.println("\t 1 - To activate a Interceptor for the marshalled data;");
	        System.out.println("\t 2 - To disable a Interceptor for the marshalled data;");
	        System.out.println("\t 3 - To activate a Interceptor before the invoker");
	        System.out.println("\t 4 - To disable a Interceptor before the invoker;");
	        System.out.println("Or press Q to stop que middleware;");
	        
	        String option = scan.next();
	       
	        Interceptors interceptors = new Interceptors();
	        String name;
	        try {
		        if(option == "1") {
		        	System.out.println("Write the name of the Interceptor that you want to activate for the marshalled data");
		        	name = scan.next();
		        	interceptors.startServerRequestInterceptor(name);
		        }
		        else if(option == "2") {
		        	System.out.println("Write the name of the Interceptor that you want to disable for the marshalled data");
		        	name = scan.next();
		        	interceptors.stopServerRequestInterceptor(name);
		        }
		        else if(option == "3") {
		        	System.out.println("Write the name of the Interceptor that you want to activate before the invoker");
		        	name = scan.next();
		        	interceptors.startServerInvocationInterceptor(name);
		        }
				else if(option == "4") {
					System.out.println("Write the name of the Interceptor that you want to disable before the invoker");
					name = scan.next();
					interceptors.stopServerInvocationInterceptor(name);
				}
				else if(option == "Q" || option == "q"){
					System.out.println("Exiting...");
					//TODO: Stop the middlewere
					break;
				}
			} catch (MiddlewareConfigException e) {
				System.out.println(e.getMessage() + ". Please, restart the process...\n\n");
			}
		}
		
		scan.close();
	}
}
