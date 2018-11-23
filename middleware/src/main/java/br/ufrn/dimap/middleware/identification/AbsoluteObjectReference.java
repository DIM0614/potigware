package br.ufrn.dimap.middleware.identification;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

/**
 * This class describes a remote absolute reference that uniquely identifies a 
 * remote object, holding information such as its id, INVOKATOR identifier, and 
 * network information: host and port.
 * 
 * @author Yuri Alessandro Martins
 * @version 1.0
 * @see ObjectId
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AbsoluteObjectReference implements Serializable {
	
	private ObjectId objectId;
	private String host;
	private int port;
	
	public AbsoluteObjectReference() {
		super();
	}
	
	/**
	 * Creates the unique identifier for remote objects: AOR
	 * @param objectId	The remote object's object id
	 * @param host		Host of the network
	 * @param port		Port of the network
	 */
	public AbsoluteObjectReference(ObjectId objectId, String host, int port) {
		this.objectId = objectId;
		this.host = host;
		this.port = port;
	}
	
	/**
	 * 
	 * @return AOR ObjectId
	 */
	public ObjectId getObjectId() {
		return objectId;
	}
	
	/**
	 * 
	 * @return AOR network host
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * 
	 * @return AOR network port
	 */
	public int getPort() {
		return port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((objectId == null) ? 0 : objectId.hashCode());
		result = prime * result + port;
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
		AbsoluteObjectReference other = (AbsoluteObjectReference) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (objectId == null) {
			if (other.objectId != null)
				return false;
		} else if (!objectId.equals(other.objectId))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
}
