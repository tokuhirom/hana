package me.geso.hana.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;;

/**
 * You don't need to use this annotation directly.
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
	String name();
}
