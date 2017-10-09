package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.annotated.NamedClassReference.namedClassReference;
import static com.lezhnin.yadi.api.Dependency.MethodDependency.methodFromClass;
import static java.util.Arrays.stream;
import com.lezhnin.yadi.api.Dependency;
import com.lezhnin.yadi.api.ServiceReference;
import java.util.function.Function;
import javax.inject.Inject;

public final class PostConstructDependencyFinder implements Function<Class<?>, Dependency[]> {

    @Override
    public Dependency[] apply(final Class<?> someClass) {
        return stream(someClass.getMethods())
                .filter(method -> method.getDeclaredAnnotation(Inject.class) != null)
                .map(method -> methodFromClass(
                        namedClassReference(someClass),
                        method,
                        stream(method.getParameterTypes())
                                .map(NamedClassReference::namedClassReference)
                                .toArray(ServiceReference<?>[]::new)
                        )
                ).toArray(Dependency[]::new);
    }
}
