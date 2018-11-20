/**
 * 
 */
package br.ufrn.dimap.middleware;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
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
		
		private final Queue<InvocationInterceptorUnserialized> clientInvocationInteceptors = new ConcurrentLinkedQueue<>();
		private final Queue<InvocationInterceptorUnserialized> serverInvocationInteceptors = new ConcurrentLinkedQueue<>();
		private final Queue<InvocationInterceptorSerialized> clientRequestInteceptors = new ConcurrentLinkedQueue<>();
		private final Queue<InvocationInterceptorSerialized> serverRequestInteceptors = new ConcurrentLinkedQueue<>();
		
		/**
		 * Registers an interceptor to be executed by the client on an
		 * invocation. Happens before the object serialization.
		 * 
		 * @param interceptor interceptor to be registered
		 */
		public void registerClientInvocationInterceptor(InvocationInterceptorUnserialized interceptor) {
			clientInvocationInteceptors.add(interceptor);
		}
		
		/**
		 * Registers an interceptor to be executed by the server on an
		 * invocation. Happens after the object deserialization.
		 * 
		 * @param interceptor interceptor to be registered
		 */
		public void registerServerInvocationInterceptor(InvocationInterceptorUnserialized interceptor) {
			serverInvocationInteceptors.add(interceptor);
		}
		
		/**
		 * Registers an interceptor to be executed by the client on an
		 * invocation. Happens after the object serialization.
		 * 
		 * @param interceptor interceptor to be registered
		 */
		public void registerClientRequestInterceptor(InvocationInterceptorSerialized interceptor) {
			clientRequestInteceptors.add(interceptor);
		}
	
		/**
		 * Registers an interceptor to be executed by the client on an
		 * invocation. Happens after the object serialization.
		 * 
		 * @param interceptor interceptor to be registered
		 */
		public void registerServerRequestInterceptor(InvocationInterceptorSerialized interceptor) {
			serverRequestInteceptors.add(interceptor);
		}

		/**
		 * Returns a collection with the client invocation interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public Collection<InvocationInterceptorUnserialized> getClientInvocationinteceptors() {
			return clientInvocationInteceptors;
		}

		/**
		 * Returns a collection with the server invocation interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public Collection<InvocationInterceptorUnserialized> getServerInvocationinteceptors() {
			return serverInvocationInteceptors;
		}

		/**
		 * Returns a collection with the client request interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public Collection<InvocationInterceptorSerialized> getClientrequestInteceptors() {
			return clientRequestInteceptors;
		}

		/**
		 * Returns a collection with the server request interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public Collection<InvocationInterceptorSerialized> getServerrequestInteceptors() {
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
			
			private final Map<String, ClientProtocolPlugIn> clientProtocolPlugins = new ConcurrentHashMap<String, ClientProtocolPlugIn>();
			private final Queue<ServerProtocolPlugin> serverProtocolPlugins = new ConcurrentLinkedQueue<>();

			/**
			 * Registers a new protocol plugin to be used when requesting an
			 * object using the specified route as a client 
			 *
			 * @param route the route identifier of the object
			 * @param protocolPlugin the plugin to be registered
			 */
			public void addClientProtocolPlugin(String route, ClientProtocolPlugIn protocolPlugin) {
				clientProtocolPlugins.put(route, protocolPlugin);
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
}
