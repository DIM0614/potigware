package br.ufrn.dimap.middleware.async;

import br.ufrn.middleware.async.interfaces.OnErrorCallback;
import br.ufrn.middleware.async.interfaces.OnResultCallback;

public class CallbackBuilder{
	
	private OnResultCallback onResult;
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
