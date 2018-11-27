package br.ufrn.dimap.middleware.remotting.impl;

import java.io.Serializable;
import java.util.UUID;

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

	private static final long serialVersionUID = -1602846527685641748L;
	
	private final UUID invocationId;
	
	private InvocationData invocationData;

    public Invocation() {
    	super();
    	this.invocationId = UUID.randomUUID();
	}

	private InvocationContext context;

    public Invocation(InvocationData invocationData) {
        this.invocationData = invocationData;
        this.context = new InvocationContext();
        this.invocationId = UUID.randomUUID();
    }

    public Invocation(InvocationData invocationData, InvocationContext context) {
        this.invocationData = invocationData;
        this.context = context;
        this.invocationId = UUID.randomUUID();
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
		result = prime * result + ((invocationId == null) ? 0 : invocationId.hashCode()); 
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
		if (invocationId == null) {
			if (other.invocationId != null)
				return false;
		} else if (!invocationId.equals(other.invocationId))
			return false;
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
