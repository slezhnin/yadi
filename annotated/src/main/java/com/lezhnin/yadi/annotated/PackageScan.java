package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.annotated.NamedClassReference.namedClassReference;
import static com.lezhnin.yadi.api.ServiceDefinition.serviceDefinition;
import static com.lezhnin.yadi.api.dependency.ConstructorDependency.constructor;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import com.lezhnin.yadi.api.ServiceDefinition;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceReference;
import com.lezhnin.yadi.api.ServiceRegistry;
import com.lezhnin.yadi.api.dependency.Dependency;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.inject.Named;
import org.reflections.Reflections;

public class PackageScan {

    private final ServiceRegistry registry;
    private final String packagePrefix;
    private final Function<Class<?>, Constructor<?>> constructorFinder;
    private final Function<Class<?>, Dependency[]> postConstructDependencyFinder;
    private final Function<Class<?>, List<ServiceDefinition<?>>> serviceFromMethodFinder;

    protected PackageScan(@Nonnull final ServiceRegistry registry,
                          @Nonnull final String packagePrefix,
                          final Function<Class<?>, Constructor<?>> constructorFinder,
                          final Function<Class<?>, Dependency[]> postConstructDependencyFinder,
                          final Function<Class<?>, List<ServiceDefinition<?>>> serviceFromMethodFinder) {
        this.registry = requireNonNull(registry);
        this.packagePrefix = requireNonNull(packagePrefix);
        this.constructorFinder = constructorFinder;
        this.postConstructDependencyFinder = postConstructDependencyFinder;
        this.serviceFromMethodFinder = serviceFromMethodFinder;
        registerAnnotations();
    }

    public static Optional<ServiceLocator> scanPackage(@Nonnull final ServiceRegistry registry, @Nonnull final String prefix) {
        new PackageScan(registry, prefix,
                new ConstructorFinder(),
                new PostConstructDependencyFinder(),
                new ServiceFromMethodFinder()
        );
        return registry instanceof ServiceLocator ? of((ServiceLocator) registry) : empty();
    }

    public static Optional<ServiceLocator> scanPackage(@Nonnull final ServiceRegistry registry, @Nonnull final Package pkg) {
        return scanPackage(registry, pkg.getName());
    }

    private void registerAnnotations() {
        new Reflections(packagePrefix).getTypesAnnotatedWith(Named.class).stream()
                                      .map(this::toServiceDefinition)
                                      .peek(d -> serviceFromMethodFinder.apply(d.getReference().getType()).forEach(registry))
                                      .forEach(registry);
    }

    @SuppressWarnings("unchecked")
    private <T> ServiceDefinition<T> toServiceDefinition(final Class<T> namedClass) {
        final ServiceReference<T> reference = namedClassReference(namedClass);
        final Constructor<T> namedConstructor = (Constructor<T>) constructorFinder.apply(namedClass);
        final ServiceReference<?>[] parameters = stream(namedConstructor.getParameterTypes())
                .map(NamedClassReference::namedClassReference)
                .toArray(ServiceReference<?>[]::new);
        return serviceDefinition(
                reference,
                constructor(reference, namedConstructor, parameters),
                postConstructDependencyFinder.apply(namedClass)
        );
    }
}
