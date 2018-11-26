package br.ufrn.dimap.middleware.config.client.interceptorConfig;

import br.ufrn.dimap.middleware.MiddlewareConfig;
import br.ufrn.dimap.middleware.MiddlewareConfigException;

public class InterceptorConfigImpl extends InterceptorConfigInvoker implements InterceptorConfig {
	
	@Override
	public Boolean startInvocationInterceptor(String name) {
		try {
			MiddlewareConfig.Interceptors.getInstance().startServerInvocationInterceptor(name);
		} catch (MiddlewareConfigException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public Boolean stopInvocationInterceptor(String name) {
		try {
			MiddlewareConfig.Interceptors.getInstance().stopServerInvocationInterceptor(name);
		} catch (MiddlewareConfigException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public Boolean startRequestInterceptor(String name) {
		try {
			MiddlewareConfig.Interceptors.getInstance().startServerRequestInterceptor(name);
		} catch (MiddlewareConfigException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public Boolean stopRequestInterceptor(String name) {
		try {
			MiddlewareConfig.Interceptors.getInstance().stopServerRequestInterceptor(name);
		} catch (MiddlewareConfigException e) {
			return false;
		}
		return true;
	}
}
