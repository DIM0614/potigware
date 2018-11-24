package generated;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.impl.ClientProxy;
import br.ufrn.dimap.middleware.remotting.impl.UnsyncRequestor;
import br.ufrn.dimap.middleware.remotting.interfaces.Callback;
import br.ufrn.dimap.middleware.remotting.interfaces.InvocationAsynchronyPattern;
import br.ufrn.dimap.middleware.remotting.interfaces.Requestor;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Object;

/**
 * Provides mathematical methods */
public class ClientMath extends ClientProxy implements Math {
  private Requestor r;

  public ClientMath(AbsoluteObjectReference absoluteObjectReference) {
    super(absoluteObjectReference);
    this.r = new UnsyncRequestor();
  }

  /**
   * Returns the value of pi given a precision
   * @param precision Desired precision
   * @return float */
  public Float pi(Float precision) throws br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    return (Float) r.request(absoluteObjectReference,"pi", Float.class, precision);
  }

  /**
   * Returns the value of pi given a precision
   * @param callback
   * @param precision Desired precision */
  public void pi(Float precision, Callback callback) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    r.request(absoluteObjectReference,"pi",callback, Float.class, precision);
  }

  /**
   * Returns the value of pi given a precision
   * @param invocationAsyncPattern
   * @param precision Desired precision
   * @return Object */
  public Object pi(Float precision, InvocationAsynchronyPattern invocationAsyncPattern) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    return r.request(absoluteObjectReference,"pi",invocationAsyncPattern, Float.class, precision);
  }

  /**
   * Returns the i-th element of the fibonacci sequence
   * @param start Starting number of the sequence
   * @param i Desired element
   * @return int */
  public Integer fibonacci(Integer start, Integer i) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    return (Integer) r.request(absoluteObjectReference,"fibonacci", Integer.class, start,i);
  }

  /**
   * Returns the i-th element of the fibonacci sequence
   * @param callback
   * @param start Starting number of the sequence
   * @param i Desired element */
  public void fibonacci(Integer start, Integer i, Callback callback) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    r.request(absoluteObjectReference,"fibonacci",callback, Integer.class, start,i);
  }

  /**
   * Returns the i-th element of the fibonacci sequence
   * @param invocationAsyncPattern
   * @param start Starting number of the sequence
   * @param i Desired element
   * @return Object */
  public Object fibonacci(Integer start, Integer i,
      InvocationAsynchronyPattern invocationAsyncPattern) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError {
    return r.request(absoluteObjectReference,"fibonacci",invocationAsyncPattern, Integer.class, start,i);
  }
}
