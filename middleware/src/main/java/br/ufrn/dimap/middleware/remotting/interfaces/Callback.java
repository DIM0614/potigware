package br.ufrn.dimap.middleware.remotting.interfaces;

/**
 * Interface for callback objects,
 * which will be called after the server's communication,
 * using it's response as input to the callback method.
 * 
 * @author victoragnez
 *
 */
public interface Callback {
	/**
	 * The only method of the interface, which will be called
	 * from the Client Request Handler after receiving the response
	 * from the server, using the returned data as input for this method.
	 * 
	 * @param returnedData the data returned from the server
	 */
	public void run(Object returnedData);
}
