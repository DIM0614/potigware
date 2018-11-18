package br.ufrn.dimap.middleware.installer;

import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.remotting.generator.Generator;
import br.ufrn.dimap.middleware.remotting.impl.ClientProxy;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;
import br.ufrn.dimap.middleware.remotting.interfaces.NamingInstaller;
import br.ufrn.dimap.middleware.utils.Wrapper;
import br.ufrn.dimap.middleware.utils.classloader.DynamicClassLoader;
import br.ufrn.dimap.middleware.utils.compiler.JavaCompilerUtils;

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
    static final String DEFAULT_COMPILER_TARGET_PACKAGE = "generated";

    private final DynamicClassLoader dynamicClassLoader = DynamicClassLoader.getDynamicClassLoader();

    /**
     * Instance logger for the client installer.
     */
    private Logger logger = Logger.getLogger(ClientInstaller.class.getName());

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
    public InstallationResult install(final String idlPath, String remoteObjectName, String invokerName) throws InstallationException {
        try {

            logger.log(Level.INFO,"Generating and loading the invoker stub.");

            Class<? extends ClientProxy> clientProxyClass = installStub(idlPath);

            logger.log(Level.INFO,"Compiling and loading the invoker implementation.");

            Class invokerClass = installImplementation(remoteObjectName, invokerName);

            logger.log(Level.INFO,"Implementation generation has completed with success.");

            return new InstallationResult(invokerClass, clientProxyClass);

        } catch (IOException | ClassNotFoundException | ParseException e) {
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
     *
     * @throws NoSuchMethodException
     * @throws RemoteError
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public Class installImplementation(final String objName, String implementationClass){

        //String implPath = getClassResource(invokerImpl);
        JavaCompilerUtils.compile(InstalationConfig.getTargetDir(), InstalationConfig.getSourcecodeFilePath(implementationClass));
        Class clazz = dynamicClassLoader.loadClassFromFile(InstalationConfig.getClassname(implementationClass),InstalationConfig.getClassFileLocation(InstalationConfig.getTargetDir(), implementationClass));

        // send classes over the network
        NamingInstaller lookup = (NamingInstaller) DefaultLookup.getInstance();
        lookup.installImplementation(objName, new File(implementationClass));

        return clazz;
    }

    /**
     * Takes an IDL description, generates code for it,
     * and bind it to the naming service.
     *
     * @param idlPath
     */
    private Class<? extends ClientProxy> installStub(String idlPath) throws IOException, ParseException, ClassNotFoundException {
        String targetDir = InstalationConfig.getTargetDir();

        String classPath = InstalationConfig.getClasspathLocation(targetDir);

        Generator.GeneratedFilesInfo filesInfo = Generator.generateFiles(idlPath, targetDir, DEFAULT_COMPILER_TARGET_PACKAGE);
        compileClassFiles(targetDir, filesInfo);

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
        String interfaceClassname = InstalationConfig.getClassname(filesInfo.getInterfName());
        String invokerClassname = InstalationConfig.getClassname(filesInfo.getInvokerName());

        String interfaceLocation = InstalationConfig.getClassFileLocation(targetDir, filesInfo.getInterfName());
        String invokerLocation = InstalationConfig.getClassFileLocation(targetDir, filesInfo.getInvokerName());

        dynamicClassLoader.loadClassFromFile(interfaceClassname, interfaceLocation);
        dynamicClassLoader.loadClassFromFile(invokerClassname, invokerLocation);

        String proxyClassname = InstalationConfig.getClassname(filesInfo.getProxyName());
        String proxyLocation = InstalationConfig.getClassFileLocation(targetDir, filesInfo.getProxyName());

        return dynamicClassLoader.loadClassFromFile(proxyClassname, proxyLocation);
    }

    /**
     * Compile java files, generating thus the .class files
     *
     * @param targetDir
     * @param filesInfo
     */
    private void compileClassFiles(String targetDir, Generator.GeneratedFilesInfo filesInfo) {
        String interfaceSourceCode = InstalationConfig.getSourcecodeFilePath(filesInfo.getInterfName());
        String invokerSourceCode = InstalationConfig.getSourcecodeFilePath(filesInfo.getInvokerName());
        String proxySourcecode = InstalationConfig.getSourcecodeFilePath(filesInfo.getProxyName());

        JavaCompilerUtils.compile(targetDir, interfaceSourceCode, invokerSourceCode, proxySourcecode);
    }

    public static void main(String[] args) {
        try {
            String idlPath = args[0];
            String invokerName = args[1];
            String remoteObjectName = args[2];

            //EXAMPLE
            //String idlPath = "C:\\Users\\Daniel\\IdeaProjects\\middlewareProjects\\interface-description-language\\src\\main\\java\\files\\example.json";
            //String invokerName = "MathImpl";
            //String remoteObjectName = "Math";

            InstallationResult installationResult = ClientInstaller.getInstance().install(idlPath, remoteObjectName,invokerName);

            Class clientProxyClass = installationResult.getClientProxyClass();
            Class invokerClass = installationResult.getInvokerClass();

        } catch (InstallationException e) {
            Logger.getLogger(ClientInstaller.class.getName()).log(Level.SEVERE,e.getMessage(),e);
        }
    }

    class InstallationResult{

        private final Class<? extends Invoker> invokerClass;
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

}
