package br.ufrn.dimap.middleware.remotting.impl;

import java.util.Arrays;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;

import java.io.Serializable;

/**
 * Comprises all necessary data to perform an invocation.
 *
 * @author vitorgreati
 */
public class InvocationData implements Serializable {

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

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(actualParams);
		result = prime * result + ((aor == null) ? 0 : aor.hashCode());
		result = prime * result + ((operationName == null) ? 0 : operationName.hashCode());
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
		InvocationData other = (InvocationData) obj;
		if (!Arrays.equals(actualParams, other.actualParams))
			return false;
		if (aor == null) {
			if (other.aor != null)
				return false;
		} else if (!aor.equals(other.aor))
			return false;
		if (operationName == null) {
			if (other.operationName != null)
				return false;
		} else if (!operationName.equals(other.operationName))
			return false;
		return true;
	}

}
