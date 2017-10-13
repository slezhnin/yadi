package com.lezhnin.yadi.api;

import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static com.lezhnin.yadi.api.dependency.ConstructorDependency.constructor;
import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.junit.parameters.AnnotatedArgumentSource;
import com.lezhnin.junit.parameters.Parameters;
import com.lezhnin.yadi.api.dependency.Dependency;
import java.util.function.Supplier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

class ServiceDefinitionTest {

    public static class TestClass {

    }

    private static final ServiceReference<TestClass> testReference = serviceReference(TestClass.class);

    public static class DependencySupplier implements Supplier<Dependency> {

        @Override
        public Dependency get() {
            return constructor(testReference);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(value = {DependencySupplier.class, DependencySupplier.class}, count = 10)
    void serviceDefinition(final Dependency<TestClass> constructorDependency, final Dependency[] dependencies) {
        final ServiceDefinition<TestClass> actual = ServiceDefinition.serviceDefinition(testReference, constructorDependency, dependencies);

        assertThat(actual)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("reference", testReference);
    }
}