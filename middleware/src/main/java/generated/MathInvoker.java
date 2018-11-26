package generated;

import br.ufrn.dimap.middleware.remotting.impl.Invocation;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Object;

/**
 * Provides mathematical methods */
public abstract class MathInvoker implements Math, Invoker {
  /**
   * Returns the value of pi given a precision
   * @param precision Desired precision
   * @return float */
  public abstract Float pi(Float precision) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError;

  /**
   * Returns the i-th element of the fibonacci sequence
   * @param start Starting number of the sequence
   * @param i Desired element
   * @return int */
  public abstract Integer fibonacci(Integer start, Integer i) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError;

  public Object invoke(Invocation invocation) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    Object[] params = invocation.getInvocationData().getActualParams();
    if (invocation.getInvocationData().getOperationName().equals( "pi" )) {
      return pi((Float) params[0] );
    }
    if (invocation.getInvocationData().getOperationName().equals( "fibonacci" )) {
      return fibonacci((Integer) params[0], (Integer) params[1] );
    }
    return null;
  }
}
