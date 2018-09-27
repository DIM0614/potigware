package br.ufrn.dimap.middleware.remotting.interfaces;

/**
 * Represents a Requestor, which abstracts
 * the network and interacts directly with
 * the request handlers.
 * 
 * @author vitorgreati
 */
public interface RequestorI<T> {

	/**
	 * Acquires the Absolute Object Reference via naming lookup,
	 * invokes the Marshaller and sends the bytes
	 * via the Client Request Handler.
	 * 
	 * @return the return of the invoked operation
	 */
	public T invoke(String objectName, String operationName, Object ... parameters) throws Exception; 
	
}
