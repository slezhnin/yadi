package com.lezhnin.junit.parameters.provider.random;

import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.junit.parameters.AnnotatedArgumentSource;
import com.lezhnin.junit.parameters.Limit;
import com.lezhnin.junit.parameters.Parameters;
import java.util.UUID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

class RandomUUIDStringTest {

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomUUIDString.class)
    void testRandomString(final String randomString) {
        assertThat(randomString).isNotNull();
        assertThat(UUID.fromString(randomString)).isNotNull();
    }
}