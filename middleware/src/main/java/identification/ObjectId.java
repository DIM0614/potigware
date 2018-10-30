
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
}
