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
}
