package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.util.Set;

import br.ufrn.dimap.middleware.remotting.interfaces.Marshaller;

public class UnmarshalException extends MarshallerException {

	private static final long serialVersionUID = -4216094159095698075L;
	ByteArrayInputStream inputStream;
	Class<?> targetClass;
	Set<Class<?>> context;
	
	public UnmarshalException() {
	}

	public UnmarshalException(ByteArrayInputStream inputStream, Class<?> targetClass, Set<Class<?>> context) {
		super();
		this.inputStream = inputStream;
		this.targetClass = targetClass;
		this.context = context;
	}
	
	public UnmarshalException(ByteArrayInputStream inputStream, Class<?> targetClass) {
		super();
		this.inputStream = inputStream;
		this.targetClass = targetClass;
		this.context = null;
	}

	public UnmarshalException(Marshaller marshaller) {
		super(marshaller);
	}

	public UnmarshalException(String message) {
		super(message);
	}

	public UnmarshalException(String message, Marshaller marshaller) {
		super(message, marshaller);
	}

	public UnmarshalException(Throwable cause) {
		super(cause);
	}

	public UnmarshalException(Throwable cause, Marshaller marshaller) {
		super(cause, marshaller);
	}
	
	public UnmarshalException(Throwable cause, Marshaller marshaller, ByteArrayInputStream inputStream, Class<?> targetClass, Set<Class<?>> context) {
		super(cause, marshaller);
		this.inputStream = inputStream;
		this.targetClass = targetClass;
		this.context = context;
	}

	public UnmarshalException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnmarshalException(String message, Throwable cause, Marshaller marshaller) {
		super(message, cause, marshaller);
	}

	public UnmarshalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public Set<Class<?>> getContext() {
		return context;
	}

	public void setContext(Set<Class<?>> context) {
		this.context = context;
	}

}
