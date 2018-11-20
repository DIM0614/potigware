package br.ufrn.dimap.middleware.remotting.impl;

import java.io.File;

public class DeploymentDescriptor {

	private File interf;
	private File invoker;
	private File impl;
	private String objName;

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
	
	
	private DeploymentDescriptor(String name, File interf, File invoker, File impl) {
		super();
		this.interf = interf;
		this.invoker = invoker;
		this.impl = impl;
		this.objName = name;
	}
	public File getInterf() {
		return interf;
	}
	public void setInterf(File interf) {
		this.interf = interf;
	}
	public File getInvoker() {
		return invoker;
	}
	public void setInvoker(File invoker) {
		this.invoker = invoker;
	}
	public File getImpl() {
		return impl;
	}
	public void setImpl(File impl) {
		this.impl = impl;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	
}
