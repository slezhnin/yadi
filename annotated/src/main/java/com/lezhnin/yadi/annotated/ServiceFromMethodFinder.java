package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.annotated.NamedClassReference.namedClassReference;
import static com.lezhnin.yadi.annotated.NamedMethodReference.namedMethodReference;
import static com.lezhnin.yadi.api.Dependency.MethodDependency.methodFromClass;
import static com.lezhnin.yadi.api.ServiceDefinition.serviceDefinition;
import static java.util.Arrays.stream;
import com.lezhnin.yadi.api.ServiceDefinition;
import java.util.function.Function;
import javax.inject.Named;

public final class ServiceFromMethodFinder implements Function<Class<?>, ServiceDefinition<?>[]>, MethodParameters {

    @Override
    public ServiceDefinition<?>[] apply(final Class<?> someClass) {
        return stream(someClass.getMethods())
                .filter(method -> method.getDeclaredAnnotation(Named.class) != null)
                .map(method -> serviceDefinition(
                        namedMethodReference(method),
                        methodFromClass(
                                namedClassReference(someClass),
                                namedMethodReference(method),
                                method,
                                methodParameters(method)
                        ))
                ).toArray(ServiceDefinition<?>[]::new);
    }
}
