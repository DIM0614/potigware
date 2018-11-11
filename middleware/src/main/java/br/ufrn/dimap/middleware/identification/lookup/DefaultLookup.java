package br.ufrn.dimap.middleware.identification.lookup;

import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.Map;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.NameServer;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**
 * The Lookup class allows clients to bind name properties 
 * to absolute object references (AORs) and retrieve AORs
 * later with the respective name properties.
 * The singleton pattern is used to implement this class.
 * 
 * @author ireneginani
 */
public class DefaultLookup implements Lookup {
	
	private NameServer nameServer;
	
	/**
	 * The lookup data structure used to register absolute object references along with their name properties.
	 */
	
	public void bind(String name, Object remoteObject, String host, String port) throws RemoteError {
		nameServer.bind(name, remoteObject, host, port);
		
	}

	public AbsoluteObjectReference find(String name) throws RemoteError {
		return nameServer.find(name);
	}

	public AbsoluteObjectReference findById(ObjectId ObjectId) throws RemoteError {
		return nameServer.findById(ObjectId);
	}
	

}