package br.ufrn.dimap.middleware.remotting.interfaces;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Interface for the Server Request Handler, 
 * responsible for managing connections with clients
 * 
 * @author victoragnez
 *
 */
public interface ServerRequestHandler {
	
	/**
	 * Default port to listen
	 */
	public static final int defaultPort = 13245;
	
	/**
	 * Initialize the handler
	 * @throws RemoteError if any error occurs
	 */
	public void init() throws RemoteError;
	
	/**
	 * Shutdowns the handler
	 */
	public void shutdown() throws RemoteError;

}
