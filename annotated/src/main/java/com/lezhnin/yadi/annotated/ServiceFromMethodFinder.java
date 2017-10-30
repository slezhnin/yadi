package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.annotated.NamedClassReference.namedClassReference;
import static com.lezhnin.yadi.annotated.NamedMethodReference.namedMethodReference;
import static com.lezhnin.yadi.api.ServiceDefinition.serviceDefinition;
import static com.lezhnin.yadi.api.dependency.MethodDependency.methodFromClass;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import com.lezhnin.yadi.api.ServiceDefinition;
import java.util.List;
import java.util.function.Function;
import javax.inject.Named;

public final class ServiceFromMethodFinder implements Function<Class<?>, List<ServiceDefinition<?>>>, MethodParameters {

    @Override
    public List<ServiceDefinition<?>> apply(final Class<?> someClass) {
        return stream(someClass.getMethods())
                .filter(method -> method.getDeclaredAnnotation(Named.class) != null)
                .map(method -> serviceDefinition(
                        namedMethodReference(method),
                        methodFromClass(
                                namedClassReference(someClass),
                                method,
                                methodParameters(method)
                        ))
                ).collect(toList());
    }
}
