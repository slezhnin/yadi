package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.annotated.PackageScanModule.fromPackage;
import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.yadi.api.ServiceLocator;
import javax.inject.Inject;
import javax.inject.Named;

import com.lezhnin.yadi.api.ServiceReference;
import org.junit.jupiter.api.Test;

class PackageScanModuleTest {

    @Test
    void testFromPackage() {
        ServiceLocator module = fromPackage(getClass().getPackage());

        final A actual = module.locate(serviceReference(A.class)).get();

        assertThat(actual).isNotNull();
        assertThat(actual.getB()).isNotNull();
        assertThat(actual.getC()).isNotNull();
        assertThat(actual.getC().getB()).isNotNull();
    }

    @Named
    public static class B {

    }

    @Named
    public static class C {

        private B b;

        public B getB() {
            return b;
        }

        @Inject
        public void setB(final B b) {
            this.b = b;
        }
    }

    @Named
    public static class A {

        private final B b;
        private final C c;

        @Inject
        public A(final B b, final C c) {
            this.b = b;
            this.c = c;
        }

        B getB() {
            return b;
        }

        C getC() {
            return c;
        }
    }
}