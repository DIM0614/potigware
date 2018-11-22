package br.ufrn.dimap.middleware.identification;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
	
	protected NameServer(int port) {
		this.port = port;
		this.nameMap = new ConcurrentHashMap <String, AbsoluteObjectReference>();
		this.remoteMap = new ConcurrentHashMap <ObjectId, Object>();
	}
	
	public void startServer() throws IOException, RemoteError {
		
		server = new ServerSocket(port);
	
	}
	
	public synchronized void receiveMessage() throws IOException {
		
		while(true) {
			Socket client = server.accept();
			ObjectInputStream msg = new ObjectInputStream(client.getInputStream());
			Object[] data;
			try {
				data = (Object[]) msg.readObject();
				ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
				if (data[0].equals("bind")) {
					bind((String)data[1], data[2], (String) data[3], (Integer) data[4]);
				}else if (data[0].equals("find")) {
					output.writeObject(find((String) data[1]));
					output.flush();
					output.close();
				}else if (data[0].equals("findById")) {
					output.writeObject(findById((ObjectId) data[1]));
					output.flush();
					output.close();
				}else if(data[0].equals("install")) {
					File directory = new File("./");
					String filesURL = directory.getCanonicalPath() + "/src/main/java/br/ufrn/dimap/middleware/remotting/files/";
					String interfaceName = (String) data[1];
					String invokerName = (String) data[3];
					String invokerImplName = (String) data[5];

					// Reference obtained in stackoverlfow: https://stackoverflow.com/questions/4350084/byte-to-file-in-java
					try (FileOutputStream fos = new FileOutputStream(filesURL + interfaceName)) {
						fos.write((byte[]) data[2]);
					}

					try (FileOutputStream fos = new FileOutputStream(filesURL + invokerName)) {
						fos.write((byte[]) data[4]);
					}

					try (FileOutputStream fos = new FileOutputStream(filesURL + invokerImplName)) {
						fos.write((byte[]) data[6]);
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} catch (RemoteError e) {
				// TODO Auto-generated catch block
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
	
}
