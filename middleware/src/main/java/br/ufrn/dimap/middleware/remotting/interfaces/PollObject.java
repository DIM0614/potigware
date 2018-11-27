package br.ufrn.dimap.middleware.remotting.interfaces;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Defines the PollObject to be used by the
 * Client Request Handler to store server's replies
 * when communicating using the Poll Object invocation
 * asynchrony pattern
 * 
 * @author victoragnez
 *
 */
public interface PollObject {
	/**
	 * Function to test whether there is an
	 * available result or not
	 * @return true if there's an available result and false otherwise
	 */
	public boolean resultAvailable();
	
	/**
	 * Function to get the first received and not taken result
	 * If there's no result available, returns null
	 * @return the result
	 * @throws RemoteError if a RemoteError occurs
	 */
	public Object getResult() throws RemoteError;
	
	/**
	 * Function to get the first received and not taken result
	 * If there's no result available, waits until a result is stored
	 * @return the result
	 * @throws InterruptedException if interrupted while waiting
	 * @throws RemoteError if a RemoteError occurs
	 */
	public Object getBlockingResult() throws InterruptedException, RemoteError;
	
	/**
	 * Function used by the client request handler to store a result object
	 * when communicating using the Poll Object invocation asynchrony pattern
	 * @param result the result to store
	 */
	public void storeResult(Object result);
	
	/**
	 * The method will be called if a RemoteError occurs
	 * 
	 * @param error the RemoteError
	 */
	public void onError(RemoteError error);
	
	/**
	 * Get expected type for result, if previously set
	 */
	public Class<?> getResultType();
	
	/**
	 * Set expected result type
	 * @param resultType the expected type
	 */
	public void setResultType(Class<?> resultType);
}
