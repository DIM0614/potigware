package br.ufrn.dimap.middleware.installer;

import br.ufrn.dimap.middleware.remotting.generator.Generator;
import br.ufrn.dimap.middleware.remotting.impl.ClientProxy;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

/**
 * Class that represents the result of an application install in the middleware.
 *
 * @author Daniel Smith
 */
public class InstallationResult {

    /**
     *  Generated files information.
     *
     */
    private Generator.GeneratedFilesInfo filesInfo;

    /**
     * Class of the provided invoker implementation.
     */
    private Class<? extends Invoker> implClass;

    /**
     * Class of the base invoker.
     */
    private final Class<Invoker> invokerClass;

    /**
     * Class of the generated client proxy implementation.
     */
    private final Class<? extends ClientProxy> clientProxyClass;

    InstallationResult(Class invokerClass, Class clientProxyClass, Generator.GeneratedFilesInfo filesInfo) {
        this.invokerClass = invokerClass;
        this.clientProxyClass = clientProxyClass;
        this.filesInfo = filesInfo;
    }

    InstallationResult(Class invokerClass, Class clientProxyClass, Class implClass) {
        this.invokerClass = invokerClass;
        this.clientProxyClass = clientProxyClass;
        this.implClass = implClass;
    }

    public Class getInvokerClass() {
        return invokerClass;
    }

    public Class getClientProxyClass() {
        return clientProxyClass;
    }

    public Class<? extends Invoker> getImplClass() {
        return implClass;
    }

    public void setImplClass(Class<? extends Invoker> implClass) {
        this.implClass = implClass;
    }

    public Generator.GeneratedFilesInfo getFilesInfo() {
        return filesInfo;
    }

}
