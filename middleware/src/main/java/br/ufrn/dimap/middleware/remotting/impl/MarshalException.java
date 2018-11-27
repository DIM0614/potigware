package br.ufrn.dimap.middleware.remotting.impl;

import java.util.Set;

import br.ufrn.dimap.middleware.remotting.interfaces.Marshaller;

public class MarshalException extends MarshallerException {

	private static final long serialVersionUID = 654762382908964768L;
	Object targetObject;
	Set<Class<?>> context;
	
	public MarshalException() {
	}

	public MarshalException(Object targetObject, Set<Class<?>> context) {
		super();
		this.targetObject = targetObject;
		this.context = context;
	}
	
	public MarshalException(Object targetObject) {
		this.targetObject = targetObject;
		this.context = null;
	}

	public MarshalException(Marshaller marshaller) {
		super(marshaller);
	}

	public MarshalException(String message) {
		super(message);
	}

	public MarshalException(String message, Marshaller marshaller) {
		super(message, marshaller);
	}

	public MarshalException(Throwable cause) {
		super(cause);
	}

	public MarshalException(Throwable cause, Marshaller marshaller) {
		super(cause, marshaller);
	}
	
	public MarshalException(Throwable cause, Marshaller marshaller, Object targetObject, Set<Class<?>> context) {
		super(cause, marshaller);
		this.targetObject = targetObject;
		this.context = context;
	}

	public MarshalException(String message, Throwable cause) {
		super(message, cause);
	}

	public MarshalException(String message, Throwable cause, Marshaller marshaller) {
		super(message, cause, marshaller);
	}

	public MarshalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

	public Set<Class<?>> getContext() {
		return context;
	}

	public void setContext(Set<Class<?>> context) {
		this.context = context;
	}

}
