package br.ufrn.dimap.middleware.identification;

import java.io.IOException;
import java.util.logging.Logger;

import br.ufrn.dimap.middleware.installer.ClientInstaller;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public class MainServer {

	private static Logger logger = Logger.getLogger(MainServer.class.getName());

	public static Logger getLogger() {
		return logger;
	}

	public static void main (String [] args) {
		NameServer server = new NameServer(8000);
		try {
			server.startServer();
			server.receiveMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
