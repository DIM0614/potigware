package br.ufrn.dimap.middleware.identification;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.ufrn.dimap.middleware.installer.InstallationConfig;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public class NameServer {

	/**
	 * The lookup data structure used to register absolute object references along with their name properties.
	 */
	private final Map<String, AbsoluteObjectReference> nameMap;
	
	/**
	 * The lookup data structure used to register object IDs along with their referred objects.
	 */
	
	private final int port;
	private ServerSocket server;

	private final Logger logger = NameServerMain.getLogger();

	private final ThreadPoolExecutor executor;
	

	/**
	 * Maps object names to file names.
	 *
	 */
	private final Map<ObjectId, InstalledFilesInfo> filesMap;

	public NameServer(int port) {
		this(port, 1000);
	}
	
	public NameServer(int port, int numThreads) {
		this.port = port;
		this.nameMap = new ConcurrentHashMap<>();
		this.filesMap = new ConcurrentHashMap<>();
		this.executor = new ThreadPoolExecutor(numThreads, numThreads, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	public void startServer() throws IOException {
		
		server = new ServerSocket(port);
	
	}
	
	public void receiveMessages() throws IOException {
		
		while(!Thread.interrupted() && !server.isClosed()) {
			logger.log(Level.INFO, "Server waiting...");
			try {
				Socket client = server.accept();
				logger.log(Level.INFO, "Client accepted...");
				
				executor.submit(() -> processClient(client));

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
			
	}

	private void processClient(Socket client) {
		
		ObjectInputStream msg = null;
		Object[] data = null;
		ObjectOutputStream output = null;
		
		try {
			msg = new ObjectInputStream(client.getInputStream());
	
			data = (Object[]) msg.readObject();
	
			//logger.log(Level.INFO, "Data read...");
	
			String opName = (String) data[0];
	
			//logger.log(Level.INFO, "Request " + opName + " arrived");
	
			if (opName.equals("find")) {
				output = new ObjectOutputStream(client.getOutputStream());
				output.writeObject(find((String) data[1]));
				output.flush();
				output.close();
			} else if (opName.equals("install")) {
	
				logger.log(Level.INFO, "Installing remotes...");
	
				String filesURL = InstallationConfig.getTargetDir() + "generated/naming/";
	
				// Create dir if not exists
				new File(filesURL).mkdirs();
	
				String interfaceName = (String) data[1];
				String invokerName = (String) data[3];
				String invokerImplName = (String) data[5];
	
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
	
				logger.log(Level.INFO, "Classes installed, generating AOR now...");
	
				String host = (String) data[8];
				Integer port = (Integer) data[9];
	
				AbsoluteObjectReference aor = bind(objName, host, port);
	
				filesMap.put(aor.getObjectId(), installedInfo);
				
				logger.log(Level.INFO, "AOR generated...");
	
			} else if (opName.equals("findClasses")) {
	
				String filesURL = InstallationConfig.getTargetDir() + "generated/naming/";
	
				InstalledFilesInfo filesInfo = filesMap.get((ObjectId) data[1]);
	
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
				
				output = new ObjectOutputStream(client.getOutputStream());
				output.writeObject(files);
				output.flush();
	
			}
		} catch(Exception e) {
			logger.log(Level.INFO, e.getMessage());
			
		} finally{
			try {
				msg.close();
			} catch(Exception e1) { }
			
			try {
				output.close();
			} catch(Exception e1) { }
			
			
			try {
				client.close();
			} catch(Exception e1) { }
		}
	}

	public AbsoluteObjectReference bind(String name, String host, int port) throws RemoteError {

		ObjectId objectId = new ObjectId();
		AbsoluteObjectReference aor = new AbsoluteObjectReference(objectId, host, port);

		//if (nameMap.containsKey(name) || remoteMap.containsKey(objectId)) {
		//	throw new RemoteError("Error on lookup binding! There already exists an absolute object reference for this name property.");
		//}

		if (nameMap.containsKey(name)) {
			throw new RemoteError("Error on lookup binding! There already exists an absolute object reference for this name property.");
		}

		//remoteMap.put(objectId, remoteObject);
		nameMap.put(name, aor);

		return aor;
	}

	private AbsoluteObjectReference find(String name) throws RemoteError {
		if (!nameMap.containsKey(name)) {
			throw new RemoteError("Error on lookup finding! No absolute object reference was registered with this name property.");
		}
		
		return nameMap.get(name);
	}

	private static class InstalledFilesInfo {
		private final String interfName;
		private final String implName;
		private final String invokerName;

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
