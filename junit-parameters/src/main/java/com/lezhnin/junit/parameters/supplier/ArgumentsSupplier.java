package com.lezhnin.junit.parameters.supplier;

import static java.util.Objects.requireNonNull;
import com.lezhnin.junit.parameters.Parameters;
import com.lezhnin.junit.parameters.provider.ValueProvider;
import com.lezhnin.junit.parameters.supplier.ArgumentSupplier;
import java.lang.reflect.Parameter;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.junit.jupiter.params.provider.Arguments;

public class ArgumentsSupplier implements Supplier<Arguments> {

    private final Parameters annotatedParameters;
    private final Parameter[] parameters;

    public ArgumentsSupplier(final Parameters annotatedParameters, final Parameter[] parameters) {
        this.annotatedParameters = requireNonNull(annotatedParameters);
        this.parameters = requireNonNull(parameters);
        requireNonNull(annotatedParameters.value(), "@Parameters(value) should be initialized!");

        if (annotatedParameters.value().length != parameters.length) {
            throw new RuntimeException("Invalid @Parameters(value) length!");
        }
    }

    @Override
    public Arguments get() {
        return Arguments.of(
                IntStream.range(0, annotatedParameters.value().length).mapToObj(i ->
                        supplier(annotatedParameters.value()[i], parameters[i], annotatedParameters.maxSize()).get()
                ).toArray()
        );
    }

    @SuppressWarnings("unchecked")
    private <T> ArgumentSupplier<T> supplier(
            final Class<? extends ValueProvider<?>> providerClass,
            final Parameter parameter,
            final int maxSize
    ) {
        try {
            return new ArgumentSupplier<>(
                    (ValueProvider<T>) requireNonNull(providerClass).newInstance(),
                    requireNonNull(parameter),
                    maxSize
            );
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
