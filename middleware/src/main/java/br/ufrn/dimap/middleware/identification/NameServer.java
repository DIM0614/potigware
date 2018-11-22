package br.ufrn.dimap.middleware.identification;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.ufrn.dimap.middleware.installer.ClientInstaller;
import br.ufrn.dimap.middleware.installer.InstallationConfig;
import br.ufrn.dimap.middleware.remotting.impl.DeploymentDescriptor;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public class NameServer {
	
	/**
	 * The lookup data structure used to register absolute object references along with their name properties.
	 */
	private Map<String, AbsoluteObjectReference> nameMap;
	
	/**
	 * The lookup data structure used to register object IDs along with their referred objects.
	 */
	private Map<ObjectId, Object> remoteMap;
	private int port;
	ServerSocket server;

	private Logger logger = MainServer.getLogger();


	/**
	 * Maps object names to file names.
	 *
	 */
	private Map<String, InstalledFilesInfo> filesMap;

	
	protected NameServer(int port) {
		this.port = port;
		this.nameMap = new ConcurrentHashMap<>();
		this.remoteMap = new ConcurrentHashMap<>();
		this.filesMap = new ConcurrentHashMap<>();
	}
	
	public void startServer() throws IOException {
		
		server = new ServerSocket(port);
	
	}
	
	public synchronized void receiveMessage() throws IOException {
		
		while(true) {
			logger.log(Level.INFO, "Server waiting...");
			try {
				Socket client = server.accept();
				logger.log(Level.INFO, "Client accepted...");
				ObjectInputStream msg = new ObjectInputStream(client.getInputStream());
				logger.log(Level.INFO, "Msg created...");
				Object[] data = null;
				try {
					while(true) {
						data = (Object[]) msg.readObject();
					}
				} catch(EOFException eof){}
				logger.log(Level.INFO, "Data read...");

				ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());

				String opName = (String) data[0];

				logger.log(Level.INFO, "Request " + opName + " arrived");

				System.out.println(opName);

				if (opName.equals("bind")) {
					bind((String)data[1], data[2], (String) data[3], (Integer) data[4]);
				}else if (opName.equals("find")) {
					output.writeObject(find((String) data[1]));
					output.flush();
					output.close();
				}else if (opName.equals("findById")) {
					output.writeObject(findById((ObjectId) data[1]));
					output.flush();
					output.close();
				}else if(opName.equals("install")) {

					logger.log(Level.INFO, "Installing remotes...");

					String filesURL = InstallationConfig.getTargetDir();

					String interfaceName = (String) data[1];
					String invokerName = (String) data[3];
					String invokerImplName = (String) data[5];

					// Reference obtained in stackoverlfow: https://stackoverflow.com/questions/4350084/byte-to-file-in-java
					try (FileOutputStream fos = new FileOutputStream(filesURL + interfaceName + ".class")) {
						fos.write((byte[]) data[2]);

					}

					try (FileOutputStream fos = new FileOutputStream(filesURL + invokerName + ".class")) {
						fos.write((byte[]) data[4]);
					}

					try (FileOutputStream fos = new FileOutputStream(filesURL + invokerImplName + ".class")) {
						fos.write((byte[]) data[6]);
					}

					String objName = (String) data[7];

					InstalledFilesInfo installedInfo = new InstalledFilesInfo(interfaceName, invokerName, invokerImplName);
					filesMap.put(objName, installedInfo);

					logger.log(Level.INFO, "Classes installed");


				} else if (opName.equals("findClasses")) {

					String filesURL = InstallationConfig.getTargetDir();

					InstalledFilesInfo filesInfo = filesMap.get((String) data[1]);

					Object[] files = new Object[6];

					byte[] interfFile = Files.readAllBytes(new File(filesURL + filesInfo.getInterfName() + ".class").toPath());
					byte[] invokerFile = Files.readAllBytes(new File(filesURL + filesInfo.getInvokerName() + ".class").toPath());
					byte[] implFile = Files.readAllBytes(new File(filesURL + filesInfo.getImplName() + ".class").toPath());

					files[0] = filesInfo.getInterfName();
					files[1] = interfFile;
					files[2] = filesInfo.getInvokerName();
					files[3] = invokerFile;
					files[4] = filesInfo.getImplName();
					files[5] = implFile;

					DataOutputStream outToServer = new DataOutputStream(client.getOutputStream());

					((ObjectOutput) outToServer).writeObject(files);
					outToServer.flush();

				}
			} catch (ClassNotFoundException | IOException | RemoteError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
			
	}
	
	public void bind (String name, Object remoteObject, String host, int port) throws RemoteError {
		
		ObjectId objectId = new ObjectId();
		AbsoluteObjectReference aor = new AbsoluteObjectReference(objectId, host, port);
		
		if (nameMap.containsKey(name) || remoteMap.containsKey(objectId)) {
			throw new RemoteError("Error on lookup binding! There already exists an absolute object reference for this name property.");
		}
		
		remoteMap.put(objectId, remoteObject);
		nameMap.put(name, aor);
		
		
	}

	public AbsoluteObjectReference find(String name) throws RemoteError {
		if (!nameMap.containsKey(name)) {
			throw new RemoteError("Error on lookup finding! No absolute object reference was registered with this name property.");
		}
		
		return nameMap.get(name);
	}

	public Object findById(ObjectId ObjectId) throws RemoteError {
		if (!remoteMap.containsKey(ObjectId)) {
			throw new RemoteError("Error on lookup finding! No absolute object reference was registered with this name property.");
		}
		
		return remoteMap.get(ObjectId);
	}



	public static class InstalledFilesInfo {
		final String interfName;
		final String implName;
		final String invokerName;

		public InstalledFilesInfo(final String interfName, final String invokerName, final String implName) {
			this.interfName = interfName;
			this.implName = implName;
			this.invokerName = invokerName;
		}

		public String getInterfName() {
			return interfName;
		}

		public String getImplName() {
			return implName;
		}

		public String getInvokerName() {
			return invokerName;
		}


	}
	
}
