package br.ufrn.dimap.middleware.extension.impl;

import br.ufrn.dimap.middleware.extension.interfaces.InvocationInterceptorUnserialized;
import br.ufrn.dimap.middleware.remotting.impl.InvocationData;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
/**
 * Interceptor in the Invoker side that verifies if a specific key on the Invocation Context has a specific value.
 * 
 * @author Pedro Arthur Medeiros
 *
 */
public class VerifyContexInvokertInterceptor implements InvocationInterceptorUnserialized {

	public void intercept(InvocationData invocationData, InvocationContext invocationContext) throws RemoteError {
		if (invocationContext.get("verify message") != (Boolean) true) {
			throw new RemoteError("Invocation Context does not match with the expected value for the key 'verify message'.");
		}
	}

}
