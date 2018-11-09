package br.ufrn.dimap.middleware.remotting.interfaces;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Interface for callback objects,
 * which will be called after the server's communication,
 * using it's response as input to the onResult method.
 * 
 * @author victoragnez
 *
 */
public interface Callback {
	/**
	 * The method to be called from the Client Request Handler after 
	 * receiving the response from the server, using the 
	 * returned data as input for this method.
	 * 
	 * @param returnedData the data returned from the server
	 */
	public void onResult(Object returnedData);
	
	/**
	 * The method will be called if a RemoteError occurs
	 * 
	 * @param error the RemoteError
	 */
	public void onError(RemoteError error);
}
