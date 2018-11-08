package br.ufrn.dimap.middleware.identification.lookup;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Implements a Lookup Service as part of the distributed object middleware. 
 * This service provides an way to bind some name to an remote object absolute 
 * reference, and other way to find this absolute reference given an name.
 * 
 * @author Yuri Alessandro Martins 
 * @version 1.0
 * @see AbsoluteObjectReference
 * @see RemoteError
 */
public interface Lookup {
	/**
	 * Lets server applications register an reference to remote objects and 
	 * associate them to a name.
	 * 
	 * @param name	Propertied that'll serve as identifier to absolute object 
	 * 				reference.
	 * @param aor	The absolute object reference that must be binded with the 
	 * 				given name (propertied).
	 * @throws RemoteError
	 */
	public void bind(String name, Object remoteObject) throws RemoteError;
	
	/**
	 * Allow clients to use lookup service to query for the ABSOLUTE OBJECT 
	 * REFERENCES of remote objects based the name.
	 * 
	 * @param name	Name that'll be used to find the absolute object reference, 
	 * 				previously binded with this same name.
	 * @return		The absolute object reference binded with the given name.
	 * @throws RemoteError
	 */
	public AbsoluteObjectReference find(String name) throws RemoteError;
	
	/**
	 * Allow the server requestor find the remote object reference given an 
	 * object id.
	 * 
	 * @param objectId Unique identifier of remote object.
	 * @return Correspondent of this object id.
	 * @throws RemoteError
	 */
	public Object findById(ObjectId objectId) throws RemoteError;
}
