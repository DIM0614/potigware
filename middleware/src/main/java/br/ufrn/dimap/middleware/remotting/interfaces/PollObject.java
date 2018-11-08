package br.ufrn.dimap.middleware.remotting.interfaces;

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
	 */
	public Object getResult();
	
	/**
	 * Function to get the first received and not taken result
	 * If there's no result available, waits until a result is stored
	 * @return the result
	 */
	public Object getBlockingResult();
	
	/**
	 * Function used by the client request handler to store a result object
	 * when communicating using the Poll Object invocation asynchrony pattern
	 * @param obj the result to store
	 */
	public void storeResult(Object obj);
}
