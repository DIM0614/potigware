/**
 * 
 */
package br.ufrn.dimap.middleware;

import java.util.Collection;
import java.util.Map;
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
public class MiddlewareConfig {
	
	/**
	 * This nested class holds the information regarding the interceptors of the framework
	 * 
	 * @author Gustavo Carvalho
	 */
	public static class Interceptors {
		private static final Collection<InvocationInterceptorUnserialized> clientInvocationInteceptors = new ConcurrentLinkedQueue<>();
		private static final Collection<InvocationInterceptorUnserialized> serverInvocationInteceptors = new ConcurrentLinkedQueue<>();
		private static final Collection<InvocationInterceptorSerialized> clientRequestInteceptors = new ConcurrentLinkedQueue<>();
		private static final Collection<InvocationInterceptorSerialized> serverRequestInteceptors = new ConcurrentLinkedQueue<>();
		
		/**
		 * Registers an interceptor to be executed by the client on an
		 * invocation. Happens before the object serialization.
		 * 
		 * @param interceptor interceptor to be registered
		 */
		public static void registerClientInvocationInterceptor(InvocationInterceptorUnserialized interceptor) {
			clientInvocationInteceptors.add(interceptor);
		}
		
		/**
		 * Registers an interceptor to be executed by the server on an
		 * invocation. Happens after the object deserialization.
		 * 
		 * @param interceptor interceptor to be registered
		 */
		public static void registerServerInvocationInterceptor(InvocationInterceptorUnserialized interceptor) {
			serverInvocationInteceptors.add(interceptor);
		}
		
		/**
		 * Registers an interceptor to be executed by the client on an
		 * invocation. Happens after the object serialization.
		 * 
		 * @param interceptor interceptor to be registered
		 */
		public static void registerClientRequestInterceptor(InvocationInterceptorSerialized interceptor) {
			clientRequestInteceptors.add(interceptor);
		}
	
		/**
		 * Registers an interceptor to be executed by the client on an
		 * invocation. Happens after the object serialization.
		 * 
		 * @param interceptor interceptor to be registered
		 */
		public static void registerServerRequestInterceptor(InvocationInterceptorSerialized interceptor) {
			serverRequestInteceptors.add(interceptor);
		}

		/**
		 * Returns a collection with the client invocation interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public static Collection<InvocationInterceptorUnserialized> getClientinvocationinteceptors() {
			return clientInvocationInteceptors;
		}

		/**
		 * Returns a collection with the server invocation interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public static Collection<InvocationInterceptorUnserialized> getServerinvocationinteceptors() {
			return serverInvocationInteceptors;
		}

		/**
		 * Returns a collection with the client request interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public static Collection<InvocationInterceptorSerialized> getClientrequestinteceptors() {
			return clientRequestInteceptors;
		}

		/**
		 * Returns a collection with the server request interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public static Collection<InvocationInterceptorSerialized> getServerrequestinteceptors() {
			return serverRequestInteceptors;
		}
	}
	
	/**
	 * This nested class holds the information regarding the protocolPlugins of the framework
	 * 
	 * @author Gustavo Carvalho
	 */
	public static class ProtocolPlugins {
			private static final Map<String, ClientProtocolPlugIn> clientProtocolPlugins = new ConcurrentHashMap<String, ClientProtocolPlugIn>();
			private static final Collection<ServerProtocolPlugin> serverProtocolPlugins = new ConcurrentLinkedQueue<>();

			/**
			 * Registers a new protocol plugin to be used when requesting an
			 * object using the specified route as a client 
			 *
			 * @param route the route identifier of the object
			 * @param protocolPlugin the plugin to be registered
			 */
			public static void addClientProtocolPlugin(String route, ClientProtocolPlugIn protocolPlugin) {
				clientProtocolPlugins.put(route, protocolPlugin);
			}
			
			/**
			 * Registers a new protocol plugin to be used when serving by the server
			 *
			 * @param protocolPlugin the plugin to be registered
			 */
			public static void addServerProtocolPlugin(ServerProtocolPlugin protocolPlugin) {
				serverProtocolPlugins.add(protocolPlugin);
			}
			
			/**
			 * Returns the protocol plugin to be used when acting as a client
			 * for the object with the specified route 
			 * 
			 * @param route the route identifier of the object
			 * @return the protocol plugin if registered, else null
			 */
			public static ClientProtocolPlugIn getClientProtocolPlugin(String route) {
				return clientProtocolPlugins.get(route);
			}
			
			/**
			 * Returns the protocol plugins to be used when acting as a server 
			 * 
			 * @return the protocol plugins
			 */
			public static Collection<ServerProtocolPlugin> getServerProtocolPlugins() {
				return serverProtocolPlugins;
			}
	}
}
