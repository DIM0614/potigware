package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import br.ufrn.dimap.middleware.remotting.interfaces.Marshaller;

/**
 * Marshaller that uses XML as marshalling format.
 */
public class XMLMarshaller implements Marshaller {

	public ByteArrayOutputStream marshal(Object object) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		
		try {
			JAXBContext jc = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = jc.createMarshaller();
			marshaller.marshal(object, byteStream);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return byteStream;
	}

	public <T> Object unmarshal(ByteArrayInputStream byteStream, Class<T> tgtClass) throws IOException, ClassNotFoundException {
		Object result = null;
		
		try {
			JAXBContext jc = JAXBContext.newInstance(tgtClass);
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			result = unmarshaller.unmarshal(byteStream);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
