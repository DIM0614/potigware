package br.ufrn.dimap.middleware.remotting.async;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Callback;

/**
 * Default implementation for the client side callback.
 *
 * @see Callback
 *
 * @author Daniel Smith
 *
 */
public class CallbackImpl implements Callback {

	/**
	 * Callback for the response result.
	 */
	private OnResultCallback onResult;

	/**
	 * Callback for remote error handling.
	 */
	private OnErrorCallback onError;

	/**
	 * Construtor for the default callback implementation.
	 *
	 * @param onResult the callback that will be invoked with the response data.
	 * @param onError the callback that will be invoked with the remote error, if occurs.
	 */
	public CallbackImpl(OnResultCallback onResult, OnErrorCallback onError) {
		this.onResult = onResult;
		this.onError = onError;
	}

	/**
	 * Invokes the OnResultCallback with the data returned from the server.
	 *
	 * @param returnedData the data returned from the server
	 */
	@Override
	public void onResult(Object returnedData) {
		if(onResult != null)
		
		onResult.invoke(returnedData);
	}
	
	/**
	 * The method will be called if a RemoteError occurs
	 * 
	 * @param error the RemoteError
	 */
	@Override
	public void onError(RemoteError error) {
		if(onError != null)
			
		onError.invoke(error);
	}

	/**
	 * Returns the builder for this implementation.
	 *
	 * @return a new <code>CallbackBuilder</code> instance.
	 */
	public static CallbackBuilder builder() {
		return new CallbackBuilder();
	}
	
}