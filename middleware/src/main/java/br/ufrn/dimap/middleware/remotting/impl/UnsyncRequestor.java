package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.interfaces.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    public UnsyncRequestor() {
    	this.marshaller = new JavaMarshaller();
    	this.clientRequestHandler = ClientRequestHandlerImpl.getInstance();
    }

    public UnsyncRequestor(Marshaller marshaller) {
    	this.marshaller = marshaller;
		this.clientRequestHandler = ClientRequestHandlerImpl.getInstance();
	}

	public Object request(AbsoluteObjectReference aor, String operationName, Object... parameters) throws RemoteError, IOException, ClassNotFoundException {

		ByteArrayOutputStream outputStream = prepareInvocation(aor, operationName, parameters);

		ByteArrayInputStream inputStream = this.clientRequestHandler.send(aor.getHost(), aor.getPort(), outputStream);

		Object returnValue = this.marshaller.unmarshal(inputStream, Object.class);

		return returnValue;
	}

	public void request(AbsoluteObjectReference aor, String operationName, Callback callback, Object... parameters) throws RemoteError, IOException {

    	ByteArrayOutputStream outputStream = prepareInvocation(aor, operationName, parameters);

		this.clientRequestHandler.send(aor.getHost(), aor.getPort(), outputStream, callback);
    }

	public Object request(AbsoluteObjectReference aor, String operationName, InvocationAsynchronyPattern invocationAsyncPattern, Object... parameters) throws RemoteError, IOException {

		ByteArrayOutputStream outputStream = prepareInvocation(aor, operationName, parameters);

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

		throw new RemoteError("Failed to performe the request");
	}

	private ByteArrayOutputStream prepareInvocation(AbsoluteObjectReference aor, String operationName, Object... parameters) throws IOException {
		InvocationData invocationData = new InvocationData(aor, operationName, parameters);

		Invocation invocation = new Invocation(invocationData);

		ByteArrayOutputStream outputStream = this.marshaller.marshal(invocation);

		return outputStream;
	}

}
