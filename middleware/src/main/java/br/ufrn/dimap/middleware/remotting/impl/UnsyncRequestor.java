package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.interfaces.ClientRequestHandler;
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

    public UnsyncRequestor() {
        this.marshaller = new JavaMarshaller();
    }

    public UnsyncRequestor(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

	public Object request(AbsoluteObjectReference aor, String operationName, Object... parameters) throws RemoteError, IOException, ClassNotFoundException {

		InvocationData invocationData = new InvocationData(aor, operationName, parameters);

		Invocation invocation = new Invocation(invocationData);

		ByteArrayOutputStream outputStream = this.marshaller.marshal(invocation);

		ByteArrayInputStream inputStream = ClientRequestHandlerImpl.getInstance().send(aor.getHost(), aor.getPort(), outputStream);

		Object returnValue = this.marshaller.unmarshal(inputStream, Object.class);

		return returnValue;
	}

}
