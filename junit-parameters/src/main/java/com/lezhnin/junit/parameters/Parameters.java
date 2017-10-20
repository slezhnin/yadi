package com.lezhnin.junit.parameters;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import com.lezhnin.junit.parameters.provider.ValueProvider;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Parameters {

    Class<? extends ValueProvider<?>>[] value();

    int count() default 5;

    int maxSize() default 5;
}
