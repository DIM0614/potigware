package identification;

/**
 * This class describes a remote absolute reference that uniquely identifies a 
 * remote object, holding information such as its id, INVOKATOR identifier, and 
 * network information: host and port.
 * 
 * @author Yuri Alessandro Martins
 * @version 1.0
 * @see ObjectId
 */
public class AbsoluteObjectReference {
	private ObjectId objectId;
	private String host;
	private String port;
	private int invokerId;

	public AbsoluteObjectReference(ObjectId objectId, String host, String port, 
			int invokerId) {
		this.objectId = objectId;
		this.host = host;
		this.port = port;
		this.invokerId = invokerId;
	}

	public ObjectId getObjectId() {
		return objectId;
	}

	public String getHost() {
		return host;
	}

	public String getPort() {
		return port;
	}

	public int getInvokerId() {
		return invokerId;
	}
	
}
