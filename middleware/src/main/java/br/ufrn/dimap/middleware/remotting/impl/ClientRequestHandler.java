package br.ufrn.dimap.middleware.remotting.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandlerI;

/**
 * This class implements the interface for Client Request Handler
 * 
 * @author victoragnez
 */

public class ClientRequestHandler implements ClientRequestHandlerI {
	
	public byte[] send(String host, int port, byte[] msg, boolean isUDP) throws UnknownHostException {
		InetAddress addr = InetAddress.getByName(host);
		byte[] reply = null;
		//TODO
		return reply;
	}
	
	public byte[] send(String host, byte[] msg, boolean isUDP) throws UnknownHostException {
		return send(host, defaultPort, msg, isUDP);
	}
	
	public byte[] send(String host, int port, byte[] msg) throws UnknownHostException {
		return send(host, port, msg, false);
	}

	public byte[] send(String host, byte[] msg) throws UnknownHostException {
		return send(host, defaultPort, msg, false);
	}

}
