package br.ufrn.dimap.middleware.remotting.impl;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufrn.dimap.middleware.extension.impl.InvocationContext;

/**
 * Represents an invocation, including invocation data (aor, method,
 * parameters) and invocation context.
 *
 * @author vitorgreati
 */
@XmlRootElement(name = "Invocation")
public class Invocation implements Serializable {

    private InvocationData invocationData;

    public Invocation() {
		super();
	}

	private InvocationContext context;

    public Invocation(InvocationData invocationData) {
        this.invocationData = invocationData;
        this.context = new InvocationContext();
    }

    public Invocation(InvocationData invocationData, InvocationContext context) {
        this.invocationData = invocationData;
        this.context = context;
    }

    public InvocationData getInvocationData() {
        return invocationData;
    }

    public void setInvocationData(InvocationData invocationData) {
        this.invocationData = invocationData;
    }

    public InvocationContext getContext() {
        return context;
    }

    public void setContext(InvocationContext context) {
        this.context = context;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((invocationData == null) ? 0 : invocationData.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Invocation other = (Invocation) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (invocationData == null) {
			if (other.invocationData != null)
				return false;
		} else if (!invocationData.equals(other.invocationData))
			return false;
		return true;
	}
}
