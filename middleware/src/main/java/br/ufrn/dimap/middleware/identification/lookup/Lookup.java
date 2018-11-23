package br.ufrn.dimap.middleware.identification.lookup;

import java.io.IOException;
import java.net.UnknownHostException;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * Implements a Lookup Service as part of the distributed object middleware. 
 * This service provides an way to bind some name to an remote object absolute 
 * reference, and other way to find this absolute reference given an name.
 * @see RemoteError
 */
public interface Lookup {
  
	/**
	 * Lets server applications register a reference to remote objects and 
	 * associate them to a name.
	 * 
	 * @param name	Propertied that'll serve as identifier to absolute object 
	 * 				reference.
	 * @param remoteObject	The absolute object reference that must be binded with the 
	 * 				given name (propertied).
	 * @param host	Server connection host.
	 * @param port	Server connection port.
	 * @throws RemoteError 
   * @throws IOException 
	 */
	void bind(String name, Object remoteObject, String host, int port) throws RemoteError, IOException;
  
	/**
	 * Allow clients to use lookup service to query for the ABSOLUTE OBJECT 
	 * REFERENCES of remote objects based the name.
	 * 
	 * @param name	Name that'll be used to find the absolute object reference, 
	 * 				previously binded with this same name.
	 * @return		The absolute object reference binded with the given name.
   * @throws RemoteError
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws ClassNotFoundException 
	 */
	AbsoluteObjectReference find(String name) throws RemoteError, UnknownHostException, IOException, ClassNotFoundException;
	
	/**
	 * Allow the server requestor find the remote object reference given an 
	 * object id.
	 * 
	 * @param ObjectId	ObjectId that'll be used to find the absolute object reference, 
	 * 				previously binded with this same ObjectId.
	 * @return		The absolute object reference binded with the given the ObjectId.
   * @throws RemoteError
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws ClassNotFoundException 
	 */
	Object findById(ObjectId ObjectId) throws RemoteError, UnknownHostException, IOException, ClassNotFoundException;

	/**
	 * Retrieve previously installed classes from the naming service,
	 * and install them locally.
	 *
	 * @param objName
	 * @throws RemoteError
	 */
	void findAndLocallyInstall(ObjectId objId) throws RemoteError, IOException, ClassNotFoundException;
}
