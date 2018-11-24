package br.ufrn.dimap.middleware.identification.lookup;

import java.io.IOException;
import java.net.UnknownHostException;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * Implements a Lookup Service as part of the distributed object middleware. 
 * This service provides an way to bind some name to an remote object absolute 
 * reference, and other way to find this absolute reference given an name.
 * @see RemoteError
 */
public interface Lookup {

  
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
	 * Retrieve previously installed classes from the naming service,
	 * and install them locally.
	 *
	 * //@param objName
	 * @throws RemoteError
	 */
	Class<? extends Invoker> findAndLocallyInstall(ObjectId objId) throws RemoteError, IOException, ClassNotFoundException;
}
