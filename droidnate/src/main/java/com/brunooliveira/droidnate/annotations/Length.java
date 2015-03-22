package com.brunooliveira.droidnate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Bruno on 20/03/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface Length {

    int min();

    int max();

    String errorMessage() default "{0} length can't be lower than {1} and greater than {2}";

}