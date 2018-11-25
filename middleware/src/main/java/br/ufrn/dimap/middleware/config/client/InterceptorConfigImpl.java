package br.ufrn.dimap.middleware.config.client;

import br.ufrn.dimap.middleware.MiddlewareConfig;
import br.ufrn.dimap.middleware.MiddlewareConfigException;

public class InterceptorConfigImpl {
	public boolean startInvocationInterceptor(String name) {
		try {
			MiddlewareConfig.Interceptors.getInstance().startServerInvocationInterceptor(name);
		} catch (MiddlewareConfigException e) {
			return false;
		}
		return true;
	}
	
	public boolean stopInvocationInterceptor(String name) {
		try {
			MiddlewareConfig.Interceptors.getInstance().stopServerInvocationInterceptor(name);
		} catch (MiddlewareConfigException e) {
			return false;
		}
		return true;
	}
	
	public boolean startRequestInterceptor(String name) {
		try {
			MiddlewareConfig.Interceptors.getInstance().startServerRequestInterceptor(name);
		} catch (MiddlewareConfigException e) {
			return false;
		}
		return true;
	}
	
	public boolean stopRequestInterceptor(String name) {
		try {
			MiddlewareConfig.Interceptors.getInstance().stopServerRequestInterceptor(name);
		} catch (MiddlewareConfigException e) {
			return false;
		}
		return true;
	}
}
