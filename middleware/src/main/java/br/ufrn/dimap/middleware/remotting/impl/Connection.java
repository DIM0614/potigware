package br.ufrn.dimap.middleware.remotting.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Wraps connections and helps to deal with concurrent issues
 * 
 * @author victoragnez
 *
 */
public class Connection {
	private final Socket socket;
	private DataOutputStream outToServer;
	private DataInputStream inFromServer;
	private long currentDeathTime = 0L;
	private boolean used = false;
	
	public Connection(String host, int port) throws RemoteError {
		try {
			this.socket = new Socket(host, port);
			this.outToServer = null;
			this.inFromServer = null;
		} catch (IOException e) {
			throw new RemoteError(e);
		}
	}
	
	/**
	 * Gets the socket instance
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}
	
	/**
	 * Closes the socket
	 */
	public synchronized void close() throws IOException {
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
	
	/**
	 * gets the socket's outputStream
	 * @return the socket's outputStream
	 * @throws IOException 
	 */
	public DataOutputStream getOutput() throws IOException {
		if(outToServer == null)
			outToServer = new DataOutputStream(socket.getOutputStream());
		return outToServer;
	}
	
	/**
	 * gets the socket's inputStream
	 * @return the socket's inputStream
	 * @throws IOException 
	 */
	public DataInputStream getInput() throws IOException {
		if(inFromServer == null)
			inFromServer = new DataInputStream(socket.getInputStream());
		return inFromServer;
	}
}
