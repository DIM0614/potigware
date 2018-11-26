package br.ufrn.dimap.middleware.extension.interfaces;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import br.ufrn.dimap.middleware.extension.impl.InvocationContext;
import br.ufrn.dimap.middleware.remotting.impl.InvocationData;
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
	void beforeInvocation (ByteArrayInputStream inputStream, InvocationContext invocationContext) throws RemoteError;
	void afterInvocation (ByteArrayInputStream inputStream, InvocationContext invocationContext) throws RemoteError;
}
