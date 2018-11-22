/**
 * 
 */
package br.ufrn.dimap.middleware.lifecycle.interfaces;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates methods to be used to activate remote objects
 * with per request activation
 * 
 * @author victoragnez
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Activate {

}
