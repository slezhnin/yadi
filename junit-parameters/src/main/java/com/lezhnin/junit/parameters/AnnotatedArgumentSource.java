package com.lezhnin.junit.parameters;

import static java.util.Arrays.stream;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class AnnotatedArgumentSource implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final Parameters parameters = context.getRequiredTestMethod().getAnnotation(Parameters.class);
        return Optional.of(parameters).map(this::argumentsStreamOf).orElseThrow(() ->
                new RuntimeException("No @Parameters was specified")
        );
    }

    private Stream<? extends Arguments> argumentsStreamOf(final Parameters parameters) {
        return Stream.generate(() ->
                Arguments.of(
                        stream(parameters.value()).map(parameterType -> {
                            try {
                                return parameterType.newInstance().get();
                            } catch (Exception e) {
                                throw new RuntimeException("Error creating " + parameterType.getName(), e);
                            }
                        }).toArray()
                )
        ).limit(parameters.count());
    }
}
