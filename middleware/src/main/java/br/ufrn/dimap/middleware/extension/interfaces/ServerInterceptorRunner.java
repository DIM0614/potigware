package br.ufrn.dimap.middleware.extension.interfaces;

import br.ufrn.dimap.middleware.extension.impl.InvocationContext;
import br.ufrn.dimap.middleware.remotting.impl.InvocationData;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Implements methods for intercepting incoming data
 * 
 * @author Gustavo Carvalho
 *
 */
public interface ServerInterceptorRunner {
	
	/**
	 * Runs the registered request interceptors on the message
	 * @param msg the original message
	 * @param context the invocation context
	 * @return the modified message
	 * @throws RemoteError
	 */
	public byte[] runRequestInterceptors(byte[] msg, InvocationContext context) throws RemoteError;

	/**
	 * Runs the registered invocation interceptors on the invocationData
	 * @param invocationData the invocationData
	 * @param context the invocation context
	 * @throws RemoteError
	 */
	public void runInvocationInterceptors(InvocationData invocationData, InvocationContext context) throws RemoteError;
}
