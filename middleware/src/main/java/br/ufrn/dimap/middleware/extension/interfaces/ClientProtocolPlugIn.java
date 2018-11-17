package br.ufrn.dimap.middleware.extension.interfaces;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Callback;
import br.ufrn.dimap.middleware.remotting.interfaces.PollObject;

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
	 * Function used by the requestor and delegated by the client handler to send 
	 * the data asynchronously and then use the server's response as 
	 * input of a callback method
	 *  
	 * @param host the host name to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @param callback the callback object to be called after
	 * @return the server reply
	 * @throws RemoteError if any error occurs
	 */

	public void send(String host, int port, ByteArrayOutputStream msg, Callback callback) throws RemoteError;

	/**
	 * Function used by the requestor and delegated by the client handler to send
	 * the data asynchronously and not to receive the response.
	 *
	 * If waitConfirmation is false, uses the Fire and Forget pattern
	 * 
	 * Otherwise, uses the Sync with Server pattern, that confirms the
	 * data was sent
	 *  
	 * @param host the host name to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @param waitConfirmation true to guarantee the data was sent
	 * @return the server reply
	 * @throws RemoteError if waitConfirmation is true and an error occurs when connecting to the server
	 */
	public void send(String host, int port, ByteArrayOutputStream msg, boolean waitConfirmation) throws RemoteError;
	
	/**
	 * Function used by the requestor and delegated by the client handler to send
	 * the data asynchronously using the Poll Object pattern
	 *  
	 * @param host the host name to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @param pollObject the pollObject to store the response from server
	 * @return the server reply
	 * @throws RemoteError if pattern is SyncWithServer and any error occurs when trying to connect to server
	 */
	public void send(String host, int port, ByteArrayOutputStream msg, PollObject pollObject) throws RemoteError;
	
	/**
	 * Shutdown the plug-in. Called when Client Request Handler changes the protocol plug-in
	 * @throws RemoteError if any error occurs
	 */
	public void shutdown() throws RemoteError;
}
