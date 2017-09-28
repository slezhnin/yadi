package com.lezhnin.yadi.annotated;

import static com.lezhnin.yadi.annotated.PackageScanModule.fromPackage;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.lezhnin.yadi.api.ServiceBeanLocator;
import org.junit.jupiter.api.Test;

class PackageScanModuleTest {

    @ServiceBean
    public static class B {

    }

    @ServiceBean
    public static class C {

    }

    @ServiceBean(dependencies = {B.class, C.class})
    public static class A {

        private final B b;
        private final C c;

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

    @Test
    void testFromPackage() {
        ServiceBeanLocator module = fromPackage(getClass().getPackage());

        final A actual = module.locate(A.class);

        assertNotNull(actual);
        assertNotNull(actual.getB());
        assertNotNull(actual.getC());
    }
}