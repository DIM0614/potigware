package br.ufrn.dimap.middleware.remotting.interfaces;

import br.ufrn.dimap.middleware.extension.impl.InvocationContext;
import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.impl.InvocationData;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

import java.io.ByteArrayOutputStream;

/**
 * Represents a UnsyncRequestor, which abstracts
 * the network and interacts directly with
 * the request handlers and possibly the client.
 * 
 * @author vitorgreati
 */
public interface Requestor {

	/**
	 * Acquires the Absolute Object Reference via naming lookup,
	 * invokes the Marshaller and sends the bytes
	 * via the Client Request Handler.
	 * 
	 * @return the return of the invoked operation
	 */
	Object request(AbsoluteObjectReference aor, String operationName, Class<?> returnType, Object ... parameters)
			throws RemoteError;


	/**
	 * Acquires the Absolute Object Reference via naming lookup,
	 * invokes the Marshaller and sends the bytes
	 * via the Client Request Handler. Accepts
	 * a callback to be invoked after the result returns.
	 *
	 * @return the return of the invoked operation
	 */
	void request(AbsoluteObjectReference aor, String operationName,
				 Callback callback,
				 Class<?> returnType, Object ... parameters)
			throws RemoteError;

	/**
	 * Acquires the Absolute Object Reference via naming lookup,
	 * invokes the Marshaller and sends the bytes
	 * via the Client Request Handler. Used for
	 * some asynchronous communication modes.
	 *
	 *
	 * @return null unless the invocationAsyncPattern is defined to be by poll object
	 */
	Object request(AbsoluteObjectReference aor, String operationName,
				   InvocationAsynchronyPattern invocationAsyncPattern,
				   Class<?> returnType, Object ... parameters)
			throws RemoteError;
	
	/**
	 * Runs the registered request interceptors on the message
	 * @param msg the original message
	 * @param context the invocation context
	 * @return the modified message
	 * @throws RemoteError
	 */
	public ByteArrayOutputStream runRequestInterceptors(ByteArrayOutputStream msg, InvocationContext context) throws RemoteError;

	/**
	 * Runs the registered invocation interceptors on the invocationData
	 * @param invocationData the invocationData
	 * @param context the invocation context
	 * @throws RemoteError
	 */
	public void runInvocationInterceptors(InvocationData invocationData, InvocationContext context) throws RemoteError;
}
