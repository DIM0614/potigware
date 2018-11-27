package br.ufrn.dimap.middleware.remotting.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufrn.dimap.middleware.extension.impl.InvocationContext;
import org.junit.Test;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.ObjectId;
import junit.framework.TestCase;

public class XMLMarshallerTest extends TestCase {
	XMLMarshaller marshaller;
	
	public XMLMarshallerTest() {
		this.marshaller = new XMLMarshaller();
	}
	
	@Test
	public void testInvocationMarshaling() throws ClassNotFoundException, IOException {
		ObjectId id = new ObjectId("55ebd0d1-befa-4faa-8392-8723ccc9ddff");
		AbsoluteObjectReference aor = new AbsoluteObjectReference(id, "localhost", 1234);
		InvocationData data = new InvocationData(aor, "op1");
		data.setActualParams(new Object[] {new Integer[] {3,2,1}});
		InvocationContext ic = new InvocationContext();
		ic.add("lala", 10);
		ic.add("oook", -3);
		
		Set<Class<?>> context = new HashSet<Class<?>>();
		for (Object p : data.getActualParams()) {
			context.add(p.getClass());
		}
		Invocation inv0 = new Invocation(data, ic);
		Invocation inv1 = marshalUnmarshal(inv0, context);
		
		assertEquals(inv0, inv1);
	}
	
	@Test
	public void testPrimitivesMarshaling() throws ClassNotFoundException, IOException {
		boolean b = false;
		char c = '@';
		int i = -33;
		double d = Double.MIN_VALUE;
		
		Object[] primitives = {b, c, i, d};
		for (Object p0 : primitives) {
			Object p1 = marshalUnmarshal(p0);
			assertEquals(p0, p1);
		}
 	}
	
	@Test
	public void testVoidObjectMarshaling() throws ClassNotFoundException, IOException {
		VoidObject v0 = new VoidObject();
		VoidObject v1 = marshalUnmarshal(v0);
	}
	
	@Test
	public void testNullMarshaling() throws ClassNotFoundException, IOException {
		Object n0 = null;
		Object n1 = marshalUnmarshal(n0);
		assertEquals(n0, n1);
 	}
	
	@Test
	public void testArrayMarshaling() throws ClassNotFoundException, IOException {
		int[] a0 = {-1, 0, Integer.MAX_VALUE, Integer.MIN_VALUE};
		int[] a1 = marshalUnmarshal(a0);
		assertTrue(Arrays.equals(a0, a1));
	}
	
	public <T> T marshalUnmarshal(T object, Set<Class<?>> context) throws IOException, ClassNotFoundException {
		if (object == null)
			return null;
		
		ByteArrayOutputStream baos;
		Class<T> objClass = (Class<T>) object.getClass();
				
		if (context == null)
			baos = marshaller.marshal(object);
		else
			baos = marshaller.marshal(object, context);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(
				baos.toByteArray());
		
		if (context == null)
			return marshaller.unmarshal(bais, objClass);
		return marshaller.unmarshal(bais, objClass, context);
	}
	
	public <T> T marshalUnmarshal(T object) throws IOException, ClassNotFoundException {
		return marshalUnmarshal(object, null);
	}

}
