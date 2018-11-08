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
	 * Function used by the requestor and delegated by the client handler to send
	 * the data synchronously, handling network communication
	 *  
	 * @param host the host name to send the data
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
	 * the data asynchronously using the specified invocation asynchrony pattern
	 *  
	 * @param host the host name to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @return the server reply
	 * @throws RemoteError if pattern is SyncWithServer or PollObject and any error occurs when trying to connect to server
	 */
	public void send(String host, int port, ByteArrayOutputStream msg, InvocationAsynchronyPattern pattern) throws RemoteError;
	
	/**
	 * @return the poll object
	 */
	public PollObject getPollObject();

	/**
	 * @param pollObject the pollObject to set
	 */
	public void setPollObject(PollObject pollObject);
	
	/**
	 * Shutdown the plug-in. Called when Client Request Handler changes the protocol plug-in
	 * @throws RemoteError if any error occurs
	 */
	public void shutdown() throws RemoteError;
}
