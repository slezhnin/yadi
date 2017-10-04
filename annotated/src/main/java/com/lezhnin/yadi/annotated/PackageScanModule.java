package com.lezhnin.yadi.annotated;

import com.lezhnin.yadi.api.*;
import com.lezhnin.yadi.simple.SimpleModule;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.Set;

import static com.lezhnin.yadi.api.Dependency.ConstructorDependency.constructor;
import static com.lezhnin.yadi.api.ServiceDefinition.service;
import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

public class PackageScanModule extends SimpleModule {

    private final String packagePrefix;

    protected PackageScanModule(@Nonnull final String packagePrefix, @Nonnull ServiceFinder... parents) {
        super(parents);
        this.packagePrefix = requireNonNull(packagePrefix);
        registerAnnotations();
    }

    public static ServiceLocator fromPackage(@Nonnull String prefix, @Nonnull ServiceFinder... parents) {
        return new PackageScanModule(prefix, parents);
    }

    public static ServiceLocator fromPackage(@Nonnull Package pkg, @Nonnull ServiceFinder... parents) {
        return new PackageScanModule(pkg.getName(), parents);
    }

    private void registerAnnotations() {
        final Reflections reflections = new Reflections(packagePrefix);
        final Set<Class<?>> namedClasses = reflections.getTypesAnnotatedWith(Named.class);
        for (Class<?> namedClass : namedClasses) {
            register(service(
                    referenceFromNamed(namedClass),
                    constructor(findConstructor(namedClass)),
                    findPostConstruction(namedClass)
            ));
        }
    }

    private Dependency[] findPostConstruction(Class<?> namedClass) {
        return stream(namedClass.getMethods())
                .filter(d -> d.getDeclaredAnnotation(Inject.class) != null)
                .map(Dependency.MethodDependency::methodFromClass)
                .toArray(Dependency[]::new);
    }

    private Constructor<?> findConstructor(Class<?> namedClass) {
        try {
            return stream(namedClass.getConstructors())
                    .filter(c -> c.getDeclaredAnnotation(Inject.class) != null)
                    .findFirst().orElse(namedClass.getConstructor());
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundException(namedClass, "constructor", new Class[0], e);
        }
    }

    private <T> ServiceReference<T> referenceFromNamed(final Class<T> namedClass) {
        return Optional.ofNullable(namedClass.getAnnotation(Named.class))
                .filter(named -> !named.value().isEmpty())
                .map(n -> serviceReference(namedClass, n.value()))
                .orElse(serviceReference(namedClass));
    }
}
