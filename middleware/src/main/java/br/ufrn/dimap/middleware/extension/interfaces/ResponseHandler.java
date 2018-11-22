package br.ufrn.dimap.middleware.extension.interfaces;


/**
 * This interface is implemented by the Server Request Handler and hosts
 * a callback function that must be used to parse the incoming data
 * from a protocol plugin implementation
 * 
 * @author Gustavo Carvalho
 */

public interface ResponseHandler {
	
	/**
	 * Handles the incoming message and returns the message that must be sent
	 * back to the user.
	 * 
	 * @param msg the incoming message
	 * @return the outgoing message
	 */
	public Byte[] handleResponse(Byte[] msg);
}
