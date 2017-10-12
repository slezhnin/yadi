package com.lezhnin.yadi.api.exception;

import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceReference;

public class ServiceNotFoundException extends RuntimeException {

    public <T> ServiceNotFoundException(final ServiceReference<T> reference, final ServiceLocator locator) {
        super("Cannot find service: \"" + reference + "\" in registry: " + locator);
    }
}
