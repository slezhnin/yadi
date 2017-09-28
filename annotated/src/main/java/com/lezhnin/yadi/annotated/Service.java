package com.lezhnin.yadi.annotated;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Service {

    Class<?>[] implemented() default {};
    Class<?>[] dependencies() default {};
}
