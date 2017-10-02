package com.lezhnin.yadi.simple;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import javax.annotation.Nonnull;

public class MethodDependencyException extends RuntimeException {

    public MethodDependencyException(@Nonnull final Class<?> dependency,
                                     @Nonnull final String method,
                                     @Nonnull final Class<?>[] dependencies,
                                     @Nonnull final Throwable cause) {
        super(format("Method {} ({}) is not found in {}", method, asList(dependencies), dependency), cause);
    }
}
