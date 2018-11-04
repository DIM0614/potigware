/**
 * 
 */
package br.ufrn.dimap.middleware;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

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
		private static final Collection<Object> clientInvocationInteceptors = new ConcurrentLinkedQueue<>();
		private static final Collection<Object> serverInvocationInteceptors = new ConcurrentLinkedQueue<>();
		private static final Collection<Object> clientRequestInteceptors = new ConcurrentLinkedQueue<>();
		private static final Collection<Object> serverRequestInteceptors = new ConcurrentLinkedQueue<>();
		
		/**
		 * Registers an interceptor to be executed by the client on an
		 * invocation. Happens before the object serialization.
		 * 
		 * @param interceptor interceptor to be registered
		 */
		public static void registerClientInvocationInterceptor(Object interceptor) {
			clientInvocationInteceptors.add(interceptor);
		}
		
		/**
		 * Registers an interceptor to be executed by the server on an
		 * invocation. Happens after the object deserialization.
		 * 
		 * @param interceptor interceptor to be registered
		 */
		public static void registerServerInvocationInterceptor(Object interceptor) {
			serverInvocationInteceptors.add(interceptor);
		}
		
		/**
		 * Registers an interceptor to be executed by the client on an
		 * invocation. Happens after the object serialization.
		 * 
		 * @param interceptor interceptor to be registered
		 */
		public static void registerClientRequestInterceptor(Object interceptor) {
			clientRequestInteceptors.add(interceptor);
		}
	
		/**
		 * Registers an interceptor to be executed by the client on an
		 * invocation. Happens after the object serialization.
		 * 
		 * @param interceptor interceptor to be registered
		 */
		public static void registerServerRequestInterceptor(Object interceptor) {
			serverRequestInteceptors.add(interceptor);
		}

		/**
		 * Returns a collection with the client invocation interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public static Collection<Object> getClientinvocationinteceptors() {
			return clientInvocationInteceptors;
		}

		/**
		 * Returns a collection with the server invocation interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public static Collection<Object> getServerinvocationinteceptors() {
			return serverInvocationInteceptors;
		}

		/**
		 * Returns a collection with the client request interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public static Collection<Object> getClientrequestinteceptors() {
			return clientRequestInteceptors;
		}

		/**
		 * Returns a collection with the server request interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public static Collection<Object> getServerrequestinteceptors() {
			return serverRequestInteceptors;
		}
	}
	
	public static class ProtocolPlugins {
			private static final Map<String, Object> protocolPlugins = new ConcurrentHashMap<String, Object>();

			public static void addProtocolPlugin(String route, Object protocolPlugin) {
				protocolPlugins.put(route, protocolPlugin);
			}
			
			public static Object getProtocolplugin(String route) {
				return protocolPlugins.get(route);
			}
			
	}
}
