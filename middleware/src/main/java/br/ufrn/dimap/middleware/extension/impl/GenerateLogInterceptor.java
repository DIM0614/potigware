package br.ufrn.dimap.middleware.extension.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import br.ufrn.dimap.middleware.extension.interfaces.InvocationInterceptorUnserialized;
import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.impl.InvocationData;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

/**  
 * Example of a Interceptor to generate Invocation Logs
 * 
 * @author giovannirosario
 */

public class GenerateLogInterceptor implements InvocationInterceptorUnserialized {
	private Logger logger;
	FileHandler fh;
	
	/*
	 * @param path_file Path to the file in which to log information in
	 * @param append Append option 
	 */
	public GenerateLogInterceptor (String path_file, Boolean append) {
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
	
	
	public void beforeInvocation(InvocationData invocationData, InvocationContext invocationContext) throws RemoteError {
		String msg;
		String operationName = invocationData.getOperationName();
		AbsoluteObjectReference aor = invocationData.getAor();
		String aor_id = Long.toString(aor.getObjectId().getObjectId());
		String aor_host = aor.getHost();
		String aor_port = Integer.toString(aor.getPort());
		
		msg = "Preparing to invoke operation " + aor_id + " " + operationName + " at " + aor_host + ":" + aor_port;

		logger.info(msg);
	}

	public void afterInvocation(InvocationData invocationData, InvocationContext invocationContext) throws RemoteError {
		String msg;
		String operationName = invocationData.getOperationName();
		AbsoluteObjectReference aor = invocationData.getAor();
		String aor_id = Long.toString(aor.getObjectId().getObjectId());
		String aor_host = aor.getHost();
		String aor_port = Integer.toString(aor.getPort());
		
		msg = "Finished invocation of operation " + aor_id + " " + operationName + " at " + aor_host + ":" + aor_port;

		logger.info(msg);
	}
}
