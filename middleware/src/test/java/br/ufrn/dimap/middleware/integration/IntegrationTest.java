package br.ufrn.dimap.middleware.integration;

import br.ufrn.dimap.middleware.remotting.async.CallbackBuilder;
import br.ufrn.dimap.middleware.remotting.async.OnErrorCallback;
import br.ufrn.dimap.middleware.remotting.async.OnResultCallback;
import br.ufrn.dimap.middleware.remotting.impl.ProxyCreator;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Callback;
import br.ufrn.dimap.middleware.remotting.interfaces.InvocationAsynchronyPattern;
import br.ufrn.dimap.middleware.remotting.interfaces.PollObject;
import generated.ClientMath;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class IntegrationTest {

    public static void main(String[] args) {

        generated.ClientMath math = null;
        try {

            System.out.println("Creating proxy");

            math = (generated.ClientMath) ProxyCreator.getInstance().create("math", ClientMath.class);

            if (math != null) {
            	System.out.println("Created");

            	System.out.println("Pi value: " + math.pi(0.1f));

                System.out.println("Fib number value: " + math.fibonacci(0, 20));

                CallbackBuilder callbackBuilder = new CallbackBuilder();
                callbackBuilder.onError(error -> error.printStackTrace())
                        .onResult(returnedData -> {
                            Float piValue = (Float) returnedData;
                            System.out.println("Pi value returned from callback: " + piValue);
                        });
                Callback piCallback = callbackBuilder.build();

                math.pi(0.1f, piCallback);

                math.pi(3.14f, InvocationAsynchronyPattern.FIRE_AND_FORGET);

                PollObject pollObject = (PollObject) math.pi(3.14f, InvocationAsynchronyPattern.POLL_OBJECT);

                while (!pollObject.resultAvailable()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Result from poll object: " + pollObject.getResult());

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
