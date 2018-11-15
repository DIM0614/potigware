package br.ufrn.dimap.middleware.remotting.async;

/**
 * Builder class for the default callback implementation.
 *
 * @author Daniel Smith
 */
public class CallbackBuilder{

	/**
	 * The functional interface to be called after the result reatrieval.
	 */
	private OnResultCallback onResult;

	/**
	 * The functional interface to be called if an error occurs.
	 */
	private OnErrorCallback onError;

	public CallbackBuilder onResult(OnResultCallback onResultCallback) {
		this.onResult = onResultCallback;
		return this;
	}

	public CallbackBuilder onError(OnErrorCallback onErrorCallback) {	
		this.onError = onErrorCallback;
		return this;
	}

	public CallbackImpl build() {
		return new CallbackImpl(onResult, onError);
	}
	
}
