package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;

/**
 * Comprises all necessary data to perform an invocation.
 *
 * @author vitorgreati
 */
public class InvocationData {

    private AbsoluteObjectReference aor;

    private String operationName;

    private Object[] actualParams;

    public InvocationData() {
		super();
	}

	public InvocationData(AbsoluteObjectReference aor, String operationName, Object ... actualParams) {
        this.aor = aor;
        this.operationName = operationName;
        this.actualParams = actualParams;
    }

    public AbsoluteObjectReference getAor() {
        return aor;
    }

    public void setAor(AbsoluteObjectReference aor) {
        this.aor = aor;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Object[] getActualParams() {
        return actualParams;
    }

    public void setActualParams(Object[] actualParams) {
        this.actualParams = actualParams;
    }

}
