package br.ufrn.dimap.middleware.extension.impl;

import java.util.HashMap;

/**
 * Store all the contextual information that needs to be pass in every remote
 * invocation.
 * 
 * @author Pedro Arthur Medeiros
 *
 */
public class InvocationContext {

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
	public boolean equals(Object obj) {
		return this.context.equals(obj);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		return result;
	}
}
