package com.brunooliveira.droidnate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Set some variables from the representation column.
 * <p>{@code name()} must be used when the column name in database is different from its representation in class. By default, this value is the exact field name for their representation.
 * 
 * <p><strong>example:</strong></p>
 * {@code @Column(name = "date_of_birth")}
 * <br>{@code private Calendar dateOfBirth;}
 * 
 * <p>And {@code generatedValue()} should be used when the field with this annotation is an auto incremented field in the corresponding table. By default, this value is {@code false}.
 * 
 * <p><strong>example:</strong></p>
 * {@code @Column(generatedValue = true)}
 * <br>{@code private Long id;}
 * <br><br>
 * 
 * @author	Bruno Oliveira
 * @since	v.1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface Column {
	
	String name() default "";

    int length() default 200;

}