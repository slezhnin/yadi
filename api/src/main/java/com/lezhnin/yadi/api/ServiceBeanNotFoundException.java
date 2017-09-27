package com.lezhnin.yadi.api;

public class ServiceBeanNotFoundException extends RuntimeException {

    public <T> ServiceBeanNotFoundException(final Class<T> serviceBeanType, final ServiceBeanLocator locator) {
        super("Cannot find provider for bean: \"" + serviceBeanType + "\" at locator: " + locator.getClass());
    }
}
