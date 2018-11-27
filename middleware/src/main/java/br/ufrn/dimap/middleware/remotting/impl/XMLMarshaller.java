package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import br.ufrn.dimap.middleware.remotting.interfaces.Marshaller;

/**
 * Marshaller that uses XML as marshaling format, making use
 * of JAXB (Java Architecture for XML Binding).
 * 
 * The class of the "root" object to be marshaled should be
 * annotated with XmlRootElement. See javax.xml.bind
 * documentation for details.
 * 
 * But, in the case when XmlRootElement is missing, XMLMarshaller
 * will wrap the object in a JAXBElement before marshaling,
 * although resulting in an undescriptive element name ("root")
 * for the object in the generated XML document.
 * 
 * Note that, by default, only public getter/setter pairs and
 * public fields are bound to XML in JAXB. This can be changed
 * by the XMLAccessorType annotation. 
 * 
 * Note also that JAXB requires a default constructor 
 * (public constructor with no arguments) in all marshaled
 * objects for unmarshalling.
 * 
 * @author carlosemv
 */
public class XMLMarshaller implements Marshaller {
	
	@Override
	public <T> ByteArrayOutputStream marshal(T object) throws MarshalException {
		return this.marshal(object, null);
	}

	@Override
	public <T> ByteArrayOutputStream marshal(T object, Set<Class<?>> context) throws MarshalException {
		
		Object marshalObject = null;
		Class<T> objClass = null;
		try {
			objClass = (Class<T>) object.getClass();
		} catch (NullPointerException e) {
			IllegalArgumentException illegal = new IllegalArgumentException(
					"Object to be marshalled cannot be null", e);
			throw new MarshalException(illegal, this, object, context);
		}
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		
		try {
		
			if (object.getClass().getAnnotation(XmlRootElement.class) == null) {
				QName qName = new QName("root");
				JAXBElement<T> objElement = new JAXBElement<T>(qName, objClass, object);
				
				marshalObject = (Object) objElement;
			} else {
				marshalObject = (Object) object;
			}

			JAXBContext jc;
			if (context != null && !context.isEmpty()) {
				context.add(objClass);
				jc = JAXBContext.newInstance(context.toArray(new Class[0]));
			} else {
				jc = JAXBContext.newInstance(objClass);
			}
			javax.xml.bind.Marshaller marshaller = jc.createMarshaller();
			
			marshaller.marshal(marshalObject, byteStream);
		} catch (Exception e) {
			throw new MarshalException(e, this, object, context);
		}
		
		return byteStream;
	}
	
	@Override
	public <T> T unmarshal(ByteArrayInputStream byteStream, Class<T> tgtClass) throws UnmarshalException {
		return this.unmarshal(byteStream, tgtClass, null);
	}

	@Override
	public <T> T unmarshal(ByteArrayInputStream byteStream, Class<T> tgtClass, Set<Class<?>> context) throws UnmarshalException {
		T result = null;
		
		try {
			JAXBContext jc;
			if (context != null && !context.isEmpty()) {
				context.add(tgtClass);
				jc = JAXBContext.newInstance(context.toArray(new Class[0]));
			} else {
				jc = JAXBContext.newInstance(tgtClass);
			}
			javax.xml.bind.Marshaller marshaller = jc.createMarshaller();
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			if (tgtClass.getAnnotation(XmlRootElement.class) == null) {
				JAXBElement<T> root = unmarshaller.unmarshal(new StreamSource(byteStream), tgtClass);
				result = root.getValue();
			} else {
				result = (T) unmarshaller.unmarshal(byteStream);
			}
		} catch (Exception e) {
			throw new UnmarshalException(e, this, byteStream, tgtClass, context);
		}
		
		return result;
	}

}
