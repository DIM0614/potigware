package br.ufrn.dimap.middleware.remotting.interfaces;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.UnknownHostException;

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
	 * using the specified invocation asynchrony pattern
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
	 * @return the default protocol
	 */
	public ClientProtocolPlugin getDefaultProtocol();

	/**
	 * @param protocol the default protocol to set
	 * @throws RemoteError if any error occurs when changing the Protocol
	 */
	public void setDefaultProtocol(ClientProtocolPlugin protocol) throws RemoteError;

	/**
	 * Gets protocol to be used to communicate with the specific host
	 * @param host the host
	 * @return the protocol
	 */
	public ClientProtocolPlugin getProtocol(String host);

	/**
	 * Sets the protocol to be used when communicating to a specific host 
	 * @param host the route to set the protocol
	 * @param protocol the protocol to set
	 * @throws RemoteError if any error occurs when changing the protocol
	 * @throws UnknownHostException if it didn't find the specified host
	 */
	public void setProtocol(String host, ClientProtocolPlugin protocol) throws RemoteError, UnknownHostException;

	/**
	 * Gets protocol to be used to communicate with the specified host
	 * in a specific port
	 * @param host the host
	 * @return the protocol
	 */
	public ClientProtocolPlugin getProtocol(String host, int port);

	/**
	 * Sets the protocol to be used when communicating to a
	 * specific host in a specific port
	 * @param host the route to set the protocol
	 * @param protocol the protocol to set
	 * @throws RemoteError if any error occurs when changing the protocol
	 * @throws UnknownHostException if it didn't find the specified host
	 */
	public void setProtocol(String host, int port, ClientProtocolPlugin protocol) throws RemoteError, UnknownHostException;
	
	/**
	 * Shutdowns all the plug-ins
	 */
	public void shutdown();

}