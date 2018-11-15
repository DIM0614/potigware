package br.ufrn.dimap.middleware.remotting.interfaces;

/**
 * Defines the protocol to be used by the
 * Server Request Handler
 * 
 * @author victoragnez
 *
 */
public interface ServerProtocolPlugin {
	/**
	 * Start to listen on a specific port
	 * @param port
	 */
	void init(int port);
	
	/**
	 * Shutdowns the plug-in
	 */
	void shutdown();
}
