package br.ufrn.dimap.middleware.extension.interfaces;

import br.ufrn.dimap.middleware.extension.impl.InvocationContext;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Represents an Invocation Interceptor for the CLIENT REQUEST HANDLER and
 * SERVER REQUEST HANDLER (Request Handling layer). 
 * Provide hooks in the invocation path, to plug in INVOCATION INTERCEPTORS. 
 * INVOCATION INTERCEPTORS are invoked before and after
 * request and reply messages pass the hook.
 * 
 * @author giovannirosario
 */

public interface InvocationInterceptorSerialized {
	byte[] intercept (byte[] inputStream, InvocationContext invocationContext) throws RemoteError;
}
