
package identification;

import java.util.UUID;

/**
 * Defines a valid (globally unique) identifier for each remote object instance, 
 * which is a 64-bit number value.
 * 
 * @author Yuri Alessandro Martins 
 * @version 1.0
 */
public class ObjectId {
	
	public UUID objectId;
	
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
            return false;
        }
		
		if (!ObjectId.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		
		final ObjectId param = (ObjectId) obj;
		if(this.objectId.equals(param)) {
			return true;
		}else {
			return false;
		}
	}
}
