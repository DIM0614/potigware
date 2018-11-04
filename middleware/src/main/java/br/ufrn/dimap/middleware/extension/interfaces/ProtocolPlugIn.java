package br.ufrn.dimap.middleware.extension.interfaces;

import java.io.ByteArrayOutputStream;

import br.ufrn.dimap.middleware.extension.interfaces.ResponseReceiverI;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
  * Abstract class for network plugins.
  * Defines the procedures to be used when sending and receiving data
  * from the network
  * 
  * @author Gustavo Carvalho
  * @author victoragnez
  */

public interface ProtocolPlugIn {
	/**
	 * Function used by the requestor and delegated by the client handler to send the data,
	 * handling network communication. Any return value must be sent to the ResponseReceiver interface.
	 *  
	 * @param host the hostname to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @param responseReceiver hosts the callback for the received message
	 */
	public abstract void send(String host, int port, ByteArrayOutputStream msg, ResponseReceiverI responseReceiver) throws RemoteError;
}
