package generated;

import java.lang.Float;
import java.lang.Integer;
import java.lang.Void;

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

  /**
   * Divides two integers
   * @param a numerator
   * @param b denominator
   * @return void */
  Void div(Integer a, Integer b) throws br.ufrn.dimap.middleware.remotting.impl.RemoteError;

  /**
   * sorts an array of integers 
   * @param vet array to be sorted
   * @return int[] */
  Integer[] sort(Integer[] vet) throws br.ufrn.dimap.middleware.remotting.impl.RemoteError;
}
