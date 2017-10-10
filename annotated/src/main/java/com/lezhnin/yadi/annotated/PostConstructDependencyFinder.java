package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.annotated.NamedClassReference.namedClassReference;
import static com.lezhnin.yadi.api.Dependency.MethodDependency.methodFromClass;
import static java.util.Arrays.stream;
import com.lezhnin.yadi.api.Dependency;
import java.util.function.Function;
import javax.inject.Inject;

public final class PostConstructDependencyFinder implements Function<Class<?>, Dependency[]>, MethodParameters {

    @Override
    public Dependency[] apply(final Class<?> someClass) {
        return stream(someClass.getMethods())
                .filter(method -> method.getDeclaredAnnotation(Inject.class) != null)
                .map(method -> methodFromClass(
                        namedClassReference(someClass),
                        namedClassReference(someClass),
                        method,
                        methodParameters(method))
                ).toArray(Dependency[]::new);
    }
}
