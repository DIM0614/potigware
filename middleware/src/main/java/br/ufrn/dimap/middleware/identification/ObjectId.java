
package br.ufrn.dimap.middleware.identification;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Defines a valid (globally unique) identifier for each remote object instance, 
 * which is a 64-bit number value.
 * 
 * @author Yuri Alessandro Martins 
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectId implements Serializable {
	
	private UUID objectId;
	
	/**
	 * Construct new ObjectId
	 */
	public ObjectId() {
		this.objectId = UUID.randomUUID();
	}
	
	/**
	 * Construct new ObjectId from given String.
	 * @param seed	String seed for new ObjectId.

	 */
	public ObjectId(String seed) {
		this.objectId = UUID.fromString(seed);
	}

	/**
	 * Return the least significant 64 bits of this UUID's 128 bit value.
	 * @return Least significant 64 bits.
	 */
	public long getObjectId() {
		return this.objectId.getLeastSignificantBits();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ObjectId objectId1 = (ObjectId) o;
		return Objects.equals(objectId, objectId1.objectId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(objectId);
	}
}
