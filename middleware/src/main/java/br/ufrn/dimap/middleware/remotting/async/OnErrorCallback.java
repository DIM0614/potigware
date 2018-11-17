package br.ufrn.dimap.middleware.remotting.async;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Represents an callback action to be invoked in occurrence of an error in the middleware.
 *
 * @author Daniel Smith
 */
@FunctionalInterface
public interface OnErrorCallback {

	/**
	 *
	 * Callback function to invoke when an error occurs.
	 *
	 * @param error the remote error that is resulting in the invocation of the callback.
	 */
	void invoke(RemoteError error);

}
