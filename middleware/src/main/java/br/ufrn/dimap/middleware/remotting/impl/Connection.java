package br.ufrn.dimap.middleware.remotting.impl;

import java.net.Socket;

public class Connection {
	private String host;
	private int port;
	private Socket socket;
	
	public Connection(String host, int port, Socket socket) {
		this.host = host;
		this.port = port;
		this.socket = socket;
	}
}
