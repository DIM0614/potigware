package br.ufrn.dimap.middleware.remotting.interfaces;

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
	 */
	public void init();
	
	/**
	 * Shutdowns the handler
	 */
	public void shutdown();

}
