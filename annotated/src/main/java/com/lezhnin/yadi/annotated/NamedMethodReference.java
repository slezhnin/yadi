package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static java.util.Optional.ofNullable;
import com.lezhnin.yadi.api.ServiceReference;
import java.lang.reflect.Method;
import java.util.function.Function;
import javax.inject.Named;

public final class NamedMethodReference<T> implements Function<Method, ServiceReference<T>> {

    @SuppressWarnings("unchecked")
    @Override
    public ServiceReference<T> apply(final Method method) {
        final Class<T> returnType = (Class<T>) method.getReturnType();
        return ofNullable(method.getAnnotation(Named.class))
                .filter(named -> !named.value().isEmpty())
                .map(named -> serviceReference(returnType, named.value()))
                .orElseGet(() -> serviceReference(returnType));
    }

    public static  <T> ServiceReference<T> namedMethodReference(final Method namedMethod) {
        return new NamedMethodReference<T>().apply(namedMethod);
    }
}
