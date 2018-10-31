package br.ufrn.dimap.middleware.remotting.impl;

import java.io.IOException;
import java.net.Socket;

public class Connection {
	private final String host;
	private final int port;
	private final Socket socket;
	private long currentDeathTime;
	private boolean used;
	
	public Connection(String host, int port, Socket socket) {
		this.host = host;
		this.port = port;
		this.socket = socket;
	}
	
	/*
	 * Closes the socket
	 */
	public synchronized void close() throws IOException {
		if(!socket.isClosed())
			socket.close();
	}
	
	/**
	 * Check if it's been used
	 * @return true if its used
	 */
	public boolean isUsed() {
		return used;
	}
	
	/**
	 * Asks to use the connection
	 * @return true if it was available and false if it's already in use
	 */
	public synchronized boolean use() {
		if(isUsed()) {
			return false;
		}
		used = true;
		return true;
	}
	
	/**
	 * Shows that this connection is available now
	 */
	public synchronized void finish() {
		used = false;
	}

	/**
	 * @return the currentDeathTime
	 */
	public long getCurrentDeathTime() {
		return currentDeathTime;
	}

	/**
	 * @param currentDeathTime the currentDeathTime to set
	 */
	public void setCurrentDeathTime(long currentDeathTime) {
		this.currentDeathTime = currentDeathTime;
	}
	
	
}
