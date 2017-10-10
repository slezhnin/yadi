package com.lezhnin.yadi.simple;

import static com.lezhnin.yadi.api.Dependency.ConstructorDependency.constructor;
import static com.lezhnin.yadi.api.Dependency.MethodDependency.methodFromClass;
import static com.lezhnin.yadi.api.ServiceBuilder.service;
import static com.lezhnin.yadi.api.ServiceDefinition.serviceDefinition;
import static com.lezhnin.yadi.api.ServiceReference.serviceReference;
import static com.lezhnin.yadi.simple.SimpleModule.module;
import static com.lezhnin.yadi.simple.SimpleModule.moduleWithRegistration;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.yadi.api.ServiceDefinition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class SimpleModuleTest {

    @Test
    void testModuleWithSelfRegistration() {
        final SimpleModule parent = moduleWithRegistration(
                register -> register.accept(serviceDefinition(serviceReference(B.class), constructor(serviceReference(B.class))))
        );
        final SimpleModule module = moduleWithRegistration(
                register -> stream(
                        new ServiceDefinition<?>[]{
                                serviceDefinition(
                                        serviceReference(Amodule.class),
                                        constructor(serviceReference(Amodule.class))
                                ),
                                serviceDefinition(
                                        serviceReference(A.class),
                                        methodFromClass(
                                                serviceReference(Amodule.class),
                                                serviceReference(A.class),
                                                "createA",
                                                serviceReference(B.class),
                                                serviceReference(C.class)
                                        )
                                ),
                                serviceDefinition(
                                        serviceReference(C.class),
                                        constructor(serviceReference(C.class)),
                                        methodFromClass(
                                                serviceReference(C.class),
                                                "setB",
                                                serviceReference(B.class)
                                        )
                                )
                        }
                ).forEach(register),
                parent);

        final A actual = module.locate(A.class).get();

        assertThat(actual).isNotNull();
        assertThat(actual.getB()).isNotNull();
        assertThat(actual.getC()).isNotNull();
        assertThat(actual.getC().getB()).isNotNull();
    }

    @Test
    void testModule() {
        final SimpleModule parent = module();
        parent.accept(serviceDefinition(serviceReference(B.class), constructor(serviceReference(B.class))));

        final SimpleModule module = module(parent);
        module.accept(
                serviceDefinition(
                        serviceReference(A.class),
                        constructor(
                                serviceReference(A.class),
                                serviceReference(B.class),
                                serviceReference(C.class)
                        )
                )
        );
        module.accept(
                serviceDefinition(
                        serviceReference(C.class),
                        constructor(serviceReference(C.class)),
                        methodFromClass(
                                serviceReference(C.class),
                                "setB",
                                serviceReference(B.class)
                        )
                )
        );

        final A actual = module.locate(A.class).get();

        assertThat(actual).isNotNull();
        assertThat(actual.getB()).isNotNull();
        assertThat(actual.getC()).isNotNull();
        assertThat(actual.getC().getB()).isNotNull();
    }

    @Test
    void testBuilder() {
        final SimpleModule module = module();
        module.accept(service().forClass(B.class).forConstructor().build());
        module.accept(service().forClass(B.class).withName("Blah!").forConstructor().build());
        module.accept(service()
                .forClass(C.class).forConstructor()
                .postConstruct().method().withName("setB").withParameters(B.class).build());
        module.accept(service().forClass(A.class).forConstructor().withParameters(B.class, C.class).build());
        module.accept(service()
                .forClass(A.class).withName("AmoduleA")
                .forMethod().withName("createA").withParameters(B.class, C.class).inClass(Amodule.class)
                .build());

        final A actual = module.locate(A.class).get();

        assertThat(actual).isNotNull();
        assertThat(actual.getB()).isNotNull();
        assertThat(actual.getC()).isNotNull();
        assertThat(actual.getC().getB()).isNotNull();
    }

    @Disabled
    @Test
    void testCycleReference() {
        final SimpleModule module = module();
        module.accept(service().forClass(A.class).forConstructor().withParameters(B.class, C.class).build());
        module.accept(service()
                .forClass(B.class).forConstructor()
                .postConstruct().method().withName("setA").withParameters(A.class).build());
        module.accept(service()
                .forClass(C.class).forConstructor()
                .postConstruct().method().withName("setB").withParameters(B.class).build());

        final A actual = module.locate(A.class).get();

        assertThat(actual).isNotNull();
        assertThat(actual.getB()).isNotNull();
        assertThat(actual.getB().getA()).isNotNull();
        assertThat(actual.getC()).isNotNull();
        assertThat(actual.getC().getB()).isNotNull();
    }

    public static class Amodule {

        public A createA(final B b, final C c) {
            return new A(b, c);
        }
    }

    public static class B {

        private A a;

        public A getA() {
            return a;
        }

        public void setA(final A a) {
            this.a = a;
        }
    }

    public static class C {

        private B b;

        public B getB() {
            return b;
        }

        public void setB(final B b) {
            this.b = b;
        }
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
}