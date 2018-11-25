package br.ufrn.dimap.middleware.config.client;

import java.util.Scanner;

public class ConfigClient {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		// swap program below for naming lookup
		InterceptorConfigImpl interceptorConfig = new InterceptorConfigImpl();
		
		while(true) {
	        System.out.println("What do you want to do?");
	        System.out.println("\t 1 - To activate a Interceptor for the marshalled data;");
	        System.out.println("\t 2 - To disable a Interceptor for the marshalled data;");
	        System.out.println("\t 3 - To activate a Interceptor before the invoker");
	        System.out.println("\t 4 - To disable a Interceptor before the invoker;");
	        System.out.println("Or press Q to quit the config program;");
	        
	        String option = scan.next();
	        
	        String name;
	        if(option == "1") {
	        	System.out.println("Write the name of the Interceptor that you want to activate for the marshalled data");
	        	name = scan.next();
	        	interceptorConfig.startRequestInterceptor(name);
	        }
	        else if(option == "2") {
	        	System.out.println("Write the name of the Interceptor that you want to disable for the marshalled data");
	        	name = scan.next();
	        	interceptorConfig.stopRequestInterceptor(name);
	        }
	        else if(option == "3") {
	        	System.out.println("Write the name of the Interceptor that you want to activate before the invoker");
	        	name = scan.next();
	        	interceptorConfig.startInvocationInterceptor(name);
	        }
			else if(option == "4") {
				System.out.println("Write the name of the Interceptor that you want to disable before the invoker");
				name = scan.next();
				interceptorConfig.stopInvocationInterceptor(name);
			}
			else if(option == "Q" || option == "q"){
				System.out.println("Exiting...");
				break;
			}
		}
		
		scan.close();
	}
}
