package br.ufrn.dimap.middleware.extension.impl;

import java.util.HashMap;

/**
 * Store all the contextual information that need to be pass in every remote
 * invocation.
 * 
 * @author pedroarthur-mf
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

}
