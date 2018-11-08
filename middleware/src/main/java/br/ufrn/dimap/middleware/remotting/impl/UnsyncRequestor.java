package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.interfaces.Callback;
import br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler;
import br.ufrn.dimap.middleware.remotting.interfaces.InvocationAsynchronyPattern;
import br.ufrn.dimap.middleware.remotting.interfaces.Marshaller;

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

	public void request(AbsoluteObjectReference aor, String operationName, InvocationAsynchronyPattern invocationAsyncPattern, Object... parameters) throws RemoteError, IOException {

		ByteArrayOutputStream outputStream = prepareInvocation(aor, operationName, parameters);

		this.clientRequestHandler.send(aor.getHost(), aor.getPort(), outputStream, invocationAsyncPattern);
	}

	private ByteArrayOutputStream prepareInvocation(AbsoluteObjectReference aor, String operationName, Object... parameters) throws IOException {
		InvocationData invocationData = new InvocationData(aor, operationName, parameters);

		Invocation invocation = new Invocation(invocationData);

		ByteArrayOutputStream outputStream = this.marshaller.marshal(invocation);

		return outputStream;
	}

}
