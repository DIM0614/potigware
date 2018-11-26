package br.ufrn.dimap.middleware.remotting.interfaces;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.UnknownHostException;

import br.ufrn.dimap.middleware.extension.interfaces.ClientProtocolPlugIn;
import br.ufrn.dimap.middleware.infrastructure.qos.QoSObserver;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Interface for the Client Request Handler, 
 * responsible for connecting to the server
 * 
 * @author victoragnez
 *
 */
public interface ClientRequestHandler {

	/**
	 * Function used by the requestor to send the data,
	 * using the specific protocol (synchronized)
	 *  
	 * @param host the host name to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @return the server reply
	 * @throws RemoteError if any error occurs
	 */
	public ByteArrayInputStream send(String host, int port, ByteArrayOutputStream msg) throws RemoteError;
	
	/**
	 * Function used by the requestor to send the data asynchronously
	 * and then use the server's response as input of a callback method
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
	 * Function used by the requestor to send the data asynchronously
	 * and not to receive the response.
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
	 * Function used by the requestor to send the data asynchronously
	 * using the Poll Object pattern
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
	 * @return the default protocol
	 */
	public ClientProtocolPlugIn getDefaultProtocol();

	/**
	 * @param protocol the default protocol to set
	 * @throws RemoteError if any error occurs when changing the Protocol
	 */
	public void setDefaultProtocol(ClientProtocolPlugIn protocol) throws RemoteError;

	/**
	 * Gets protocol to be used to communicate with the specific host
	 * @param host the host
	 * @return the protocol
	 */
	public ClientProtocolPlugIn getProtocol(String host);

	/**
	 * Sets the protocol to be used when communicating to a specific host 
	 * @param host the route to set the protocol
	 * @param protocol the protocol to set
	 * @throws RemoteError if any error occurs when changing the protocol
	 * @throws UnknownHostException if it didn't find the specified host
	 */
	public void setProtocol(String host, ClientProtocolPlugIn protocol) throws RemoteError, UnknownHostException;

	/**
	 * Gets protocol to be used to communicate with the specified host
	 * in a specific port
	 * @param host the host
	 * @return the protocol
	 */
	public ClientProtocolPlugIn getProtocol(String host, int port);

	/**
	 * Sets the protocol to be used when communicating to a
	 * specific host in a specific port
	 * @param host the route to set the protocol
	 * @param protocol the protocol to set
	 * @throws RemoteError if any error occurs when changing the protocol
	 * @throws UnknownHostException if it didn't find the specified host
	 */
	public void setProtocol(String host, int port, ClientProtocolPlugIn protocol) throws RemoteError, UnknownHostException;
	
	/**
	 * Shutdowns all the plug-ins
	 * @throws RemoteError if any error occurs when shutdown
	 */
	public void shutdown() throws RemoteError;

	public QoSObserver getQosObserver();

}