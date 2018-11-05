package br.ufrn.dimap.middleware.extension.interfaces;

import br.ufrn.dimap.middleware.remotting.impl.InvocationData;
import br.ufrn.dimap.middleware.extension.impl.InvocationContext;

/*
 * Represents an Invocation Interceptor for the REQUESTOR/INVOKER (Invocation layer). 
 * Provide hooks in the invocation path, to plug in INVOCATION INTERCEPTORS. 
 * INVOCATION INTERCEPTORS are invoked before and after
 * request and reply messages pass the hook.
 * 
 * @author giovannirosario
 */

public interface InvocationInterceptorPostSerialization {
	void beforeInvocation (InvocationData invocationData) throws RemoteError;
	
	void beforeInvocation (InvocationData invocationData, InvocationContext invocationContext) throws RemoteError;
	
	void afterInvocation (InvocationData invocationData) throws RemoteError;
	
	void afterInvocation (InvocationData invocationData, InvocationContext invocationContext) throws RemoteError;
}
