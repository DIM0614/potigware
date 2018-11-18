package br.ufrn.dimap.middleware.installer;

import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.remotting.generator.Generator;
import br.ufrn.dimap.middleware.remotting.impl.ClientProxy;
import br.ufrn.dimap.middleware.remotting.impl.ProxyCreator;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;
import br.ufrn.dimap.middleware.remotting.interfaces.NamingInstaller;
import br.ufrn.dimap.middleware.utils.Wrapper;
import br.ufrn.dimap.middleware.utils.classloader.DynamicClassLoader;
import br.ufrn.dimap.middleware.utils.compiler.JavaCompilerUtils;
import generated.MathImpl;
import generated.MathInvoker;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    private static final String DEFAULT_COMPILER_TARGET_PACKAGE = "generated";

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
                    w = new Wrapper<>(new ClientInstaller());
                    wrapper = w;
                }
            }
        }

        return w.getInstance();
    }

    /**
     * Takes an IDL description and install the generated files in
     * the naming service.
     *
     * @param idlPath
     */
    public Class<? extends ClientProxy> install(final String idlPath) throws InstallationException {
        try {
            return installInternal(idlPath);
        } catch (IOException | ParseException | ClassNotFoundException e) {
            throw new InstallationException(e);
        }
    }

    /**
     * Takes an Invoker implementation and the object id, binding them
     * to the naming service.
     *
     * @param idlPath
     */
    public void install(final String objName, final Class<? extends Invoker> invokerImpl) throws InstallationException {
        try {
            installImplementation(objName, invokerImpl);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | RemoteError | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new InstallationException(e);
        }
    }


    /**
     * Takes an Invoker implementation and the object id, binding them
     * to the naming service.
     *
     * @param idlPath
     */
    public <T extends ClientProxy> T install(final String objName, final Class<? extends Invoker> invokerImpl, final Class<T> proxyToReturn) throws InstallationException {
        try {
            installImplementation(objName, invokerImpl);
            return (T) ProxyCreator.getInstance().create(objName, proxyToReturn);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | RemoteError | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new InstallationException(e);
        }
    }

    /**
     * Install an implementation of an Invoker.
     *
     * It is important that the abstract Invoker and
     * the interface was already loaded in the naming service.
     *
     * @param objName
     * @param invokerImpl
     * @throws NoSuchMethodException
     * @throws RemoteError
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public void installImplementation(final String objName, Class<? extends Invoker> invokerImpl) throws NoSuchMethodException, RemoteError, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {

        String implPath = getClassResource(invokerImpl);

        // send classes over the network
        NamingInstaller lookup = (NamingInstaller) DefaultLookup.getInstance();
        lookup.installImplementation(objName, new File(implPath));
    }

    private String getClassResource(Class<?> klass) {
        return klass.getClassLoader().getResource(
                klass.getName().replace('.', '/') + ".class").toString();
    }

    /**
     * Takes an IDL description, generates code for it,
     * and bind it to the naming service.
     *
     * @param idlPath
     */
    private Class<? extends ClientProxy> installInternal(String idlPath) throws InstallationException, IOException, ParseException, ClassNotFoundException {
        String targetDir = getTargetDir();

        String classPath = getClasspathLocation(targetDir);

        Generator.GeneratedFilesInfo filesInfo = Generator.generateFiles(idlPath, targetDir, DEFAULT_COMPILER_TARGET_PACKAGE);
        compileClassFiles(targetDir, filesInfo);

        DynamicClassLoader dynamicClassLoader = DynamicClassLoader.getDynamicClassLoader();
        Class clientProxy = loadClasses(targetDir, filesInfo, dynamicClassLoader);

        // send classes over the network
        NamingInstaller lookup = (NamingInstaller) DefaultLookup.getInstance();
        lookup.installBase(new File(String.format("%s%s.class", classPath, filesInfo.getInterfName())),
                new File(String.format("%s%s.class", classPath, filesInfo.getInvokerName())));

        return clientProxy;
    }

    /**
     * Load classes from the generated .class
     *
     * @param targetDir
     * @param filesInfo
     * @param dynamicClassLoader
     */
    private Class<? extends ClientProxy> loadClasses(String targetDir, Generator.GeneratedFilesInfo filesInfo, DynamicClassLoader dynamicClassLoader) {
        String interfaceClassname = getClassname(filesInfo.getInterfName());
        String invokerClassname = getClassname(filesInfo.getInvokerName());

        String interfaceLocation = getClassFileLocation(targetDir, filesInfo.getInterfName());
        String invokerLocation = getClassFileLocation(targetDir, filesInfo.getInvokerName());

        dynamicClassLoader.loadClassFromFile(interfaceClassname, interfaceLocation);
        dynamicClassLoader.loadClassFromFile(invokerClassname, invokerLocation);

        String proxyClassname = getClassname(filesInfo.getProxyName());
        String proxyLocation = getClassFileLocation(targetDir, filesInfo.getProxyName());

        return dynamicClassLoader.loadClassFromFile(proxyClassname, proxyLocation);
    }

    /**
     * Compile java files, generating thus the .class files
     *
     * @param targetDir
     * @param filesInfo
     */
    private void compileClassFiles(String targetDir, Generator.GeneratedFilesInfo filesInfo) {
        String interfaceSourceCode = getSourcecodeFilePath(filesInfo.getInterfName());
        String invokerSourceCode = getSourcecodeFilePath(filesInfo.getInvokerName());
        String proxySourcecode = getSourcecodeFilePath(filesInfo.getProxyName());

        JavaCompilerUtils.compile(targetDir, interfaceSourceCode, invokerSourceCode, proxySourcecode);
    }

    private String getClasspathLocation(String targetDir) {
        return String.format("%s/%s/", targetDir, DEFAULT_COMPILER_TARGET_PACKAGE);
    }

    private String getTargetDir() {
        return String.format("%s/src/main/java/", System.getProperty("user.dir"));
    }

    private String getClassFileLocation(String targetDir, String classname) {
        return String.format("file:%s/%s/%s.class", targetDir, DEFAULT_COMPILER_TARGET_PACKAGE, classname);
    }

    private String getClassname(String classname) {
        return String.format("%s.%s", DEFAULT_COMPILER_TARGET_PACKAGE, classname);
    }

    private String getSourcecodeFilePath(String className) {
        return String.format("%s/%s.java", DEFAULT_COMPILER_TARGET_PACKAGE, className);
    }

    public static void main(String[] args) {
        try {
            Class clientProxyClass = ClientInstaller.getInstance().install("C:\\Users\\Daniel\\IdeaProjects\\middlewareProjects\\interface-description-language\\src\\main\\java\\files\\example.json");
            ClientProxy clientProxy = ClientInstaller.getInstance().install("abc", MathImpl.class, clientProxyClass);
        } catch (InstallationException e) {
            Logger.getLogger(ClientInstaller.class.getName()).log(Level.SEVERE,e.getMessage(),e);
        }
    }

}
