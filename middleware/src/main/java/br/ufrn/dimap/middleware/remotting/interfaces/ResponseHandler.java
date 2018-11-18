package br.ufrn.dimap.middleware.remotting.interfaces;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * This interface is implemented by the Server Request Handler and hosts
 * a callback function that must be used to parse the incoming data
 * from a protocol plug-in implementation
 * 
 * @author Gustavo Carvalho
 * @author victoragnez
 */

public interface ResponseHandler {
	
	/**
	 * Handles the incoming message and returns the message that must be sent
	 * back to the user.
	 * 
	 * @param msg the input bytes
	 * @param respond true if it's expected to marshal and send the result
	 * @return the output bytes if respond equals true and null otherwise
	 * @throws RemoteError if any error occurs
	 */
	public byte[] handleResponse(byte[] msg, boolean respond) throws RemoteError;
}
