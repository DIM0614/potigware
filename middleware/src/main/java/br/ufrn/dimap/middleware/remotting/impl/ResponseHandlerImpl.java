package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.impl.LifecycleManagerImpl;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.LifecycleManager;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;
import br.ufrn.dimap.middleware.remotting.interfaces.Marshaller;
import br.ufrn.dimap.middleware.remotting.interfaces.ResponseHandler;

/**
 * Handles requests from clients.
 * 
 * @author victoragnez
 *
 */
public class ResponseHandlerImpl implements ResponseHandler {
	
	private final Marshaller marshaller = null; //TODO instantiate marshaller
	private final LifecycleManager lifecycleManager = new LifecycleManagerImpl();
	
	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ResponseHandler#handleResponse(java.io.InputStream)
	 */
	@Override
	public byte[] handleResponse(byte[] msg, boolean respond) throws RemoteError {
		Invocation invocation;
		InvocationData invocationData;
		AbsoluteObjectReference aor;
		Invoker invoker;
		
		try {
			invocation = marshaller.unmarshal(new ByteArrayInputStream(msg), Invocation.class);
			invocationData = invocation.getInvocationData();
			aor = invocationData.getAor();
			invoker = lifecycleManager.getInvoker(aor);
		} catch(Exception e) {
			throw new RemoteError(e);
		}
		
		byte[] ret;
		
		try {
			Object returnedData = invoker.invoke(invocation);
			lifecycleManager.invocationDone(aor, invoker);
			if(respond)
				ret = marshaller.marshal(returnedData).toByteArray();
			else
				ret = null;
		} catch(Exception e) {
			throw new RemoteError(e);
		} finally {
			lifecycleManager.invocationDone(aor, invoker);
		}
		
		return ret;
	}
}
