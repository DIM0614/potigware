package br.ufrn.dimap.middleware.identification;

import java.io.IOException;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public class MainServer {

	public static void main (String [] args) {
		NameServer server = new NameServer(8000);
		try {
			server.startServer();
			server.receiveMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
