package com.lezhnin.yadi.api;

public class ServiceNotFoundException extends RuntimeException {

    public <T> ServiceNotFoundException(final ServiceReference<T> reference, final ServiceRegistry registry) {
        super("Cannot find service: \"" + reference + "\" in registry: " + registry);
    }
}
