package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.remotting.generator.Generator;
import br.ufrn.dimap.middleware.remotting.interfaces.NamingInstaller;
import br.ufrn.dimap.middleware.utils.classloader.DynamicClassLoader;
import org.json.simple.parser.ParseException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Provides an easy way to installer an IDL-specified interface in the server.
 *
 * @author vitorgreati
 */
public class ClientInstaller {

    private static Wrapper<ClientInstaller> wrapper;

    /**
     * Takes an IDL description, generates code for it,
     * and bind it to the naming service.
     *
     * @param idlPath
     */
    public Class<? extends  ClientProxy> install(final String objName, final String idlPath) throws ParseException, IOException, ClassNotFoundException, RemoteError, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        String targetDir = String.format("%s/src/main/java/", System.getProperty("user.dir"));
        String targetPackage = "generated";
        String classPath = String.format("%s/%s/", targetDir, targetPackage);

        if (targetDir != null) {
            // generate java files
            Generator.GeneratedFilesInfo filesInfo = Generator.generateFiles(idlPath, targetDir, targetPackage);
            // compile java files, generating thus the .class files
            compile(targetDir, String.format("%s/%s.java", targetPackage, filesInfo.getInterfName()));
            compile(targetDir, String.format("%s/%s.java", targetPackage, filesInfo.getInvokerName()));
            compile(targetDir, String.format("%s/%s.java", targetPackage, filesInfo.getProxyName()));
            // load classes from the generated .class
            DynamicClassLoader.getDynamicClassLoader().loadClassFromFile(targetPackage + "." + filesInfo.getInterfName(), String.format("file:%s/%s/%s.class", targetDir, targetPackage, filesInfo.getInterfName()));
            DynamicClassLoader.getDynamicClassLoader().loadClassFromFile(targetPackage + "." + filesInfo.getInvokerName(), String.format("file:%s/%s/%s.class", targetDir, targetPackage, filesInfo.getInvokerName()));
            Class client = DynamicClassLoader.getDynamicClassLoader().loadClassFromFile(targetPackage + "." + filesInfo.getProxyName(), String.format("file:%s/%s/%s.class", targetDir, targetPackage, filesInfo.getProxyName()));
            // send classes over the network
            NamingInstaller lookup = (NamingInstaller) DefaultLookup.getInstance();
            lookup.install(objName, new File(String.format("%s%s.class", classPath, filesInfo.getInterfName())),
                    new File(String.format("%s%s.class", classPath, filesInfo.getInvokerName())));
            return client;
        }

        return null;
    }

    /**
     * Compile a .java into the respective folder.
     *
     * @param rootPath
     * @param fileName
     */
    private void compile(String rootPath, String fileName) {
        File root =  new File(rootPath);
        File sourceFile = new File(root,fileName);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, sourceFile.getPath());
    }

    private ClientInstaller(){}

    public static ClientInstaller getInstance () {
        ClientInstaller.Wrapper<ClientInstaller> w = wrapper;
        if (w == null) { // check 1
            synchronized (ClientInstaller.class) {
                w = wrapper;
                if (w == null) { // check 2
                    w = new ClientInstaller.Wrapper<ClientInstaller>(new ClientInstaller());
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

    public static void main(String[] args) {

        try {
            Class proxy = ClientInstaller.getInstance().install("math", "/home/vitorgreati/math.json");

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (RemoteError remoteError) {
            remoteError.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
