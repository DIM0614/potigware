package br.ufrn.dimap.middleware.remotting.interfaces;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

import java.io.IOException;

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
	Object request(AbsoluteObjectReference aor, String operationName, Object ... parameters)
			throws RemoteError, IOException, ClassNotFoundException;


	/**
	 * Acquires the Absolute Object Reference via naming lookup,
	 * invokes the Marshaller and sends the bytes
	 * via the Client Request Handler. Accepts
	 * a callback to be invoked after the result returns.
	 *
	 * @return the return of the invoked operation
	 */
	void request(AbsoluteObjectReference aor, String operationName, Callback callback, Object ... parameters)
			throws RemoteError, IOException, ClassNotFoundException;

	/**
	 * Acquires the Absolute Object Reference via naming lookup,
	 * invokes the Marshaller and sends the bytes
	 * via the Client Request Handler. Accepts
	 * a callback to be invoked after the result returns.
	 *
	 * @return the return of the invoked operation
	 */
	void request(AbsoluteObjectReference aor, String operationName, InvocationAsynchronyPattern invocationAsyncPattern, Object ... parameters)
			throws RemoteError, IOException, ClassNotFoundException;
}
