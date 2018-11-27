package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.remotting.interfaces.Marshaller;

public class MarshallerException extends Exception {
	
	private static final long serialVersionUID = 5120069428010972119L;
	Marshaller marshaller;

	public MarshallerException() {}
	
	public MarshallerException(Marshaller marshaller) {
		this.marshaller = marshaller;
	}
	
	public MarshallerException(String message) {
		super(message);
	}
	
	public MarshallerException(String message, Marshaller marshaller) {
		super(message);
		this.marshaller = marshaller;
	}
	
	public MarshallerException(Throwable cause) {
		super(cause);
	}
	
	public MarshallerException(Throwable cause, Marshaller marshaller) {
		super(cause);
		this.marshaller = marshaller;
	}

	public MarshallerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public MarshallerException(String message, Throwable cause, Marshaller marshaller) {
		super(message, cause);
		this.marshaller = marshaller;
	}

	public MarshallerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public Marshaller getMarshaller() {
		return marshaller;
	}

	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}
}
