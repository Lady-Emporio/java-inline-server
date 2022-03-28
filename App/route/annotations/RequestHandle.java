package app.route.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import app.route.enums.METHODS;

@Retention(RUNTIME)
@Target(METHOD)
@Repeatable(RequestHandle_container.class)
public @interface RequestHandle {
	String path();
	String name() default "";
	METHODS [] methods() default {METHODS.GET};
}
