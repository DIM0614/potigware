package generated;

import br.ufrn.dimap.middleware.remotting.impl.Invocation;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.String;

/**
 * Config interface for the middleware server interceptors */
public abstract class InterceptorConfigInvoker implements InterceptorConfig, Invoker {
  /**
   * Starts an invocation interceptor with the specified name
   * @param name name of the interceptor
   * @return bool */
  public abstract Boolean startInvocationInterceptor(String name) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError;

  /**
   * Stops an invocation interceptor with the specified name
   * @param name name of the interceptor
   * @return bool */
  public abstract Boolean stopInvocationInterceptor(String name) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError;

  /**
   * Starts a request interceptor with the specified name
   * @param name name of the interceptor
   * @return bool */
  public abstract Boolean startRequestInterceptor(String name) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError;

  /**
   * Stops a request interceptor with the specified name
   * @param name name of the interceptor
   * @return bool */
  public abstract Boolean stopRequestInterceptor(String name) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError;

  public Object invoke(Invocation invocation) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    Object[] params = invocation.getInvocationData().getActualParams();
    if (invocation.getInvocationData().getOperationName().equals( "startInvocationInterceptor" )) {
      return startInvocationInterceptor((String) params[0] );
    }
    if (invocation.getInvocationData().getOperationName().equals( "stopInvocationInterceptor" )) {
      return stopInvocationInterceptor((String) params[0] );
    }
    if (invocation.getInvocationData().getOperationName().equals( "startRequestInterceptor" )) {
      return startRequestInterceptor((String) params[0] );
    }
    if (invocation.getInvocationData().getOperationName().equals( "stopRequestInterceptor" )) {
      return stopRequestInterceptor((String) params[0] );
    }
    return null;
  }
}
