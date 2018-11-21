package br.ufrn.dimap.middleware.installer;

import br.ufrn.dimap.middleware.remotting.impl.ClientProxy;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * Class that represents the result of an application in the middleware.
 *
 * @author Daniel Smith
 */
public class InstallationResult {

    /**
     * Class of the provided invoker implementation.
     */
    private final Class<? extends Invoker> invokerClass;

    /**
     * Class of the generated client proxy implementation.
     */
    private final Class<? extends ClientProxy> clientProxyClass;

    InstallationResult(Class invokerClass, Class clientProxyClass) {
        this.invokerClass = invokerClass;
        this.clientProxyClass = clientProxyClass;
    }

    public Class getInvokerClass() {
        return invokerClass;
    }

    public Class getClientProxyClass() {
        return clientProxyClass;
    }
}
