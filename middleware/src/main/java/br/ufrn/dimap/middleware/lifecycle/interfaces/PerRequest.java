package br.ufrn.dimap.middleware.lifecycle.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Per-request Annotation, to be used on objects that can be accessed by any client whenever it's needed.
 * <p>
 * Objects annotated with it will be pooled in the server accordingly to the client's needs.
 *
 * @author Gabriel Victor
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PerRequest {
    /**
     * Defines the default pool size for the annotated class.
     */
    int poolSize() default 10;
}
