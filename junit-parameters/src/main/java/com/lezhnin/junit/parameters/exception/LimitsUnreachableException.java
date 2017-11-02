package com.lezhnin.junit.parameters.exception;

import static java.lang.String.format;
import java.util.function.Predicate;

public class LimitsUnreachableException extends RuntimeException {

    public LimitsUnreachableException(final Class<?> supplied, final Predicate<?> predicate) {
        super(format("Supplied %s value cannot reach limit with predicate %s", supplied, predicate));
    }
}
