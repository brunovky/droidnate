package com.brunooliveira.droidnate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies the class with this annotation is a representation of an entity in database.
 * <p>{@code name()} must be used when the table name in database is different from the class name representing the entity. By default, this value is the class name with this annotation.
 * 
 * <p><strong>example:</strong></p>
 * {@code @Entity(name = "tb_user")}
 * <br>{@code public class User}
 * <br><br>
 * 
 * @author	Bruno Oliveira
 * @since	v.1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface Entity {

	public String name() default "";

}