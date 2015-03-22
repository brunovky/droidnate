package com.brunooliveira.droidnate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies the field with this annotation can not be null in the database. 
 * If this field is null, it returns {@link com.brunooliveira.droidnate.exception.DroidnateException}, with information on {@code errorMessage()}.
 *
 * <p>{@code errorMessage()} should be used to set the error message reported to the system. By default, this value is "{0} can't be null", where "{0}" is the field name with this annotation.
 *
 * <p><strong>example:</strong></p>
 * {@code @NotNull(errorMessage = "Sorry! Username can't be null...")}
 * <br>{@code private String username;}
 * <br><br>
 *
 * @author	Bruno Oliveira
 * @since	v.1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface Validator {

    Class<? extends com.brunooliveira.droidnate.validator.Validator> validatorClass();
	
	String errorMessage() default "Validator invalid";

}