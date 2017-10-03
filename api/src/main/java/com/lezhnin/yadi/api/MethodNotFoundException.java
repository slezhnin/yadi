package com.lezhnin.yadi.api;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import javax.annotation.Nonnull;

public class MethodNotFoundException extends RuntimeException {

    public MethodNotFoundException(@Nonnull final Class<?> type,
                                   @Nonnull final String method,
                                   @Nonnull final Class<?>[] parameters,
                                   @Nonnull final Throwable cause) {
        super(format("Method %s (%s) is not found in %s", method, asList(parameters), type), cause);
    }

    public MethodNotFoundException(@Nonnull final Class<?> type,
                                   @Nonnull final String method,
                                   @Nonnull final Class<?>[] parameters) {
        super(format("Method %s (%s) is not found in %s", method, asList(parameters), type));
    }
}
