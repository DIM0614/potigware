package br.ufrn.dimap.middleware.extension.impl;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import br.ufrn.dimap.middleware.extension.interfaces.InvocationInterceptorSerialized;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public class GenerateLogSerializedInterceptor implements InvocationInterceptorSerialized {

	private Logger logger;
	FileHandler fh;
	
	/*
	 * @param path_file Path to the file in which to log information in
	 * @param append Append option 
	 */
	public GenerateLogSerializedInterceptor (String path_file, Boolean append) {
		try {
			fh = new FileHandler(path_file, append);
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] intercept(byte[] inputStream, InvocationContext invocationContext) throws RemoteError {
		String msg;
		
		msg = "Intercepting message of size " + inputStream.length;

		logger.info(msg);
		
		return inputStream;
	}
	

}
