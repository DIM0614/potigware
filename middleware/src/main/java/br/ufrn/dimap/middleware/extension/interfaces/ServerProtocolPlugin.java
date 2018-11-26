package br.ufrn.dimap.middleware.extension.interfaces;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Abstract class for server network plugins.
 * Defines the procedures to be used when receiving and returning data
 * from the network
 * 
 * @author Gustavo Carvalho
 */

public interface ServerProtocolPlugin {
	
	/**
	 * Returns the default port set to the protocol 
	 * @return the port
	 */
	public int getDefaultPort();
	
	/**
	 * Makes the plugin start listening for incoming connections.
	 * 
	 * @param responseHandler Hosts the callback function that handles the incoming data
	 * @throws RemoteError
	 */
	public void listen(int port, ResponseHandler responseHandler) throws RemoteError;
	
	/**
	 * Shutdown the plug-in. Used when shutting down the server.
	 * @throws RemoteError if any error occurs
	 */
	public void shutdown() throws RemoteError;
}
