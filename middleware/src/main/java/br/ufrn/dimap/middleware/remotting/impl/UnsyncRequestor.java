package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.interfaces.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Unsynchronized requestor to be used for
 * requestors instantiated per request.
 * When one needs one requestor for all requests,
 * a synchronized version must be used.
 *
 * @author vitorgreati
 */
public class UnsyncRequestor implements br.ufrn.dimap.middleware.remotting.interfaces.Requestor {

    private Marshaller marshaller;

    private ClientRequestHandler clientRequestHandler;

    private Logger logger = Logger.getLogger(UnsyncRequestor.class.getName());

    public UnsyncRequestor() {
    	this.marshaller = new XMLMarshaller();
    	this.clientRequestHandler = ClientRequestHandlerImpl.getInstance();
    }

    public UnsyncRequestor(Marshaller marshaller) {
    	this.marshaller = marshaller;
		this.clientRequestHandler = ClientRequestHandlerImpl.getInstance();
    }

	@Override
	public Object request(AbsoluteObjectReference aor, String operationName, Class<?> returnType, Object... parameters) throws RemoteError {

        ByteArrayOutputStream outputStream = null;
        try {

            Invocation invocation = makeInvocation(aor, operationName, parameters);
            outputStream = marshallInvocation(invocation);

            clientRequestHandler.getQosObserver().started(invocation, outputStream.size());

            ByteArrayInputStream inputStream = this.clientRequestHandler.send(aor.getHost(), aor.getPort(), outputStream);

            clientRequestHandler.getQosObserver().done(invocation);

            Object returnValue = this.marshaller.unmarshal(inputStream, returnType);
            
            if(returnValue instanceof VoidObject)
				returnValue = null;
            
            return returnValue;

        } catch (IOException | ClassNotFoundException e) {
            throw new RemoteError(e);
        }
    }

    @Override
	public void request(AbsoluteObjectReference aor, String operationName, Callback callback, Class<?> returnType, Object... parameters) throws RemoteError {

        ByteArrayOutputStream outputStream = null;

        try {

            Invocation invocation = makeInvocation(aor, operationName, parameters);
            outputStream = marshallInvocation(invocation);

            clientRequestHandler.getQosObserver().started(invocation, outputStream.size());

            this.clientRequestHandler.send(aor.getHost(), aor.getPort(), outputStream, callback);

            clientRequestHandler.getQosObserver().done(invocation);

        } catch (IOException e) {
            throw new RemoteError(e);
        }

    }

    @Override
	public Object request(AbsoluteObjectReference aor, String operationName,
                          InvocationAsynchronyPattern invocationAsyncPattern,
                          Class<?> returnType, Object... parameters) throws RemoteError {

        ByteArrayOutputStream outputStream = null;
        try {
            Invocation invocation = makeInvocation(aor, operationName, parameters);
            outputStream = marshallInvocation(invocation);

            clientRequestHandler.getQosObserver().started(invocation, outputStream.size());

            switch (invocationAsyncPattern) {
                case FIRE_AND_FORGET:
                    this.clientRequestHandler.send(aor.getHost(), aor.getPort(), outputStream, false);
                    return null;
                case SYNC_WITH_SERVER:
                    this.clientRequestHandler.send(aor.getHost(), aor.getPort(), outputStream, true);
                    return null;
                case POLL_OBJECT:
                    PollObject pollObject = new DefaultPollObject();
                    this.clientRequestHandler.send(aor.getHost(), aor.getPort(), outputStream, pollObject);
                    return pollObject;
            }

            clientRequestHandler.getQosObserver().done(invocation);
        } catch (IOException e) {
            throw new RemoteError(e);
        }
		throw new RemoteError("Failed to performe the request");
	}

	private Invocation makeInvocation(AbsoluteObjectReference aor, String operationName, Object... parameters) {
        InvocationData invocationData = new InvocationData(aor, operationName, parameters);

        Invocation invocation = new Invocation(invocationData);

        return invocation;
    }

    private ByteArrayOutputStream marshallInvocation(Invocation invocation) throws IOException {
    	Set<Class<?>> context = new HashSet<Class<?>>();
    	for (Object p : invocation.getInvocationData().getActualParams()) {
    		context.add(p.getClass());
    	}

        ByteArrayOutputStream outputStream = this.marshaller.marshal(invocation, context);

        return outputStream;
    }

}
