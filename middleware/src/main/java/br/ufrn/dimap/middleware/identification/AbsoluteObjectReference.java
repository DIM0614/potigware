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
}
