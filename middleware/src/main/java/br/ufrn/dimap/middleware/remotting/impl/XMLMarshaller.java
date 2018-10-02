package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import br.ufrn.dimap.middleware.remotting.interfaces.MarshallerI;

/**
 * Marshaller that uses XML as marshalling format.
 */
public class XMLMarshaller implements MarshallerI {

	public ByteArrayOutputStream marshal(Object object) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object unmarshal(ByteArrayInputStream byteStream) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}