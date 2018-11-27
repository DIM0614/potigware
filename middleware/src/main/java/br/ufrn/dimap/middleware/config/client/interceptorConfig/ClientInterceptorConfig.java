package br.ufrn.dimap.middleware.config.client.interceptorConfig;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.impl.ClientProxy;
import br.ufrn.dimap.middleware.remotting.impl.UnsyncRequestor;
import br.ufrn.dimap.middleware.remotting.interfaces.Callback;
import br.ufrn.dimap.middleware.remotting.interfaces.InvocationAsynchronyPattern;
import br.ufrn.dimap.middleware.remotting.interfaces.Requestor;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.String;

/**
 * Config interface for the middleware server interceptors */
public class ClientInterceptorConfig extends ClientProxy implements InterceptorConfig {
  private Requestor r;

  public ClientInterceptorConfig(AbsoluteObjectReference absoluteObjectReference) {
    super(absoluteObjectReference);
    this.r = new UnsyncRequestor();
  }

  /**
   * Starts an invocation interceptor with the specified name
   * @param name name of the interceptor
   * @return bool */
  public Boolean startInvocationInterceptor(String name) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    return (Boolean) r.request(absoluteObjectReference,"startInvocationInterceptor", Boolean.class, (Object) name);
  }

  /**
   * Starts an invocation interceptor with the specified name
   * @param callback
   * @param name name of the interceptor */
  public void startInvocationInterceptor(String name, Callback callback) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    r.request(absoluteObjectReference,"startInvocationInterceptor",callback, Boolean.class, (Object) name);
  }

  /**
   * Starts an invocation interceptor with the specified name
   * @param invocationAsyncPattern
   * @param name name of the interceptor
   * @return Object */
  public Object startInvocationInterceptor(String name,
      InvocationAsynchronyPattern invocationAsyncPattern) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    return r.request(absoluteObjectReference,"startInvocationInterceptor",invocationAsyncPattern, Boolean.class, (Object) name);
  }

  /**
   * Stops an invocation interceptor with the specified name
   * @param name name of the interceptor
   * @return bool */
  public Boolean stopInvocationInterceptor(String name) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    return (Boolean) r.request(absoluteObjectReference,"stopInvocationInterceptor", Boolean.class, (Object) name);
  }

  /**
   * Stops an invocation interceptor with the specified name
   * @param callback
   * @param name name of the interceptor */
  public void stopInvocationInterceptor(String name, Callback callback) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    r.request(absoluteObjectReference,"stopInvocationInterceptor",callback, Boolean.class, (Object) name);
  }

  /**
   * Stops an invocation interceptor with the specified name
   * @param invocationAsyncPattern
   * @param name name of the interceptor
   * @return Object */
  public Object stopInvocationInterceptor(String name,
      InvocationAsynchronyPattern invocationAsyncPattern) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    return r.request(absoluteObjectReference,"stopInvocationInterceptor",invocationAsyncPattern, Boolean.class, (Object) name);
  }

  /**
   * Starts a request interceptor with the specified name
   * @param name name of the interceptor
   * @return bool */
  public Boolean startRequestInterceptor(String name) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    return (Boolean) r.request(absoluteObjectReference,"startRequestInterceptor", Boolean.class, (Object) name);
  }

  /**
   * Starts a request interceptor with the specified name
   * @param callback
   * @param name name of the interceptor */
  public void startRequestInterceptor(String name, Callback callback) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    r.request(absoluteObjectReference,"startRequestInterceptor",callback, Boolean.class, (Object) name);
  }

  /**
   * Starts a request interceptor with the specified name
   * @param invocationAsyncPattern
   * @param name name of the interceptor
   * @return Object */
  public Object startRequestInterceptor(String name,
      InvocationAsynchronyPattern invocationAsyncPattern) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    return r.request(absoluteObjectReference,"startRequestInterceptor",invocationAsyncPattern, Boolean.class, (Object) name);
  }

  /**
   * Stops a request interceptor with the specified name
   * @param name name of the interceptor
   * @return bool */
  public Boolean stopRequestInterceptor(String name) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    return (Boolean) r.request(absoluteObjectReference,"stopRequestInterceptor", Boolean.class, (Object) name);
  }

  /**
   * Stops a request interceptor with the specified name
   * @param callback
   * @param name name of the interceptor */
  public void stopRequestInterceptor(String name, Callback callback) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    r.request(absoluteObjectReference,"stopRequestInterceptor",callback, Boolean.class, (Object) name);
  }

  /**
   * Stops a request interceptor with the specified name
   * @param invocationAsyncPattern
   * @param name name of the interceptor
   * @return Object */
  public Object stopRequestInterceptor(String name,
      InvocationAsynchronyPattern invocationAsyncPattern) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    return r.request(absoluteObjectReference,"stopRequestInterceptor",invocationAsyncPattern, Boolean.class, (Object) name);
  }
}
