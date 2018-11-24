package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.installer.ClientInstaller;
import br.ufrn.dimap.middleware.remotting.interfaces.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.Remote;
import java.util.logging.Level;
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
    	this.marshaller = new JavaMarshaller();
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

            outputStream = prepareInvocation(aor, operationName, parameters);

            ByteArrayInputStream inputStream = this.clientRequestHandler.send(aor.getHost(), aor.getPort(), outputStream);

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

            outputStream = prepareInvocation(aor, operationName, parameters);

            this.clientRequestHandler.send(aor.getHost(), aor.getPort(), outputStream, callback);

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
            outputStream = prepareInvocation(aor, operationName, parameters);
        } catch (IOException e) {
            throw new RemoteError(e);
        }

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
