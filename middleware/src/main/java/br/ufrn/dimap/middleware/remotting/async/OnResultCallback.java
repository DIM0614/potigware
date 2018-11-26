package br.ufrn.dimap.middleware.remotting.async;

/**
 * Represents an callback action to be invoked when the middleware returns the result to the client.
 *
 * @see br.ufrn.dimap.middleware.remotting.interfaces.Callback
 * @see br.ufrn.dimap.middleware.remotting.async.CallbackImpl
 *
 * @author Daniel Smith
 * @author Adelino Afonso
 * @author Jhonathan Cabral
 * @author Jonathan Rocha
 * @author Yuri Reinaldo  
 */
@FunctionalInterface
public interface OnResultCallback {

	/**
	 *
	 * Callback function to invoke when an error occurs.
	 *
	 * @param returnedData the returned data from the response that is resulting in the invocation of the callback.
	 */
	void invoke(Object returnedData);

}
