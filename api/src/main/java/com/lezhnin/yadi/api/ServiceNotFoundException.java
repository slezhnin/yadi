package com.lezhnin.yadi.api;

public class ServiceNotFoundException extends RuntimeException {

    public <T> ServiceNotFoundException(final Class<T> serviceBeanType, final ServiceLocator locator) {
        super("Cannot find provider for bean: \"" + serviceBeanType + "\" at locator: " + locator.getClass());
    }
}
