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
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;
import br.ufrn.dimap.middleware.remotting.interfaces.NamingInstaller;
import br.ufrn.dimap.middleware.remotting.impl.DeploymentDescriptor;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
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
    
	//private static final String NAMING_SERVER_HOST = "localhost";
	private static final String NAMING_SERVER_HOST = "35.230.92.188";

	private static final int NAMING_SERVER_PORT = 8000;

	private static final String MIDDLEWARE_FOLDER = "middleware";

	private Logger logger = Logger.getLogger(ClientInstaller.class.getName());

	private DefaultLookup() {}
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
	 *  Called by clients who need to know the location of a remote object.
	 *
	 * @see br.ufrn.dimap.middleware.identification.lookup.Lookup#find(String)
	 */
	public AbsoluteObjectReference find(String name) throws IOException, ClassNotFoundException {

        Socket socket = new Socket(NAMING_SERVER_HOST, NAMING_SERVER_PORT);
        
        AbsoluteObjectReference aor = null;

		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        Object data[] = new Object[2];

        data[0] = "find";
        data[1] = name;

		oos.writeObject(data);
		oos.flush();

        logger.log(Level.INFO, "Find requested");

        try {
            while (true) {
                logger.log(Level.INFO, "Waiting server response...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                aor = (AbsoluteObjectReference) ois.readObject();
            }
        } catch (EOFException e) {}

		logger.log(Level.INFO, "AOR received!");

		socket.close();

		return aor;
	}

	/**
	 * Called by the middleware when it needs to load the classes needed
	 * to instantiate the object.
	 *
	 * @return impl installed class
	 * @param objId
	 * @throws RemoteError
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public Class<? extends Invoker> findAndLocallyInstall(ObjectId objId) throws RemoteError, IOException, ClassNotFoundException {

        Socket socket = new Socket(NAMING_SERVER_HOST, NAMING_SERVER_PORT);

		Object data[] = new Object[2];
		data[0] = "findClasses";
		data[1] = objId;

		logger.log(Level.INFO, "Waiting for output stream...");

		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

		logger.log(Level.INFO, "Making request...");

		oos.writeObject(data);
		oos.flush();

		logger.log(Level.INFO, "Waiting for naming server response...");

		Object[] files = null;

		try {
			//while(true){				
				logger.log(Level.INFO, "Waiting for input stream...");
				
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				files = (Object[]) ois.readObject();
			//}
		} catch(EOFException e) {}

		byte[] interfFile = (byte[]) files[1];
		byte[] invokerFile = (byte[]) files[3];
		byte[] implFile = (byte[]) files[5];

		String interfName = (String) files[0];
		String invokerName = (String) files[2];
		String implName = (String) files[4];

		String filesURL = InstallationConfig.getTargetDir() + "generated/middleware/";

		logger.log(Level.INFO, "Storing interface file...");

		// Create dir if not exists
        new File(filesURL).mkdirs();

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
		dynamicClassLoader.loadClassFromFile(getClassname(interfName), getClassFileLocation(InstallationConfig.getTargetDir(), MIDDLEWARE_FOLDER, interfName));
		dynamicClassLoader.loadClassFromFile(getClassname(invokerName), getClassFileLocation(InstallationConfig.getTargetDir(), MIDDLEWARE_FOLDER, invokerName));
		Class<? extends Invoker> implClass =  dynamicClassLoader.loadClassFromFile(getClassname(implName), getClassFileLocation(InstallationConfig.getTargetDir(), MIDDLEWARE_FOLDER, implName));

		logger.log(Level.INFO, "Implementation saved in the middleware");

		socket.close();
		
		return implClass;
	}

	/**
	 * Install in the naming service the generated files.
	 *
	 */
	@Override
	public void install(DeploymentDescriptor deploymentDescriptor, NetAddr middlewareAddress) throws IOException, RemoteError {
		if(deploymentDescriptor != null) {
			if(deploymentDescriptor.getInterfaceFile() != null && deploymentDescriptor.getInvokerFile() != null && deploymentDescriptor.getInvokerImplementation() != null){

                Socket socket = new Socket(NAMING_SERVER_HOST, NAMING_SERVER_PORT);

				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

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

				oos.writeObject(data);
				oos.flush();
			}
		}
	}

}