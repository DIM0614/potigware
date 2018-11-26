package generated;

import java.lang.Boolean;
import java.lang.String;

/**
 * Config interface for the middleware server interceptors */
public interface InterceptorConfig {
  /**
   * Starts an invocation interceptor with the specified name
   * @param name name of the interceptor
   * @return bool */
  Boolean startInvocationInterceptor(String name) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError;

  /**
   * Stops an invocation interceptor with the specified name
   * @param name name of the interceptor
   * @return bool */
  Boolean stopInvocationInterceptor(String name) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError;

  /**
   * Starts a request interceptor with the specified name
   * @param name name of the interceptor
   * @return bool */
  Boolean startRequestInterceptor(String name) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError;

  /**
   * Stops a request interceptor with the specified name
   * @param name name of the interceptor
   * @return bool */
  Boolean stopRequestInterceptor(String name) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError;
}
