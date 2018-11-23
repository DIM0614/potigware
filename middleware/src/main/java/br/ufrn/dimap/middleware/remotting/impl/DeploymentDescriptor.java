package br.ufrn.dimap.middleware.remotting.impl;

import java.io.File;

/**
 * Class that represents the application to be deployed.
 *
 * @author vitorgreati
 * @author Daniel Smith
 */
public class DeploymentDescriptor {

	/**
	 * File that represents the interface file.
	 */
	private File interfaceFile;

	/**
	 * File that represents the abstract invoker file.
	 */
	private File invokerFile;

	/**
	 * File that represents the invoker implementation file.
	 */
	private File invokerImplementation;

	/**
	 * File that represents the remote object name of the artifact to be deployed.
	 *
	 */
	private String remoteObjectName;

	/**
	 * Static factory method for the deployment descriptor.
	 *
	 * @param objName
	 * @param interf
	 * @param invoker
	 * @param impl
	 * @return
	 */
	public static DeploymentDescriptor createDeploymentDescriptor(String objName, File interf, File invoker, File impl) {
		if (objName == null) {
			throw new IllegalArgumentException("Object name must not be null");
		}
		if (interf == null) {
			throw new IllegalArgumentException("Interface must not be null");
		}
		if (invoker == null) {
			throw new IllegalArgumentException("Invoker must not be null");
		}
		if (impl == null) {
			throw new IllegalArgumentException("Implementation must not be null");
		}
				
		return new DeploymentDescriptor(objName, interf, invoker, impl);		
	}

	private DeploymentDescriptor(String name, File interfaceFile, File invokerFile, File impl) {
		super();
		this.interfaceFile = interfaceFile;
		this.invokerFile = invokerFile;
		this.invokerImplementation = impl;
		this.remoteObjectName = name;
	}
	public File getInterfaceFile() {
		return interfaceFile;
	}

	public File getInvokerFile() {
		return invokerFile;
	}

	public File getInvokerImplementation() {
		return invokerImplementation;
	}

	public String getRemoteObjectName() {
		return remoteObjectName;
	}

}
