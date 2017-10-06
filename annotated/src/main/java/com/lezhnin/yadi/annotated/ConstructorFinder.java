package com.lezhnin.yadi.annotated;

import static java.util.Arrays.stream;
import com.lezhnin.yadi.api.MethodNotFoundException;
import java.lang.reflect.Constructor;
import java.util.function.Function;
import javax.inject.Inject;

public final class ConstructorFinder implements Function<Class<?>, Constructor<?>> {

    @Override
    public Constructor<?> apply(final Class<?> someClass) {
        try {
            return stream(someClass.getConstructors())
                    .filter(c -> c.getDeclaredAnnotation(Inject.class) != null)
                    .findFirst()
                    .orElseGet(() -> someClass.getConstructors()[0]);
        } catch (final Exception exception) {
            throw new MethodNotFoundException(someClass, "constructor", new Class[0], exception);
        }
    }
}
