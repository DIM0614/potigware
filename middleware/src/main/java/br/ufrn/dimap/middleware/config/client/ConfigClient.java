package br.ufrn.dimap.middleware.config.client;

import java.util.Scanner;

import br.ufrn.dimap.middleware.MiddlewareConfig;
import br.ufrn.dimap.middleware.config.client.interceptorConfig.ClientInterceptorConfig;
import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.impl.ClientRequestHandlerImpl;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public class ConfigClient {
	public static void main(String[] args) throws RemoteError{
		Scanner scan = new Scanner(System.in);
		
		AbsoluteObjectReference aor = MiddlewareConfig.getConfigaor();
		
		ClientInterceptorConfig interceptorConfig = new ClientInterceptorConfig(aor);
				
		while(true) {
	        System.out.println("What do you want to do?");
	        System.out.println("\t 1 - To activate a Interceptor for the marshalled data;");
	        System.out.println("\t 2 - To disable a Interceptor for the marshalled data;");
	        System.out.println("\t 3 - To activate a Interceptor before the invoker");
	        System.out.println("\t 4 - To disable a Interceptor before the invoker;");
	        System.out.println("Or press Q to quit the config program;");
	        
	        String option = scan.next();
	        
	        String name;
	        if(option.equals("1")) {
	        	System.out.println("Write the name of the Interceptor that you want to activate for the marshalled data");
	        	name = scan.next();
	        	interceptorConfig.startRequestInterceptor(name);
	        }
	        else if(option.equals("2")) {
	        	System.out.println("Write the name of the Interceptor that you want to disable for the marshalled data");
	        	name = scan.next();
	        	interceptorConfig.stopRequestInterceptor(name);
	        }
	        else if(option.equals("3")) {
	        	System.out.println("Write the name of the Interceptor that you want to activate before the invoker");
	        	name = scan.next();
	        	interceptorConfig.startInvocationInterceptor(name);
	        }
			else if(option.equals("4")) {
				System.out.println("Write the name of the Interceptor that you want to disable before the invoker");
				name = scan.next();
				interceptorConfig.stopInvocationInterceptor(name);
			}
			else if(option.equals("Q") || option.equals("q")){
				System.out.println("Exiting...");
				break;
			}
		}
		
		scan.close();
		ClientRequestHandlerImpl.getInstance().shutdown();
	}
}
