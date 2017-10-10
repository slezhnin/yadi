package com.lezhnin.yadi.annotated;

import static java.util.Arrays.stream;
import com.lezhnin.yadi.api.ServiceReference;
import java.lang.reflect.Method;

public interface MethodParameters {

    default ServiceReference<?>[] methodParameters(final Method method) {
        return stream(method.getParameters())
                .map(NamedParameterReference::namedParameterReference)
                .toArray(ServiceReference<?>[]::new);
    }
}
