package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.impl.LifecycleManagerImpl;
import br.ufrn.dimap.middleware.infrastructure.lifecycleManager.interfaces.LifecycleManager;
import br.ufrn.dimap.middleware.infrastructure.qos.BasicRemotingPatterns;
import br.ufrn.dimap.middleware.infrastructure.qos.QoSObserver;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;
import br.ufrn.dimap.middleware.remotting.interfaces.Marshaller;
import br.ufrn.dimap.middleware.MiddlewareConfig;
import br.ufrn.dimap.middleware.extension.impl.InvocationContext;
import br.ufrn.dimap.middleware.extension.interfaces.InvocationInterceptorSerialized;
import br.ufrn.dimap.middleware.extension.interfaces.InvocationInterceptorUnserialized;
import br.ufrn.dimap.middleware.extension.interfaces.ResponseHandler;
import br.ufrn.dimap.middleware.extension.interfaces.ServerInterceptorRunner;

/**
 * Handles requests from clients.
 * 
 * @author victoragnez
 *
 */
public class ResponseHandlerImpl implements ResponseHandler, ServerInterceptorRunner {
	
	private final Marshaller marshaller = new JavaMarshaller(); 
	private final LifecycleManager lifecycleManager;
	private final QoSObserver qosObserver;
	
	public ResponseHandlerImpl() throws RemoteError {
		lifecycleManager = new LifecycleManagerImpl();
		qosObserver = new QoSObserver(BasicRemotingPatterns.ServerRequestHandler);
	}
	
	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ResponseHandler#handleResponse(java.io.InputStream)
	 */
	@Override
	public byte[] handleResponse(byte[] msg) throws RemoteError {
		Invocation invocation;
		InvocationData invocationData;
		AbsoluteObjectReference aor;
		Invoker invoker;
		
		try {
			// Running request interceptor with fake invocation data due to marshalization limitations
			msg = runRequestInterceptors(msg, new InvocationContext());
			
			invocation = marshaller.unmarshal(new ByteArrayInputStream(msg), Invocation.class);
			
			// Running invocation interceptor
			runInvocationInterceptors(invocation.getInvocationData(), invocation.getContext());
			
			invocationData = invocation.getInvocationData();
			aor = invocationData.getAor();
			
			qosObserver.started(invocation, msg.length);

			invoker = lifecycleManager.getInvoker(aor);
		} catch(Exception e) {
			throw new RemoteError(e);
		}
		
		byte[] ret;
		
		try {
			Object returnedData = invoker.invoke(invocation);
			if(returnedData == null)
				returnedData = new VoidObject();
			lifecycleManager.invocationDone(aor, invoker);
			qosObserver.done(invocation);
			ret = marshaller.marshal(returnedData).toByteArray();
		} catch(Exception e) {
			throw new RemoteError(e);
		} finally {
			lifecycleManager.invocationDone(aor, invoker);
		}
		
		return ret;
	}

	@Override
	public byte[] runRequestInterceptors(byte[] msg, InvocationContext context)
			throws RemoteError {
		for (InvocationInterceptorSerialized iis : MiddlewareConfig.Interceptors.getInstance().getServerRequestInteceptors()) {
			msg = iis.intercept(msg, context);
		}
		return msg;
	}

	@Override
	public void runInvocationInterceptors(InvocationData invocationData, InvocationContext context) throws RemoteError {
		for (InvocationInterceptorUnserialized iiu : MiddlewareConfig.Interceptors.getInstance().getServerInvocationInteceptors()) {
			iiu.intercept(invocationData, context);
		}
	}
}
