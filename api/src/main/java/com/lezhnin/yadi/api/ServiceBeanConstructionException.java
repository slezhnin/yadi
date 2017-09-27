package com.lezhnin.yadi.api;

public class ServiceBeanConstructionException extends RuntimeException {

    public ServiceBeanConstructionException(final Class<?> constructedClass, final Throwable cause) {
        super("Cannot construct simple service bean provider class: " + constructedClass, cause);
    }
}
