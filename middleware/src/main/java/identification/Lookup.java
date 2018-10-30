package identification;

/**
 * Implements a Lookup Service as part of the distributed object middleware. 
 * This service provides an way to bind some name to an remote object absolute 
 * reference, and other way to find this absolute reference given an name.
 * 
 * @author Yuri Alessandro Martins 
 * @version 1.0
 * @see AbsoluteObjectReference
 */
public interface Lookup {
	// TODO both methods must throws RemoteException (pending of implementation)
	/**
	 * Lets server applications register an reference to remote objects and 
	 * associate them to a name.
	 * 
	 * @param name	Propertied that'll serve as identifier to absolute object 
	 * 				reference.
	 * @param aor	The absolute object reference that must be binded with the 
	 * 				given name (propertied).
	 */
	public void bind(String name, AbsoluteObjectReference aor);
	
	/**
	 * Allow clients to use lookup service to query for the ABSOLUTE OBJECT 
	 * REFERENCES of remote objects based the name.
	 * 
	 * @param name	Name that'll be used to find the absolute object reference, 
	 * 				previously binded with this same name.
	 * @return		The absolute object reference binded with the given name.
	 */
	public AbsoluteObjectReference find(String name);
}
