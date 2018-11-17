package br.ufrn.dimap.middleware.installer;

import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.remotting.generator.Generator;
import br.ufrn.dimap.middleware.remotting.impl.ClientProxy;
import br.ufrn.dimap.middleware.remotting.interfaces.NamingInstaller;
import br.ufrn.dimap.middleware.utils.Wrapper;
import br.ufrn.dimap.middleware.utils.classloader.DynamicClassLoader;
import br.ufrn.dimap.middleware.utils.compiler.JavaCompilerUtils;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides an easy way to installer an IDL-specified interface in the server.
 *
 * <br>
 * Implements the singleton pattern.
 *
 * @author vitorgreati
 * @author Daniel Smith
 */
public class ClientInstaller {
    /**
     * Represents the location where the compiled files are placed.
     */
    public static final String DEFAULT_COMPLILER_TARGET_PACKAGE = "generated";

    /**
     * Private constructor for the singleton instance.
     */
    private ClientInstaller() {}

    /**
     * Represents the wrapped instance of this singleton.
     */
    private static Wrapper<ClientInstaller> wrapper;

    /**
     * Returns the instance of the singleton.
     *
     * @return the singleton instance, if exists. If the single instances wasn't created, creates one and then return.
     */
    public static ClientInstaller getInstance() {
        Wrapper<ClientInstaller> w = wrapper;
        if (w == null) { // check 1
            synchronized (ClientInstaller.class) {
                w = wrapper;
                if (w == null) { // check 2
                    w = new Wrapper<ClientInstaller>(new ClientInstaller());
                    wrapper = w;
                }
            }
        }

        return w.getInstance();
    }

    /**
     * Takes an IDL description, generates code for it,
     * and bind it to the naming service.
     *
     * @param idlPath
     */
    public Class<? extends ClientProxy> install(final String objName, final String idlPath) throws InstallationException {
        try {
            return installInternal(objName, idlPath);
        } catch (IOException e) {
            throw new InstallationException(e);
        } catch (ParseException e) {
            throw new InstallationException(e);
        } catch (ClassNotFoundException e) {
            throw new InstallationException(e);
        }
    }

    /**
     * Takes an IDL description, generates code for it,
     * and bind it to the naming service.
     *
     * @param idlPath
     */
    private Class<? extends ClientProxy> installInternal(String objName, String idlPath) throws IOException, ParseException, ClassNotFoundException {
        String targetDir = String.format("%s/src/main/java/", System.getProperty("user.dir"));
        String classPath = String.format("%s/%s/", targetDir, DEFAULT_COMPLILER_TARGET_PACKAGE);

        // generate java files
        Generator.GeneratedFilesInfo filesInfo = Generator.generateFiles(idlPath, targetDir, DEFAULT_COMPLILER_TARGET_PACKAGE);
        // compile java files, generating thus the .class files
        String interfaceFile = String.format("%s/%s.java", DEFAULT_COMPLILER_TARGET_PACKAGE, filesInfo.getInterfName());
        JavaCompilerUtils.compile(targetDir, interfaceFile);

        String invokerFile = String.format("%s/%s.java", DEFAULT_COMPLILER_TARGET_PACKAGE, filesInfo.getInvokerName());
        JavaCompilerUtils.compile(targetDir, invokerFile);

        String proxyFile = String.format("%s/%s.java", DEFAULT_COMPLILER_TARGET_PACKAGE, filesInfo.getProxyName());
        JavaCompilerUtils.compile(targetDir, proxyFile);

        // load classes from the generated .class
        DynamicClassLoader dynamicClassLoader = DynamicClassLoader.getDynamicClassLoader();

        dynamicClassLoader.loadClassFromFile(String.format("%s.%s", DEFAULT_COMPLILER_TARGET_PACKAGE, filesInfo.getInterfName()),
                String.format("file:%s/%s/%s.class", targetDir, DEFAULT_COMPLILER_TARGET_PACKAGE, filesInfo.getInterfName()));
        dynamicClassLoader.loadClassFromFile(String.format("%s.%s", DEFAULT_COMPLILER_TARGET_PACKAGE, filesInfo.getInvokerName()),
                String.format("file:%s/%s/%s.class", targetDir, DEFAULT_COMPLILER_TARGET_PACKAGE, filesInfo.getInvokerName()));

        Class client = dynamicClassLoader.loadClassFromFile(String.format("%s.%s", DEFAULT_COMPLILER_TARGET_PACKAGE, filesInfo.getProxyName()),
                String.format("file:%s/%s/%s.class", targetDir, DEFAULT_COMPLILER_TARGET_PACKAGE, filesInfo.getProxyName()));
        // send classes over the network
        NamingInstaller lookup = (NamingInstaller) DefaultLookup.getInstance();
        lookup.install(objName, new File(String.format("%s%s.class", classPath, filesInfo.getInterfName())),
                new File(String.format("%s%s.class", classPath, filesInfo.getInvokerName())));

        return client;
    }

    public static void main(String[] args) {
        try {
            Class proxy = ClientInstaller.getInstance().install("math", "/home/vitorgreati/math.json");
        } catch (InstallationException e) {
            Logger.getLogger(ClientInstaller.class.getName()).log(Level.SEVERE,e.getMessage(),e);
        }
    }

}
