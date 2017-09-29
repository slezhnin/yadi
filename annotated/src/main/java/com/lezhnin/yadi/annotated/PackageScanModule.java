package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.simple.ConstructorDependency.constructor;
import static com.lezhnin.yadi.simple.MethodDependency.method;
import static com.lezhnin.yadi.simple.SimpleServiceProvider.provider;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;
import com.lezhnin.yadi.api.ServiceLocator;
import com.lezhnin.yadi.simple.ServiceDependency;
import com.lezhnin.yadi.simple.SimpleModule;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import org.reflections.Reflections;

public class PackageScanModule extends SimpleModule {

    private final String packagePrefix;

    protected PackageScanModule(@Nonnull final String packagePrefix, @Nonnull ServiceLocator... parents) {
        super(parents);
        this.packagePrefix = requireNonNull(packagePrefix);
    }

    public static ServiceLocator fromPackage(@Nonnull String prefix, @Nonnull ServiceLocator... parents) {
        return new PackageScanModule(prefix, parents);
    }

    public static ServiceLocator fromPackage(@Nonnull Package pkg, @Nonnull ServiceLocator... parents) {
        return new PackageScanModule(pkg.getName(), parents);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doBind() {
        final Reflections reflections = new Reflections(packagePrefix);
        final Set<Class<?>> namedClasses = reflections.getTypesAnnotatedWith(Named.class);
        for (Class<?> namedClass : namedClasses) {
            final Class<?>[] implementedServiceInterfaces = (
                    namedClass.getInterfaces().length != 0 ?
                            namedClass.getInterfaces() :
                            new Class<?>[]{namedClass}
            );
            final ServiceDependency[] dependencies = findDependencies(
                    namedClass
            );
            for (Class implementedServiceInterface : implementedServiceInterfaces) {
                bind(implementedServiceInterface).to(provider(namedClass, dependencies));
            }
        }
    }

    private ServiceDependency[] findDependencies(final Class<?> annotatedServiceClass) {
        return concat(
                concat(
                        stream(annotatedServiceClass.getConstructors())
                                .filter(c -> c.getDeclaredAnnotation(Inject.class) != null)
                                .map(c -> constructor(c.getParameterTypes())),
                        stream(annotatedServiceClass.getMethods())
                                .filter(d -> d.getDeclaredAnnotation(Inject.class) != null)
                                .map(d -> method(d, d.getParameterTypes()))
                ),
                of(constructor())
        ).toArray(ServiceDependency[]::new);
    }
}
