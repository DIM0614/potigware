package br.ufrn.dimap.middleware.remotting.interfaces;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Represents the connection protocol to the Client Request Handler,
 * which handles networking communication
 *  
 * @author victoragnez
 */

public interface ClientProtocolPlugin {
	
	/**
	 * Function used by the requestor and delegated by the client handler to send the data,
	 * handling network communication
	 *  
	 * @param host the hostname to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @return the server reply
	 */
	public ByteArrayInputStream send(String host, int port, ByteArrayOutputStream msg) throws RemoteError;
}
