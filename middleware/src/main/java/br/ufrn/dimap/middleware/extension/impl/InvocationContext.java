package br.ufrn.dimap.middleware.extension.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

/**
 * Store all the contextual information that needs to be pass in every remote
 * invocation.
 * 
 * @author Pedro Arthur Medeiros
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class InvocationContext implements Serializable {

	private HashMap<String, Object> context;

	public InvocationContext() {
		this.context = new HashMap<String, Object>();
	}

	public void add(String key, Object value) {
		this.context.put(key, value);
	}

	public Object get(String key) {
		return this.context.get(key);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InvocationContext that = (InvocationContext) o;
		return Objects.equals(context, that.context);
	}

	@Override
	public int hashCode() {
		return Objects.hash(context);
	}
}
