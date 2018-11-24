package generated;

import java.lang.Float;
import java.lang.Integer;

/**
 * Provides mathematical methods */
public interface Math {
  /**
   * Returns the value of pi given a precision
   * @param precision Desired precision
   * @return float */
  Float pi(Float precision) throws br.ufrn.dimap.middleware.remotting.impl.RemoteError;

  /**
   * Returns the i-th element of the fibonacci sequence
   * @param start Starting number of the sequence
   * @param i Desired element
   * @return int */
  Integer fibonacci(Integer start, Integer i) throws
      br.ufrn.dimap.middleware.remotting.impl.RemoteError;
}
