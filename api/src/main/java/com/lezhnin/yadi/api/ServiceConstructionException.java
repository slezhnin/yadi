package com.lezhnin.yadi.api;

public class ServiceConstructionException extends RuntimeException {

    public ServiceConstructionException(final Class<?> constructedClass, final Throwable cause) {
        super("Cannot construct service class: " + constructedClass, cause);
    }
}
