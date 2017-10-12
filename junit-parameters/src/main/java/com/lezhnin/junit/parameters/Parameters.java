package com.lezhnin.junit.parameters;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.Supplier;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Parameters {

    Class<? extends Supplier<?>>[] value();

    int count() default 5;
}
