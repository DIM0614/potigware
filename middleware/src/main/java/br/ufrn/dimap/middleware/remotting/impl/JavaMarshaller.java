package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import br.ufrn.dimap.middleware.remotting.interfaces.MarshallerI;

/**
 * Marshaller that simply serializes (marshal)
 * and deserializes (unmarshal) java objects.
 * 
 * @author carlosv
 */
public class JavaMarshaller implements MarshallerI {
	/**
	 * Uses ObjectOutputStream to serialize an Object into a byte stream.
	 * 
	 * @param object an Object that must implement the Java.io.Serializable interface
	 */
	public ByteArrayOutputStream marshal(Object object) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
		objectStream.writeObject(object);
		
		return byteStream;
	}

	/**
	 * Uses ObjectInputStream to deserialize a byte stream into an Object
	 * 
	 * @param byteStream a byte stream that represents a serialized instance of a known class 
	 */
	public Object unmarshal(ByteArrayInputStream byteStream) throws IOException, ClassNotFoundException {
		ObjectInputStream objectStream = new ObjectInputStream(byteStream);
		Object obj = objectStream.readObject();
		return obj;
	}
}
