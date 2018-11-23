package br.ufrn.dimap.middleware.remotting.exec;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.impl.ServerRequestHandlerImpl;
import br.ufrn.dimap.middleware.remotting.interfaces.ServerRequestHandler;

public class MiddlewareMain {

    public static void main(String[] args) {

        try {
            ServerRequestHandler reqhander = new ServerRequestHandlerImpl(8001);

            reqhander.init();

        } catch (RemoteError remoteError) {
            remoteError.printStackTrace();
        }

    }

}
