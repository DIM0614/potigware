package br.ufrn.dimap.middleware.integration;

import br.ufrn.dimap.middleware.remotting.impl.ProxyCreator;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.impl.ServerRequestHandlerImpl;
import br.ufrn.dimap.middleware.remotting.interfaces.ServerRequestHandler;
import generated.ClientMath;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class IntegrationTest {

    public static void main(String[] args) {

        generated.Math math = null;
        try {

            System.out.println("Creating proxy");

            math = (generated.Math) ProxyCreator.getInstance().create("math", ClientMath.class);
            
            
            if (math != null) {
            	System.out.println("Created");

            	System.out.println("Pi value: " + math.pi(0.1f));

                System.out.println("Fib number value: " + math.fibonacci(0, 20));
            } else {
                System.out.println("Object not found");
            }

        } catch (RemoteError remoteError) {
            remoteError.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
