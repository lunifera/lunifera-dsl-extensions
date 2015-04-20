package org.lunifera.dsl.ext.cpp.qt.lib.types.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface Index {

	String value() default "";
}
