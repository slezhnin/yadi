package com.lezhnin.yadi.api;

public class ServiceNotFoundException extends RuntimeException {

    public <T> ServiceNotFoundException(final ServiceReference<T> reference, final ServiceLocator locator) {
        super("Cannot find service: \"" + reference + "\" in registry: " + locator);
    }
}
