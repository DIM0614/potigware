package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import br.ufrn.dimap.middleware.remotting.interfaces.Marshaller;

/**
 * Marshaller that simply serializes (marshal)
 * and deserializes (unmarshal) java objects.
 * 
 * All objects to be marshalled must implement
 * the java.io.Serializable interface.
 * 
 * @author carlosemv
 */
public class JavaMarshaller implements Marshaller {
	/**
	 * Uses ObjectOutputStream to serialize an Object into a byte stream.
	 * 
	 * @param object an Object that must implement the Java.io.Serializable interface
	 */
	@Override
	public <T> ByteArrayOutputStream marshal(T object) throws MarshalException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

		try {
			ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
			objectStream.writeObject(object);
		} catch (Exception e) {
			MarshalException me = new MarshalException(e, this);
			me.setTargetObject(object);
			throw me;
		}
		
		return byteStream;
	}
	
	@Override
	public <T> ByteArrayOutputStream marshal(T object, Set<Class<?>> context) throws MarshalException {
		return this.marshal(object);
	}

	/**
	 * Uses ObjectInputStream to deserialize a byte stream into an Object
	 * 
	 * @param byteStream a byte stream that represents a serialized instance of a known class
	 * @param tgtClass is not used, result is cast to Object
	 */
	@Override
	public <T> T unmarshal(ByteArrayInputStream byteStream, Class<T> tgtClass) throws UnmarshalException {
		T obj = null;
		try {
			ObjectInputStream objectStream = new ObjectInputStream(byteStream);
			obj = (T) objectStream.readObject();
		} catch (Exception e) {
			UnmarshalException ue = new UnmarshalException(e, this);
			ue.setInputStream(byteStream);
			ue.setTargetClass(tgtClass);
			throw ue;
		}
		return obj;
	}
	
	@Override
	public <T> T unmarshal(ByteArrayInputStream byteStream, Class<T> tgtClass, Set<Class<?>> context) throws UnmarshalException {
		return this.unmarshal(byteStream, tgtClass);
	}
}
