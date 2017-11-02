package com.lezhnin.yadi.api;

import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static com.lezhnin.yadi.api.dependency.ConstructorDependency.constructor;
import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.junit.parameters.AnnotatedArgumentSource;
import com.lezhnin.junit.parameters.Parameters;
import com.lezhnin.junit.parameters.provider.ProviderFromSupplier;
import com.lezhnin.yadi.api.dependency.Dependency;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

class ServiceDefinitionTest {

    private static final ServiceReference<TestClass> testReference = serviceReference(TestClass.class);

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(value = {DependencyProvider.class, DependencyProvider.class}, count = 10)
    void serviceDefinition(final Dependency constructorDependency, final Dependency[] dependencies) {
        final ServiceDefinition<TestClass> actual = ServiceDefinition.serviceDefinition(testReference, constructorDependency, dependencies);

        assertThat(actual)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("reference", testReference);
    }

    public static class TestClass {

    }

    public static class DependencyProvider extends ProviderFromSupplier<Dependency> {

        public DependencyProvider() {
            super(() -> constructor(testReference), Dependency.class);
        }
    }
}