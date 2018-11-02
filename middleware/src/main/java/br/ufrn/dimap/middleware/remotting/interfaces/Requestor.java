package br.ufrn.dimap.middleware.remotting.interfaces;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Represents a Requestor, which abstracts
 * the network and interacts directly with
 * the request handlers and possibly the client.
 * 
 * @author vitorgreati
 */
public interface Requestor<T> {

	/**
	 * Acquires the Absolute Object Reference via naming lookup,
	 * invokes the Marshaller and sends the bytes
	 * via the Client Request Handler.
	 * 
	 * @return the return of the invoked operation
	 */
	T request(AbsoluteObjectReference aor, String operationName, Object ... parameters) throws RemoteError;
	
}
