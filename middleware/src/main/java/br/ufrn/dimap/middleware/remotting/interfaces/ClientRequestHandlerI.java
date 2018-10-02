package br.ufrn.dimap.middleware.remotting.interfaces;

import java.net.UnknownHostException;

/**
 * Represents a Client Request Handler,
 * which handles networking synchronous communication
 * inside client applications
 * 
 * TODO allow asynchronous communication?
 * 
 * @author victoragnez
 */

public interface ClientRequestHandlerI {
	
	//Default port used by the middleware
	static final int defaultPort = 23456;
	
	/**
	 * Function used by the requestor to send the data,
	 * handling network communication
	 *  
	 * @param host the hostname to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @param isUDP a flag to determine the protocol between TCP and UDP
	 * @return the server reply
	 * @throws UnknownHostException 
	 */
	public byte[] send(String host, int port, byte[] msg, boolean isUDP) throws UnknownHostException;
	
	/**
	 * Function used by the requestor to send the data,
	 * handling network communication
	 *  
	 * @param host the hostname to send the data
	 * @param msg the data to be sent
	 * @param isUDP a flag to determine the protocol between TCP and UDP
	 * @return the server reply
	 */
	public byte[] send(String host, byte[] msg, boolean isUDP) throws UnknownHostException;
	
	/**
	 * Function used by the requestor to send the data,
	 * handling network communication
	 *  
	 * @param host the hostname to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @return the server reply
	 */
	public byte[] send(String host, int port, byte[] msg) throws UnknownHostException;
	
	/**
	 * Function used by the requestor to send the data,
	 * handling network communication
	 *  
	 * @param host the hostname to send the data
	 * @param msg the data to be sent
	 * @return the server reply
	 */
	public byte[] send(String host, byte[] msg) throws UnknownHostException;
}
