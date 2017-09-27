package com.lezhnin.yadi.simple;

import static com.lezhnin.yadi.simple.SimpleModule.simpleModule;
import static com.lezhnin.yadi.simple.SimpleServiceBeanProvider.provider;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.lezhnin.yadi.api.ServiceBeanModule;
import org.junit.jupiter.api.Test;

class SimpleModuleTest {

    public static class B {

    }

    public static class C {

    }

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
    void test1() {
        final ServiceBeanModule parent = new SimpleModule() {
            @Override
            protected void doBind() {
                bind(B.class).to(provider(B.class));
            }
        };

        final ServiceBeanModule module = new SimpleModule(parent) {
            @Override
            protected void doBind() {
                bind(A.class).to(provider(A.class, B.class, C.class));
                bind(C.class).to(provider(C.class));
            }
        };

        final A actual = module.locate(A.class);

        assertNotNull(actual);
        assertNotNull(actual.getB());
        assertNotNull(actual.getC());
    }

    @Test
    void test2() {
        final ServiceBeanModule parent = simpleModule();
        parent.bind(B.class).to(provider(B.class));

        final ServiceBeanModule module = simpleModule(parent);
        module.bind(A.class).to(provider(A.class, B.class, C.class))
              .bind(C.class).to(provider(C.class));

        final A actual = module.locate(A.class);

        assertNotNull(actual);
        assertNotNull(actual.getB());
        assertNotNull(actual.getC());
    }
}