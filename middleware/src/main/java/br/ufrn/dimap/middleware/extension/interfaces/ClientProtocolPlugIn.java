package br.ufrn.dimap.middleware.extension.interfaces;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
  * Abstract class for client network plugins.
  * Defines the procedures to be used when sending and receiving data
  * from the network
  * 
  * @author Gustavo Carvalho
  * @author victoragnez
  */

public interface ClientProtocolPlugIn {
	/**
	 * Function used by the requestor and delegated by the client handler to send the data,
	 * handling network communication.
	 *  
	 * @param host the hostname to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @return the server reply
	 */
	public ByteArrayInputStream send(String host, int port, ByteArrayOutputStream msg) throws RemoteError;
	
	/**
	 * Shutdown the plug-in. Called when Client Request Handler changes the protocol plug-in
	 * @throws RemoteError if any error occurs
	 */
	public void shutdown() throws RemoteError;
}
