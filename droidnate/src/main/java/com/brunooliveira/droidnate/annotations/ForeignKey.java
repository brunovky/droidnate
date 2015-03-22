package com.brunooliveira.droidnate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies that the object with this annotation is a representation of foreign key from the corresponding table. 
 * 
 * <p>{@code fieldName()} should be used to indicate the foreign key name in the corresponding table. By default, this value is the object name with this annotation + suffix "_id".
 * 
 * <p>And {@code objectField()} should be used to indicate the object field name with this annotation that represent the primary key of this table. By default, this value looks for a field from this object called "id".
 * 
 * <p><strong>example:</strong></p>
 * {@code @ForeignKey(fieldName = "address_id", objectField = "id")}
 * <br>{@code private Address address;}
 * <br><br>
 * 
 * @author	Bruno Oliveira
 * @since	v.1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface ForeignKey {
	
	public String fieldName() default "";
	
	public String objectField() default "id";

}