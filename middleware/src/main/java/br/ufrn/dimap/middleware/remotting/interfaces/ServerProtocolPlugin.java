package br.ufrn.dimap.middleware.remotting.interfaces;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Defines the protocol to be used by the
 * Server Request Handler
 * 
 * @author Gustavo Carvalho
 * @author victoragnez
 *
 */
public interface ServerProtocolPlugin {
	/**
	 * Start to listen on a specific port
	 * @param port
	 */
	void listen(int port);
	
	/**
	 * Shutdowns the plug-in
	 * @throws RemoteError if any error occurs
	 */
	void shutdown() throws RemoteError;
}
