package br.ufrn.dimap.middleware.installer;

import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.remotting.generator.Generator;
import br.ufrn.dimap.middleware.remotting.impl.ClientProxy;
import br.ufrn.dimap.middleware.remotting.impl.DeploymentDescriptor;
import br.ufrn.dimap.middleware.remotting.interfaces.NamingInstaller;
import br.ufrn.dimap.middleware.utils.Wrapper;
import br.ufrn.dimap.middleware.utils.classloader.DynamicClassLoader;

import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
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
     * Takes an IDL description and install the generated files in
     * the naming service.
     *
     * @param idlPath
     */
    public InstallationResult install(final String idlPath, String remoteObjectName, String invokerName) throws InstallationException {
        try {

            logger.log(Level.INFO,"Generating the invoker stub.");

			String targetDir = InstallationConfig.getTargetDir();
			String classPath = InstallationConfig.getClasspathLocation(targetDir);

            Generator.GeneratedFilesInfo filesInfo = Generator.generateFiles(idlPath, targetDir, InstallationConfig.DEFAULT_COMPILER_TARGET_PACKAGE);

			compileClassFiles(targetDir, filesInfo);

            logger.log(Level.INFO,"Compiling the invoker implementation.");

			compile(InstallationConfig.getTargetDir(), InstallationConfig.getSourcecodeFilePath(invokerName));

            logger.log(Level.INFO,"Loading the stub and the invoker implementation.");

            dynamicClassLoader.loadClassFromFile(getClassname(filesInfo.getInterfName()), getClassFileLocation(targetDir, filesInfo.getInterfName()));
            dynamicClassLoader.loadClassFromFile(getClassname(filesInfo.getInvokerName()), getClassFileLocation(targetDir, filesInfo.getInvokerName()));

            Class clientProxy = (Class<? extends ClientProxy>) dynamicClassLoader.loadClassFromFile(getClassname(filesInfo.getProxyName()), getClassFileLocation(targetDir, filesInfo.getProxyName()));
            Class clazz = dynamicClassLoader.loadClassFromFile(getClassname(invokerName), getClassFileLocation(InstallationConfig.getTargetDir(), invokerName));
			
			logger.log(Level.INFO, "Sending classes through the network.");

            deployApplication(remoteObjectName, invokerName, filesInfo.getInterfName(), classPath);

            logger.log(Level.INFO,"Implementation generation has completed with success.");

            return new InstallationResult(clazz, clientProxy);

        } catch (IOException | ClassNotFoundException | ParseException e) {
            throw new InstallationException(e);
        }
    }

    /**
     * Register the application into the naming service.
     *
     * @param remoteObjectName the name of the remote object.
     * @param invokerName the name of the invoker class
     * @param interfaceName the name of the IDL generated interface class
     * @param classPath the source folder where the classes were placed.
     */
    private void deployApplication(String remoteObjectName, String invokerName,String interfaceName, String classPath) {

        DeploymentDescriptor deploymentDescriptor = DeploymentDescriptor.createDeploymentDescriptor(
                remoteObjectName,
                new File(String.format("%s%s.class", classPath, interfaceName)),
                new File(String.format("%s%s.class", classPath, interfaceName)),
                new File(invokerName));

        NamingInstaller lookup = (NamingInstaller) DefaultLookup.getInstance();
        lookup.install(deploymentDescriptor);
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
        try {

            String idlPath = Objects.requireNonNull(args[0], "The IDL path should be provided.");
            String invokerName = Objects.requireNonNull(args[1], "The invoker name should be provided.");
            String remoteObjectName = Objects.requireNonNull(args[2],"The remote object name should be provided.");

            InstallationResult installationResult = ClientInstaller.getInstance().install(idlPath, remoteObjectName,invokerName);

            Class clientProxyClass = installationResult.getClientProxyClass();
            Class invokerClass = installationResult.getInvokerClass();

        } catch (InstallationException e) {
            Logger.getLogger(ClientInstaller.class.getName()).log(Level.SEVERE,e.getMessage(),e);
        }
    }

}
