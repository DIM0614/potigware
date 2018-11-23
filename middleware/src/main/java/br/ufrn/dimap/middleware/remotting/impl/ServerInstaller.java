package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.remotting.generator.Generator;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;
import br.ufrn.dimap.middleware.utils.classloader.DynamicClassLoader;
import org.json.simple.parser.ParseException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * Provides an easy way to install classes in the server.
 *
 * @author vitorgreati
 */
public class ServerInstaller {

    private static Wrapper<ServerInstaller> wrapper;

    /**
     * Contains all the necessary data to load the
     * arriving classes.
     *
     * @author vitorgreati
     */
    public class InstallationPack {
        private String className;
        private InputStream istream;

        public InstallationPack(String className, InputStream istream) {
            this.className = className;
            this.istream = istream;
        }

        public String getClassName() {
            return className;
        }

        public InputStream getIstream() {
            return istream;
        }
    }

    /**
     * Takes an IDL description, generates code for it,
     * and bind it to the naming service.
     *
     * @param idlPath
     */
    public Class<? extends Invoker> install(InstallationPack interfacePack, InstallationPack invokerPack) throws IOException {
        DynamicClassLoader loader = DynamicClassLoader.getDynamicClassLoader();
        // load interface
        loader.loadClassFromInputStream(interfacePack.getIstream(), interfacePack.getClassName());
        // load invoker
        Class invoker =  loader.loadClassFromInputStream(invokerPack.getIstream(), invokerPack.getClassName());

        return invoker;
    }

    private ServerInstaller(){}

    public static ServerInstaller getInstance () {
        ServerInstaller.Wrapper<ServerInstaller> w = wrapper;
        if (w == null) { // check 1
            synchronized (ServerInstaller.class) {
                w = wrapper;
                if (w == null) { // check 2
                    w = new ServerInstaller.Wrapper<ServerInstaller>(new ServerInstaller());
                    wrapper = w;
                }
            }
        }

        return w.getInstance();
    }

    private static class Wrapper<T> {
        private T instance;
        public Wrapper(T service) { this.instance = service; }
        public T getInstance() { return this.instance; }
    }

}
