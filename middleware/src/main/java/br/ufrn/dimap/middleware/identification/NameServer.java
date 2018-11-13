package br.ufrn.dimap.middleware.identification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public class NameServer {

	private Map<String, AbsoluteObjectReference> nameMap;
	private Map<ObjectId, RemoteObject> remoteMap;
	private int port;
	
	protected NameServer() {
		this.nameMap = new ConcurrentHashMap <String, AbsoluteObjectReference>();
		this.remoteMap = new ConcurrentHashMap <ObjectId, RemoteObject>();
	}
	
	private void startServer() throws IOException, RemoteError {
		
		ServerSocket server = new ServerSocket(port);
		
		while(true) {
			Socket client = server.accept();
			BufferedReader msg = new BufferedReader(new InputStreamReader(client.getInputStream()));
			Object[] data = msg.toString().trim().split(";");
			
			if (data[0].equals("bind")) {
				bind((String)data[1], (RemoteObject) data[2], (String) data[3], (Integer) data[4]);
			}else if (data[0].equals("find")) {
				find((String) data[1]);
			}else if (data[0].equals("findById")) {
				findById((ObjectId) data[1]);
			}
		}
			
	}
	
	public void bind(String name, Object remoteObject, String host, int port) throws RemoteError {
		
		ObjectId objectId = new ObjectId();
		AbsoluteObjectReference aor = new AbsoluteObjectReference(objectId, host, port);
		
		if (nameMap.containsKey(name) || remoteMap.containsKey(objectId)) {
			throw new RemoteError("Error on lookup binding! There already exists an absolute object reference for this name property.");
		}
		
		remoteMap.put(objectId, (RemoteObject) remoteObject);
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
