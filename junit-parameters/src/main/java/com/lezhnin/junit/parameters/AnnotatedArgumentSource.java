package com.lezhnin.junit.parameters;

import static java.util.stream.Stream.generate;
import com.lezhnin.junit.parameters.supplier.ArgumentsSupplier;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class AnnotatedArgumentSource implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final Parameters parameters = context.getRequiredTestMethod().getAnnotation(Parameters.class);
        return Optional.of(parameters)
                       .map(definedParameters -> argumentStream(
                               definedParameters,
                               context.getRequiredTestMethod().getParameters())
                       ).orElseThrow(() -> new RuntimeException("No @Parameters was specified"));
    }

    private Stream<? extends Arguments> argumentStream(
            final Parameters annotatedParameters,
            final Parameter[] parameters
    ) {
        return generate(new ArgumentsSupplier(annotatedParameters, parameters)).limit(annotatedParameters.count());
    }
}
