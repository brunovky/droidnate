package com.brunooliveira.droidnate.ws.annotations;

import com.brunooliveira.droidnate.ws.enums.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Produces {
	
	public MediaType value() default MediaType.JSON;

}