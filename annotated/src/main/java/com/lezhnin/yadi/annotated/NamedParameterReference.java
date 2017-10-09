package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static java.util.Optional.ofNullable;
import com.lezhnin.yadi.api.ServiceReference;
import java.lang.reflect.Parameter;
import java.util.function.Function;
import javax.inject.Named;

public final class NamedParameterReference<T> implements Function<Parameter, ServiceReference<T>> {

    @SuppressWarnings("unchecked")
    @Override
    public ServiceReference<T> apply(final Parameter parameter) {
        final Class<T> parameterType = (Class<T>) parameter.getType();
        return ofNullable(parameter.getAnnotation(Named.class))
                .filter(named -> !named.value().isEmpty())
                .map(named -> serviceReference(parameterType, named.value()))
                .orElseGet(() -> serviceReference(parameterType));
    }

    public static <T> ServiceReference<T> namedParameterReference(final Parameter namedParameter) {
        return new NamedParameterReference<T>().apply(namedParameter);
    }
}
