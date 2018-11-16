package br.ufrn.dimap.middleware.identification;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	private Map<ObjectId, Class<? extends Invoker>> remoteMap;
	private int port;
	ServerSocket server;
	
	protected NameServer() {
		this.nameMap = new ConcurrentHashMap <String, AbsoluteObjectReference>();
		this.remoteMap = new ConcurrentHashMap <ObjectId, Class<? extends Invoker>>();
	}
	
	private void startServer() throws IOException, RemoteError {
		
		server = new ServerSocket(port);
	
	}
	
	private synchronized void receiveMessage() throws IOException {
		
		while(true) {
			Socket client = server.accept();
			ObjectInputStream msg = new ObjectInputStream(client.getInputStream());
			Object[] data;
			try {
				data = (Object[]) msg.readObject();
				ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
				if (data[0].equals("bind")) {
					bind((String)data[1], (Class<? extends Invoker>) data[2], (String) data[3], (Integer) data[4]);
				}else if (data[0].equals("find")) {
					output.writeObject(find((String) data[1]));
					output.flush();
					output.close();
				}else if (data[0].equals("findById")) {
					output.writeObject(findById((ObjectId) data[1]));
					output.flush();
					output.close();
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
	
	public void bind (String name, Class<? extends Invoker> remoteObject, String host, int port) throws RemoteError {
		
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
