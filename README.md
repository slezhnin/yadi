# YADI
__Yet Another Dependency Injection__

Here I'm trying to make a simple dependency injection framework.

The main idea is to make as little intrusion into implementation classes as possible.

## Some thoughts

Service dependencies:
1. Construction time dependencies
2. Post construction time dependencies

Dependency type:
1. Service reference (id, string)
2. Immediate supplier (supplier without external dependencies)

Construction method:
1. Instance creation through constructor
2. Instance creation through method invocation

## Annotation

[annotated README](annotated/README.md)