package com.lezhnin.yadi.api;

import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.junit.parameters.AnnotatedArgumentSource;
import com.lezhnin.junit.parameters.Parameters;
import com.lezhnin.junit.parameters.provider.Mock;
import com.lezhnin.junit.parameters.provider.ProviderFromSupplier;
import com.lezhnin.junit.parameters.provider.random.RandomUUIDString;
import java.util.Optional;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

class ServiceReferenceTest {

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters({RandomUUIDString.class, TestClassProvider.class})
    void serviceReferenceFromIdAndSupplier(final String id, final Supplier<TestClass> testClassSupplier) {
        final ServiceReference<TestClass> actual = ServiceReference.serviceReference(TestClass.class, id, testClassSupplier);

        assertThat(actual)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("type", TestClass.class)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("supplier", Optional.of(testClassSupplier));
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomUUIDString.class)
    void serviceReferenceFromId(final String id) {
        final ServiceReference<TestClass> actual = ServiceReference.serviceReference(TestClass.class, id);

        assertThat(actual)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("type", TestClass.class)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("supplier", Optional.empty());
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(Mock.class)
    void serviceReferenceFromSupplier(final Supplier<TestClass> testClassSupplier) {
        final ServiceReference<TestClass> actual = ServiceReference.serviceReference(TestClass.class, testClassSupplier);

        assertThat(actual)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("type", TestClass.class)
                .hasFieldOrPropertyWithValue("id", TestClass.class.getCanonicalName())
                .hasFieldOrPropertyWithValue("supplier", Optional.of(testClassSupplier));
    }

    @Test
    void serviceReferenceFromClass() {
        final ServiceReference<TestClass> actual = ServiceReference.serviceReference(TestClass.class);

        assertThat(actual)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("type", TestClass.class)
                .hasFieldOrPropertyWithValue("id", TestClass.class.getCanonicalName())
                .hasFieldOrPropertyWithValue("supplier", Optional.empty());
    }

    @Test
    void fromTypes() {
        final ServiceReference<?>[] actual = ServiceReference.fromTypes(TestClass.class);

        assertThat(actual).hasSize(1).contains(ServiceReference.serviceReference(TestClass.class));
    }

    @Test
    void toTypes() {
        final Class<?>[] actual = ServiceReference.toTypes(ServiceReference.serviceReference(TestClass.class));

        assertThat(actual).hasSize(1).contains(TestClass.class);
    }

    private static class TestClass {

    }

    public static class TestClassProvider extends ProviderFromSupplier<Supplier<TestClass>> {

        public TestClassProvider() {
            super(() -> TestClass::new, Supplier.class);
        }
    }
}