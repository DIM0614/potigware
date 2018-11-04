package br.ufrn.dimap.middleware.extension.interfaces;

import java.io.ByteArrayInputStream;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Interface to be implemented by the RequestHandlers.
 * 
 * Handles the messages returned from the network connection by the
 * Protocol Plugin
 *  
 * @author Gustavo Carvalho
 *
 */
public interface ResponseReceiverI {
	
	/**
	 * Callback for the message returned from the network
	 * 
	 * @param msg the returned message
	 * @throws RemoteError
	 */
	public void receiveMsg(ByteArrayInputStream msg) throws RemoteError;
}
