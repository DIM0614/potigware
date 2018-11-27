/**
 * 
 */
package br.ufrn.dimap.middleware;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import br.ufrn.dimap.middleware.extension.interfaces.ClientProtocolPlugIn;
import br.ufrn.dimap.middleware.extension.interfaces.InvocationInterceptorSerialized;
import br.ufrn.dimap.middleware.extension.interfaces.InvocationInterceptorUnserialized;
import br.ufrn.dimap.middleware.extension.interfaces.ServerProtocolPlugin;
import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.impl.ClientRequestHandlerImpl;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.utils.Wrapper;

/**
 * This static class serves as a facade for the configuration of the
 * middleware. Interceptors, plugins and other features are registered
 * here and are available for the whole  
 * 
 * @author Gustavo Carvalho
 *
 */
public final class MiddlewareConfig {

	private static final AbsoluteObjectReference configAor = new AbsoluteObjectReference(new ObjectId("e5d57a7b-75bd-4648-814d-3ea9d083c526"), "35.231.37.80", 8001);
	//private static final AbsoluteObjectReference configAor = new AbsoluteObjectReference(new ObjectId("e5d57a7b-75bd-4648-814d-3ea9d083c526"), "localhost", 8001);
	
	public static AbsoluteObjectReference getConfigaor() {
		return configAor;
	}
	
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
		public Queue<InvocationInterceptorUnserialized> getClientInvocationInteceptors() {
			return clientInvocationInteceptors;
		}

		/**
		 * Returns a collection with the server invocation interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public Queue<InvocationInterceptorUnserialized> getServerInvocationInteceptors() {
			return serverInvocationInteceptors;
		}

		/**
		 * Returns a collection with the client request interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public Queue<InvocationInterceptorSerialized> getClientRequestInteceptors() {
			return clientRequestInteceptors;
		}

		/**
		 * Returns a collection with the server request interceptors in the order
		 * they must be applied
		 * @return the interceptor collection
		 */
		public Queue<InvocationInterceptorSerialized> getServerRequestInteceptors() {
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
		
		private final Queue<ServerProtocolPlugin> serverProtocolPlugins = new ConcurrentLinkedQueue<>();

		/**
		 * Registers a new Client protocol plugin for a specified host and port
		 * @param host
		 * @param port
		 * @param protocolPlugin
		 * @throws MiddlewareConfigException
		 */
		public void registerClientProtocolPlugin(String host, int port, ClientProtocolPlugIn protocolPlugin) throws MiddlewareConfigException {
			try {
				ClientRequestHandlerImpl.getInstance().setProtocol(host, port, protocolPlugin);
			} catch (UnknownHostException | RemoteError e) {
				throw new MiddlewareConfigException(e.getMessage());
			}
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
		 * Returns the protocol plugins to be used when acting as a server 
		 * 
		 * @return the protocol plugins
		 */
		public Collection<ServerProtocolPlugin> getServerProtocolPlugins() {
			return serverProtocolPlugins;
		}
	}
}
