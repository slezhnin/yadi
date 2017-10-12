package com.lezhnin.yadi.api;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class ServiceReferenceTest {

    private static class TestClass {

    }

    @Test
    void serviceReference() {
        final String id = "TestClassId";
        final Supplier<TestClass> testClassSupplier = TestClass::new;

        final ServiceReference<TestClass> actual = ServiceReference.serviceReference(TestClass.class, id, testClassSupplier);

        assertThat(actual)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("type", TestClass.class)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("supplier", Optional.of(testClassSupplier));
    }

    @Test
    void serviceReference1() {
        final String id = "TestClassId";

        final ServiceReference<TestClass> actual = ServiceReference.serviceReference(TestClass.class, id);

        assertThat(actual)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("type", TestClass.class)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("supplier", Optional.empty());
    }

    @Test
    void serviceReference2() {
        final Supplier<TestClass> testClassSupplier = TestClass::new;

        final ServiceReference<TestClass> actual = ServiceReference.serviceReference(TestClass.class, testClassSupplier);

        assertThat(actual)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("type", TestClass.class)
                .hasFieldOrPropertyWithValue("id", TestClass.class.getCanonicalName())
                .hasFieldOrPropertyWithValue("supplier", Optional.of(testClassSupplier));
    }

    @Test
    void serviceReference3() {
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
}