
package br.ufrn.dimap.middleware.identification;

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
public class ObjectId {
	
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objectId == null) ? 0 : objectId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectId other = (ObjectId) obj;
		if (objectId == null) {
			if (other.objectId != null)
				return false;
		} else if (!objectId.equals(other.objectId))
			return false;
		return true;
	}
}
