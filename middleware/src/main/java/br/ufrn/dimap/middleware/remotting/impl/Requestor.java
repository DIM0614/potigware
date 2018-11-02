package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;

public class Requestor<T> implements br.ufrn.dimap.middleware.remotting.interfaces.Requestor<T> {

    private String marshaller;

    public Requestor() {
        this.marshaller = null; /* TODO*/
    }

    public Requestor(String marshaller) { /* TODO */

        this.marshaller = marshaller; /* TODO */

    }

	public T request(AbsoluteObjectReference aor, String operationName, Object... parameters) throws RemoteError {

		InvocationData invocationData = new InvocationData(aor, operationName, parameters);
		Invocation invocation = new Invocation(invocationData);

		/* TODO */
		// Marshaller
		// Client Request Handler

		return null;

	}

}
