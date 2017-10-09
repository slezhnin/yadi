# Annotated

An attempt to make package scan for annotations (Named, Inject)
and put annotated classes into a service registry.

Annotations will be scanned only for public classes and methods.

## @Named class

Example:
```java
package example;

@Named
public class Example {}

@Named("E1")
public class NamedExample {}
```

In the example block two classes are defined:

1. First with annotation `@Named` without parameter
will register service of `class Example` with name `example.Example`.
2. Second annotated with parameter will register
service of `class NamedExample` with name `E1`.

## @Named method

Example:
```java
package example;

public class Service1 {}

public class Service2 {

    private final Service1 service1;
    
    public Service2(final Service1 service1) {
        this.service1 = service1;
    }
}

@Named
public class Example {

    @Named
    public Service1 getService1() {
        return new Service1();
    }
    
    @Named("S2")
    public Service2 getService2(final Service1 service1) {
        return new Service2(service1);
    }
}
```

In this example three services will be created:

1. Service of `class Example` with name `example.Example`.
2. Service of `class Service1` with name `example.Service1`
created by method `Example.getService1`.
3. Service of `class Service2` with name `S2`
created by method `Example.getService2`.

In this case the `class Example` serves as a configuration module,
that defines how other service instances are created.

## @Named parameter

Modified example of the previous section:
```java
// ...

@Named
public class Example {

    @Named("S1")
    public Service1 getService1() {
        return new Service1();
    }
    
    @Named("S2")
    public Service2 getService2(@Named("S1") final Service1 service1) {
        return new Service2(service1);
    }
}
```

Here in the method `Example.getService1` to annotation `@Named` was added a parameter.
Thereafter the name of service `example.Service1` was change to `S1`.
To find the service used as parameter to method `Example.getService2`
the annotation `@Named("S1")` was added to parameter.

## @Inject constructor

Example:
```java
package example;

@Named
public class Example {}

public class ConstructExample {
    
    public ConstructExample() {}
    
    @Inject
    public ConstructExample(final Example example) {}
}
```

The annotation `@Inject` with a constructor should be used
if several constructors exist in the class.
If no `@Inject` annotation is placed on constructor of the class
then the first constructor of the class will be used.

## @Inject method

Example:
```java
package example;

@Named
public class Example {
    
    private NamedExample namedExample;
    
    @Inject
    public void setNamedExample(final NamedExample namedExample) {
        this.namedExample = namedExample; 
    }
}

@Named
public class NamedExample {
    
    @Inject
    void afterConstruction() {}
}
```

If annotation `@Inject` is used for a method
then this method will be called after creation
of the instance of the enclosing class.

The parameters of method will be injected into it upon call.
