package br.ufrn.dimap.middleware.async;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.middleware.async.interfaces.OnErrorCallback;
import br.ufrn.middleware.async.interfaces.OnResultCallback;

/**
 * Interface for callback objects,
 * which will be called after the server's communication,
 * using it's response as input to the onResult method.
 * 
 * @author victoragnez
 *
 */
public class CallbackImpl {

	private OnResultCallback onResult;
	private OnErrorCallback onError;

	public CallbackImpl(OnResultCallback onResult, OnErrorCallback onError) {
		this.onResult = onResult;
		this.onError = onError;
	}

	/**
	 * The method to be called from the Client Request Handler after 
	 * receiving the response from the server, using the 
	 * returned data as input for this method.
	 * 
	 * @param returnedData the data returned from the server
	 */
	public void onResult(Object returnedData) {
		if(onResult != null)
		
		onResult.invoke(returnedData);
	}
	
	/**
	 * The method will be called if a RemoteError occurs
	 * 
	 * @param error the RemoteError
	 */
	public void onError(RemoteError error) {
		if(onError != null)
			
		onError.invoke(error);
	}
	
	public static CallbackBuilder builder() {
		return new CallbackBuilder();
	}
	
}