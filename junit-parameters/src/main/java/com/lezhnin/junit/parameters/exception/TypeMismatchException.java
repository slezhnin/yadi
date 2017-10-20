package com.lezhnin.junit.parameters.exception;

import static java.lang.String.format;

public class TypeMismatchException extends RuntimeException {

    public TypeMismatchException(final Class<?> provided, final Class<?> actual) {
        super(format("Provided %s is not assignable from actual %s", provided, actual));
    }
}
