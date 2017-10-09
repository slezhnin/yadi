package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static java.util.Optional.ofNullable;
import com.lezhnin.yadi.api.ServiceReference;
import java.util.function.Function;
import javax.inject.Named;

public final class NamedClassReference<T> implements Function<Class<T>, ServiceReference<T>> {

    @Override
    public ServiceReference<T> apply(final Class<T> someClass) {
        return ofNullable(someClass.getAnnotation(Named.class))
                .filter(named -> !named.value().isEmpty())
                .map(named -> serviceReference(someClass, named.value()))
                .orElseGet(() -> serviceReference(someClass));
    }

    public static  <T> ServiceReference<T> namedClassReference(final Class<T> namedClass) {
        return new NamedClassReference<T>().apply(namedClass);
    }
}
