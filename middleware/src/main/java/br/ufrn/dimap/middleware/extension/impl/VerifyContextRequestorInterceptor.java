package br.ufrn.dimap.middleware.extension.impl;

import br.ufrn.dimap.middleware.extension.interfaces.InvocationInterceptorUnserialized;
import br.ufrn.dimap.middleware.remotting.impl.InvocationData;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Interceptor in the Request side that insert a value in the Invocation Context that will be verified in the Invoker side.
 * 
 * @author pedroarthur-mf
 *
 */
public class VerifyContextRequestorInterceptor implements InvocationInterceptorUnserialized {

	public void intercept(InvocationData invocationData, InvocationContext invocationContext) throws RemoteError {
		invocationContext.add("verify message", true);
	}
}
