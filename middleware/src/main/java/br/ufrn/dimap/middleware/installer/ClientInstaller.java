package br.ufrn.dimap.middleware.installer;

import br.ufrn.dimap.middleware.identification.NetAddr;
import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.remotting.generator.Generator;
import br.ufrn.dimap.middleware.remotting.impl.ClientProxy;
import br.ufrn.dimap.middleware.remotting.impl.DeploymentDescriptor;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import br.ufrn.dimap.middleware.remotting.interfaces.NamingInstaller;
import br.ufrn.dimap.middleware.utils.Wrapper;
import br.ufrn.dimap.middleware.utils.classloader.DynamicClassLoader;

import com.sun.security.ntlm.Client;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static br.ufrn.dimap.middleware.installer.InstallationConfig.getClassFileLocation;
import static br.ufrn.dimap.middleware.installer.InstallationConfig.getClassname;
import static br.ufrn.dimap.middleware.utils.compiler.JavaCompilerUtils.compile;

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
     * Generate files from IDL and install them locally.
     *
     *
     * @param idlPath
     * @return
     * @throws ParseException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public InstallationResult installLocalBase(final String idlPath) throws ParseException, IOException, ClassNotFoundException {

        logger.log(Level.INFO,"Generating the invoker stub.");

        String targetDir = InstallationConfig.getTargetDir();

        Generator.GeneratedFilesInfo filesInfo = Generator.generateFiles(idlPath, targetDir, InstallationConfig.DEFAULT_COMPILER_TARGET_PACKAGE);

        logger.log(Level.INFO,"Compiling base classes...");

        compileClassFiles(targetDir, filesInfo);

        logger.log(Level.INFO,"Loading base classes...");

        dynamicClassLoader.loadClassFromFile(getClassname(filesInfo.getInterfName()), getClassFileLocation(targetDir, filesInfo.getInterfName()));
        Class invokerClass = dynamicClassLoader.loadClassFromFile(getClassname(filesInfo.getInvokerName()), getClassFileLocation(targetDir, filesInfo.getInvokerName()));
        Class clientProxyClass = (Class<? extends ClientProxy>) dynamicClassLoader.loadClassFromFile(getClassname(filesInfo.getProxyName()), getClassFileLocation(targetDir, filesInfo.getProxyName()));

        logger.log(Level.INFO,"Interface and abstract invoker are now available");

        return new InstallationResult(invokerClass, clientProxyClass, filesInfo);
    }

    /**
     * Install an implementation in the naming service.
     *
     * @param ir
     * @param remoteObjectName
     * @param implementationName
     * @return
     * @throws IOException
     * @throws RemoteError
     */
    public InstallationResult implRemoteInstall(InstallationResult ir, String remoteObjectName, String implementationName, NetAddr middlewareAddr) throws IOException, RemoteError {

        logger.log(Level.INFO,"Start installing the implementation remotely...");

        String targetDir = InstallationConfig.getTargetDir();
        String classPath = InstallationConfig.getClasspathLocation(targetDir);

        Generator.GeneratedFilesInfo filesInfo = ir.getFilesInfo();

        logger.log(Level.INFO,"Compiling implementation..");

        compile(InstallationConfig.getTargetDir(), InstallationConfig.getSourcecodeFilePath(implementationName));

        logger.log(Level.INFO,"Loading implementation..");

        Class clazz = dynamicClassLoader.loadClassFromFile(getClassname(implementationName), getClassFileLocation(InstallationConfig.getTargetDir(), implementationName));

        logger.log(Level.INFO,"Send files to naming service...");

        deployApplication(middlewareAddr, remoteObjectName, filesInfo.getInterfName(), filesInfo.getInvokerName(),  implementationName , classPath);

        logger.log(Level.INFO,"Implementation installed");

        ir.setImplClass(clazz);

        return ir;
    }

    /**
     * Takes an IDL description and install the generated files in
     * the naming service.
     *
     * //TODO: remove this, since it must be separated into two steps
     * @param idlPath
     */
    public InstallationResult install(final String idlPath, String remoteObjectName, String implementationName, NetAddr middlewareAddr) throws InstallationException {
        try {

            logger.log(Level.INFO,"Generating the invoker stub.");

			String targetDir = InstallationConfig.getTargetDir();
			String classPath = InstallationConfig.getClasspathLocation(targetDir);

            Generator.GeneratedFilesInfo filesInfo = Generator.generateFiles(idlPath, targetDir, InstallationConfig.DEFAULT_COMPILER_TARGET_PACKAGE);

			compileClassFiles(targetDir, filesInfo);

            logger.log(Level.INFO,"Compiling the invoker implementation.");

			compile(InstallationConfig.getTargetDir(), InstallationConfig.getSourcecodeFilePath(implementationName));

            logger.log(Level.INFO,"Loading the stub and the invoker implementation.");

            dynamicClassLoader.loadClassFromFile(getClassname(filesInfo.getInterfName()), getClassFileLocation(targetDir, filesInfo.getInterfName()));
            Class invokerClass = dynamicClassLoader.loadClassFromFile(getClassname(filesInfo.getInvokerName()), getClassFileLocation(targetDir, filesInfo.getInvokerName()));

            Class clientProxy = (Class<? extends ClientProxy>) dynamicClassLoader.loadClassFromFile(getClassname(filesInfo.getProxyName()), getClassFileLocation(targetDir, filesInfo.getProxyName()));
            Class clazz = dynamicClassLoader.loadClassFromFile(getClassname(implementationName), getClassFileLocation(InstallationConfig.getTargetDir(), implementationName));
			
			logger.log(Level.INFO, "Sending classes through the network.");

            deployApplication(middlewareAddr, remoteObjectName, filesInfo.getInterfName(), filesInfo.getInvokerName(),  implementationName , classPath);

            logger.log(Level.INFO,"Implementation generation has completed with success.");

            return new InstallationResult(invokerClass, clientProxy, clazz);

        } catch (IOException | ClassNotFoundException | ParseException e) {
            throw new InstallationException(e);
        } catch (RemoteError remoteError) {
            remoteError.printStackTrace();
        }

        return null;
    }

    /**
     * Register the application into the naming service.
     *
     * @param remoteObjectName the name of the remote object.
     * @param invokerName the name of the invoker class
     * @param interfaceName the name of the IDL generated interface class
     * @param classPath the source folder where the classes were placed.
     */
    private void deployApplication(NetAddr middlewareAddr, String remoteObjectName, String interfaceName, String invokerName, String implementationName, String classPath) throws IOException, RemoteError {

        DeploymentDescriptor deploymentDescriptor = DeploymentDescriptor.createDeploymentDescriptor(
                remoteObjectName,
                new File(String.format("%s%s.class", classPath, interfaceName)),
                new File(String.format("%s%s.class", classPath, invokerName)),
                new File(String.format("%s%s.class", classPath, implementationName)));

        NamingInstaller lookup = (NamingInstaller) DefaultLookup.getInstance();
        lookup.install(deploymentDescriptor, middlewareAddr);
    }

    /**
     * Compile java files, generating thus the .class files
     *
     * @param targetDir the target directory of the compilation.
     * @param filesInfo the result of the dynamic class generation.
     */
    private void compileClassFiles(String targetDir, Generator.GeneratedFilesInfo filesInfo) {
        String interfaceSourceCode = InstallationConfig.getSourcecodeFilePath(filesInfo.getInterfName());
        String invokerSourceCode = InstallationConfig.getSourcecodeFilePath(filesInfo.getInvokerName());
        String proxySourcecode = InstallationConfig.getSourcecodeFilePath(filesInfo.getProxyName());

        compile(targetDir, interfaceSourceCode, invokerSourceCode, proxySourcecode);
    }

    /**
     * Main method for the CLI remote object installer application.
     *
     * @param args the arg array that provides the three required parameters:
     *             <ul>
     *                  <li>The idl path in the file system</li>
     *                  <li>The name of the invoker class created into the generated directory.</li>
     *                  <li>The remote object name to be deployed.</li>
     *             </ul>
     *
     * @throws IllegalArgumentException if any of the required parameters is null.
     */
    public static void main(String[] args) {

        try(Scanner scanner = new Scanner(System.in)) {

            while (true) {

                System.out.println("Welcome to the object implementation tool!");

                System.out.println("Please, provide your IDL description path: ");

                String idlPath = scanner.nextLine();

                try {

                    InstallationResult ir = ClientInstaller.getInstance().installLocalBase(idlPath);

                    System.out.println("Your files were generated in the 'generated' package and " +
                            "loaded in the current JMV instance.\n");
                    System.out.println("Now, create a concrete implementation of the generated invoker in the 'generated' package " +
                            "and provide its name:");

                    String implName = null;

                    while (true) {

                        implName = scanner.nextLine();

                        String providedPath = InstallationConfig.getTargetDir() + InstallationConfig.getSourcecodeFilePath(implName);

                        if (!(new File(providedPath).exists())) {
                            System.out.println("(X) This implementation file doesn't exist! Provide " +
                                    "a correct one.");
                            continue;
                        } else break;
                    }

                    System.out.println("How do you want to call your object? ");

                    String objName = scanner.nextLine();

                    System.out.println("Finally, provide the middleware network location:");

                    String host = scanner.nextLine();

                    Integer port = scanner.nextInt();

                    System.out.println("Installing...");

                    try {

                        ClientInstaller.getInstance().implRemoteInstall(ir, objName, implName, new NetAddr(host, port));

                        System.out.println("Done!");
                        System.out.println("Now you can create the proxy using the following information:\n" +
                                "(*) object name: " + objName + "\n" +
                                "(*) host: " + host + "\n" +
                                "(*) port: " + port);

                        break;

                    } catch (RemoteError remoteError) {
                        System.out.println("(X) Error while installing files in the naming server");
                    }

                } catch (ParseException | IOException e) {
                    System.out.println("(X) Error while parsing the provided IDL description");
                    continue;
                } catch (ClassNotFoundException e) {
                    System.out.println("(X) Error when trying to load the generated files");
                    continue;
                }
            }
        }

    }

}
