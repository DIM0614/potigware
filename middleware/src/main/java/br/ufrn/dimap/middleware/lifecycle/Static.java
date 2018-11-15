package br.ufrn.dimap.middleware.lifecycle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Static Annotation, to be used on statictest objects that can be accessed by any client.
 * <p>
 * Objects annotated with it are initializated at server start up and it's attributes cannot be
 * changed by clients. These objects are also destructed on server shut down.
 *
 * @author Gilberto Soares
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Static {
    /**
     * Defines the load priority for the annotated class. Higher numbers are loaded first.
     */
    int priority() default 10;

    /**
     * Defines the initialization method for the annotated class.
     *
     * @return the method name.
     */
    String method() default "getInstance";
}
