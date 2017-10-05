package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.api.Dependency.ConstructorDependency.constructor;
import static com.lezhnin.yadi.api.Dependency.MethodDependency.methodFromClass;
import static com.lezhnin.yadi.api.ServiceDefinition.serviceDefinition;
import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import com.lezhnin.yadi.api.Dependency;
import com.lezhnin.yadi.api.MethodNotFoundException;
import com.lezhnin.yadi.api.ServiceFinder;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceReference;
import com.lezhnin.yadi.simple.SimpleModule;
import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import org.reflections.Reflections;

public class PackageScanModule extends SimpleModule {

    private final String packagePrefix;

    protected PackageScanModule(@Nonnull final String packagePrefix, @Nonnull final ServiceFinder... parents) {
        super(parents);
        this.packagePrefix = requireNonNull(packagePrefix);
        registerAnnotations();
    }

    public static ServiceLocator fromPackage(@Nonnull final String prefix, @Nonnull final ServiceFinder... parents) {
        return new PackageScanModule(prefix, parents);
    }

    public static ServiceLocator fromPackage(@Nonnull final Package pkg, @Nonnull final ServiceFinder... parents) {
        return new PackageScanModule(pkg.getName(), parents);
    }

    @SuppressWarnings("unchecked")
    private void registerAnnotations() {
        final Reflections reflections = new Reflections(packagePrefix);
        final Set<Class<?>> namedClasses = reflections.getTypesAnnotatedWith(Named.class);
        for (final Class<?> namedClass : namedClasses) {
            final ServiceReference reference = referenceFromNamed(namedClass);
            final Constructor<?> namedConstructor = findConstructor(namedClass);
            final ServiceReference<?>[] parameters = stream(namedConstructor.getParameterTypes())
                    .map(this::referenceFromNamed).toArray(ServiceReference<?>[]::new);
            register(serviceDefinition(
                    referenceFromNamed(namedClass),
                    constructor(reference, namedConstructor, parameters),
                    findPostConstruction(namedClass)
            ));
        }
    }

    private Dependency[] findPostConstruction(final Class<?> namedClass) {
        return stream(namedClass.getMethods())
                .filter(d -> d.getDeclaredAnnotation(Inject.class) != null)
                .map(method -> methodFromClass(
                        referenceFromNamed(namedClass),
                        method,
                        stream(method.getParameterTypes()).map(this::referenceFromNamed).toArray(ServiceReference<?>[]::new))
                ).toArray(Dependency[]::new);
    }

    private Constructor<?> findConstructor(final Class<?> namedClass) {
        try {
            return stream(namedClass.getConstructors())
                    .filter(c -> c.getDeclaredAnnotation(Inject.class) != null)
                    .findFirst().orElse(namedClass.getConstructor());
        } catch (final NoSuchMethodException exception) {
            throw new MethodNotFoundException(namedClass, "constructor", new Class[0], exception);
        }
    }

    private <T> ServiceReference<T> referenceFromNamed(final Class<T> namedClass) {
        final Optional<ServiceReference<T>> ref = ofNullable(namedClass.getAnnotation(Named.class))
                .filter(named -> !named.value().isEmpty())
                .map(n -> serviceReference(namedClass, n.value()));
        return ref.orElseGet(() -> serviceReference(namedClass));
    }
}
