package br.ufrn.dimap.middleware.identification.lookup;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.remotting.interfaces.NamingInstaller;
import br.ufrn.dimap.middleware.remotting.impl.DeploymentDescriptor;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.utils.IOUtils;

/**

 * The DefaultLookup class allows clients to bind name properties 
 * to implicitly constructed absolute object references (AORs), 
 * made up of a object instances along with host and port information 
 * provided by the user.
 *  
 * Users might use this class later to retrieve AORs
 * by their respective name properties, and get object instances 
 * according to their respective object IDs as well.
 * The singleton pattern is used to implement this class.
 * 
 * @author ireneginani
 * @author thiagolucena
 * @author vinihcampos
 */
public class DefaultLookup implements Lookup, NamingInstaller {
	
	private Socket socket;
	private DataOutputStream outToServer;
	private DataInputStream inFromServer;
  
	/**
	 * Wraps the instance
	 */
	private static Wrapper<Lookup> instanceWrapper;

	/**
	 * Returns single instance of the lookup class, creating instance if needed
	 * 
	 * @author victoragnez
	 */
  
	public static Lookup getInstance() {
		Wrapper<Lookup> w = instanceWrapper;
        if (w == null) { // check 1
        	synchronized (DefaultLookup.class) {
        		w = instanceWrapper;
        		if (w == null) { // check 2
        			w = new Wrapper<Lookup>(new DefaultLookup());
        			instanceWrapper = w;
        		}
        	}
        }
        
        return w.getInstance();
	}

    /**
	 * 
	 * Wraps the instance to allow final modifier
	 * 
	 * @author victoragnez
	 * 
	 * @param <T> the type to be wrapped
	 */
	private static class Wrapper<T> {
		private final T instance;
	    public Wrapper(T service) {
	        this.instance = service;
	    }
	    public T getInstance() {
	        return instance;
	    }
	}
	
	
	/**
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#bind(String, Object, String, int)
	 */
	
	public void Connection(String host, int port) throws RemoteError {
		try {
			this.socket = new Socket(host, port);
			this.outToServer = new DataOutputStream(socket.getOutputStream());
			this.inFromServer = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			throw new RemoteError(e);
		}
	}
	
	
	public void bind (String name, Object remoteObject, String host, int port) throws RemoteError, IOException {
		Object data [] = new Object[5];
		data[0] = "bind";
		data[1] = name;
		data[2] = remoteObject;
		data[3] = host;
		data[4] = port;
		((ObjectOutput) outToServer).writeObject(data);
		outToServer.flush();
		
	}
  
  /**
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#find(String)
	 */
	public AbsoluteObjectReference find(String name) throws RemoteError, UnknownHostException, IOException, ClassNotFoundException {
		String data  = "find " + name;
		outToServer.writeChars(data);
		outToServer.flush();
		return (AbsoluteObjectReference) ((ObjectInput) inFromServer).readObject();
	}
  
   /**
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#findById(ObjectId)
	 */	
	public Object findById(ObjectId ObjectId) throws RemoteError, UnknownHostException, IOException, ClassNotFoundException {
		Object data [] = new Object[2];
		data[0] = "findById";
		data[1] = ObjectId;
		((ObjectOutput) outToServer).writeObject(data);
		outToServer.flush();
		return ((ObjectInput) inFromServer).readObject();
	}

	/**
	 *
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.NamingInstaller#install(DeploymentDescriptor)
	 */
	@Override
	public void install(DeploymentDescriptor deploymentDescriptor) throws IOException {

		if(deploymentDescriptor != null) {
			if(deploymentDescriptor.getInterfaceFile() != null && deploymentDescriptor.getInvokerFile() != null && deploymentDescriptor.getInvokerImplementation() != null){

				Object data[] = new Object[7];
				data[0] = "install";
				data[1] = "interfaceFile";
				data[2] = Files.readAllBytes(deploymentDescriptor.getInterfaceFile().toPath());
				data[3] = "invokerFile";
				data[4] = Files.readAllBytes(deploymentDescriptor.getInvokerFile().toPath());
				data[5] = "invokerImplementation";
				data[6] = Files.readAllBytes(deploymentDescriptor.getInvokerImplementation().toPath());

				((ObjectOutput) outToServer).writeObject(data);
				outToServer.flush();
			}
		}
	}

}