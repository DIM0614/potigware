package br.ufrn.dimap.middleware.remotting.exec;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.impl.ServerRequestHandlerImpl;
import br.ufrn.dimap.middleware.remotting.interfaces.ServerRequestHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MiddlewareMain {

    private static final int MIDDLEWARE_PORT = 8001;

    public static void main(String[] args) {

        try {


            ServerRequestHandler reqhander = new ServerRequestHandlerImpl(MIDDLEWARE_PORT);

            reqhander.init();

            Logger.getLogger(MiddlewareMain.class.getName()).log(Level.INFO, "Middleware server started at port " + MIDDLEWARE_PORT);

        } catch (RemoteError remoteError) {
            remoteError.printStackTrace();
        }

    }

}
