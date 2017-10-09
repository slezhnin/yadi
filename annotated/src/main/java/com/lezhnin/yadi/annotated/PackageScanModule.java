package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.annotated.NamedClassReference.namedClassReference;
import static com.lezhnin.yadi.api.Dependency.ConstructorDependency.constructor;
import static com.lezhnin.yadi.api.ServiceDefinition.serviceDefinition;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import com.lezhnin.yadi.api.Dependency;
import com.lezhnin.yadi.api.ServiceDefinition;
import com.lezhnin.yadi.api.ServiceFinder;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.api.ServiceReference;
import com.lezhnin.yadi.simple.SimpleModule;
import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.inject.Named;
import org.reflections.Reflections;

public class PackageScanModule extends SimpleModule {

    private final String packagePrefix;
    private final Function<Class<?>, Constructor<?>> constructorFinder;
    private final Function<Class<?>, Dependency[]> postConstructDependencyFinder;
    private final Function<Class<?>, ServiceDefinition<?>[]> serviceFromMethodFinder;

    protected PackageScanModule(@Nonnull final String packagePrefix,
                                final Function<Class<?>, Constructor<?>> constructorFinder,
                                final Function<Class<?>, Dependency[]> postConstructDependencyFinder,
                                final Function<Class<?>, ServiceDefinition<?>[]> serviceFromMethodFinder, @Nonnull final ServiceFinder... parents) {
        super(parents);
        this.packagePrefix = requireNonNull(packagePrefix);
        this.constructorFinder = constructorFinder;
        this.postConstructDependencyFinder = postConstructDependencyFinder;
        this.serviceFromMethodFinder = serviceFromMethodFinder;
        registerAnnotations();
    }

    public static ServiceLocator fromPackage(@Nonnull final String prefix, @Nonnull final ServiceFinder... parents) {
        return new PackageScanModule(
                prefix,
                new ConstructorFinder(),
                new PostConstructDependencyFinder(),
                new ServiceFromMethodFinder(),
                parents
        );
    }

    public static ServiceLocator fromPackage(@Nonnull final Package pkg, @Nonnull final ServiceFinder... parents) {
        return new PackageScanModule(
                pkg.getName(),
                new ConstructorFinder(),
                new PostConstructDependencyFinder(),
                new ServiceFromMethodFinder(),
                parents
        );
    }

    @SuppressWarnings("unchecked")
    private void registerAnnotations() {
        final Reflections reflections = new Reflections(packagePrefix);
        final Set<Class<?>> namedClasses = reflections.getTypesAnnotatedWith(Named.class);
        for (final Class<?> namedClass : namedClasses) {
            final ServiceReference reference = namedClassReference(namedClass);
            final Constructor<?> namedConstructor = constructorFinder.apply(namedClass);
            final ServiceReference<?>[] parameters = stream(namedConstructor.getParameterTypes())
                    .map(NamedClassReference::namedClassReference)
                    .toArray(ServiceReference<?>[]::new);
            accept(serviceDefinition(
                    namedClassReference(namedClass),
                    constructor(reference, namedConstructor, parameters),
                    postConstructDependencyFinder.apply(namedClass)
            ));
            stream(serviceFromMethodFinder.apply(namedClass)).forEach(this);
        }
    }
}
