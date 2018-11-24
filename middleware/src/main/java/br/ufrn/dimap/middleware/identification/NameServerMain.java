package br.ufrn.dimap.middleware.identification;

import java.io.IOException;
import java.util.logging.Logger;

public class NameServerMain {

	private static Logger logger = Logger.getLogger(NameServerMain.class.getName());

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
