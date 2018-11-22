package br.ufrn.dimap.middleware.remotting.impl;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents an invocation, including invocation data (aor, method,
 * parameters) and invocation context.
 *
 * @author vitorgreati
 */
@XmlRootElement(name = "Invocation")
public class Invocation {

    private InvocationData invocationData;

    public Invocation() {
		super();
	}

	private Map<String, Object> context;

    public Invocation(InvocationData invocationData) {
        this.invocationData = invocationData;
        this.context = new HashMap<String, Object>();
    }

    public Invocation(InvocationData invocationData, Map<String, Object> context) {
        this.invocationData = invocationData;
        this.context = context;
    }

    public InvocationData getInvocationData() {
        return invocationData;
    }

    public void setInvocationData(InvocationData invocationData) {
        this.invocationData = invocationData;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
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
