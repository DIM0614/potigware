package br.ufrn.dimap.middleware.identification.lookup;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.NetAddr;
import br.ufrn.dimap.middleware.identification.ObjectId;
import br.ufrn.dimap.middleware.installer.ClientInstaller;
import br.ufrn.dimap.middleware.installer.InstallationConfig;
import br.ufrn.dimap.middleware.remotting.interfaces.NamingInstaller;
import br.ufrn.dimap.middleware.remotting.impl.DeploymentDescriptor;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.utils.IOUtils;
import br.ufrn.dimap.middleware.utils.StringUtils;
import br.ufrn.dimap.middleware.utils.classloader.DynamicClassLoader;

import static br.ufrn.dimap.middleware.installer.InstallationConfig.getClassFileLocation;
import static br.ufrn.dimap.middleware.installer.InstallationConfig.getClassname;

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
	private ObjectOutputStream outToServer;

	private static final String HOST = "localhost";
	private static final int PORT = 8000;

	private Logger logger = Logger.getLogger(ClientInstaller.class.getName());

	private DefaultLookup() throws RemoteError {
		init(HOST, PORT);
	}
	/**
	 * Wraps the instance
	 */
	private static Wrapper<Lookup> instanceWrapper;

	/**
	 * Returns single instance of the lookup class, creating instance if needed
	 * 
	 * @author victoragnez
	 */
  
	public static Lookup getInstance() throws RemoteError {
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
	
	public void init(String host, int port) throws RemoteError {
		try {
			this.socket = new Socket(host, port);
			this.outToServer = new ObjectOutputStream(socket.getOutputStream());

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
	 *  Called by clients who need to know the location of a remote object.
	 *
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#find(String)
	 */
	public AbsoluteObjectReference find(String name) throws RemoteError, UnknownHostException, IOException, ClassNotFoundException {
		Object[] data = new Object[2];
		data[0] = "find";
		data[1] = name;
		outToServer.writeObject(data);
		outToServer.flush();
		return (AbsoluteObjectReference) ((ObjectInput) new ObjectInputStream(socket.getInputStream())).readObject();
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
		return ((ObjectInput) new ObjectInputStream(socket.getInputStream())).readObject();
	}

	/**
	 * Called by the middleware when it needs to load the classes needed
	 * to instantiate the object.
	 *
	 * @param objId
	 * @throws RemoteError
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public void findAndLocallyInstall(ObjectId objId) throws RemoteError, IOException, ClassNotFoundException {

		Object data[] = new Object[10];
		data[0] = "findClasses";
		data[1] = objId;
		((ObjectOutput) outToServer).writeObject(data);
		outToServer.flush();

		logger.log(Level.INFO, "Waiting for naming server response...");

		Object[] files = (Object[]) ((ObjectInput) new ObjectInputStream(socket.getInputStream())).readObject();

		byte[] interfFile = (byte[]) files[1];
		byte[] invokerFile = (byte[]) files[3];
		byte[] implFile = (byte[]) files[5];

		String interfName = (String) files[0];
		String invokerName = (String) files[2];
		String implName = (String) files[4];

		String filesURL = InstallationConfig.getTargetDir();

		logger.log(Level.INFO, "Storing interface file...");

		try (FileOutputStream fos = new FileOutputStream(filesURL + interfName + ".class")) {
			fos.write(interfFile);
		}

		logger.log(Level.INFO, "Storing invoker file...");
		try (FileOutputStream fos = new FileOutputStream(filesURL + invokerName + ".class")) {
			fos.write(invokerFile);
		}

		logger.log(Level.INFO, "Storing implementation file...");
		try (FileOutputStream fos = new FileOutputStream(filesURL + implName + ".class")) {
			fos.write(implFile);
		}

		logger.log(Level.INFO, "Dynamically loading files in the middleware...");
		DynamicClassLoader dynamicClassLoader = DynamicClassLoader.getDynamicClassLoader();
		dynamicClassLoader.loadClassFromFile(getClassname(interfName), getClassFileLocation(filesURL, interfName));
		dynamicClassLoader.loadClassFromFile(getClassname(invokerName), getClassFileLocation(filesURL, invokerName));
		dynamicClassLoader.loadClassFromFile(getClassname(implName), getClassFileLocation(filesURL, implName));

		logger.log(Level.INFO, "Implementation saved in the middleware");
	}

	/**
	 * Install in the naming service the generated files.
	 *
	 */
	@Override
	public void install(DeploymentDescriptor deploymentDescriptor, NetAddr middlewareAddress) throws IOException {
		if(deploymentDescriptor != null) {
			if(deploymentDescriptor.getInterfaceFile() != null && deploymentDescriptor.getInvokerFile() != null && deploymentDescriptor.getInvokerImplementation() != null){

				Object data[] = new Object[10];
				data[0] = "install";
				data[1] = StringUtils.getFileName(deploymentDescriptor.getInterfaceFile().getPath());
				data[2] = Files.readAllBytes(deploymentDescriptor.getInterfaceFile().toPath());
				data[3] = StringUtils.getFileName(deploymentDescriptor.getInvokerFile().getPath());
				data[4] = Files.readAllBytes(deploymentDescriptor.getInvokerFile().toPath());
				data[5] = StringUtils.getFileName(deploymentDescriptor.getInvokerImplementation().getPath());
				data[6] = Files.readAllBytes(deploymentDescriptor.getInvokerImplementation().toPath());
				data[7] = deploymentDescriptor.getRemoteObjectName();
				data[8] = middlewareAddress.getHost();
				data[9] = middlewareAddress.getPort();

				outToServer.writeObject(data);
				outToServer.flush();
			}
		}
	}

}