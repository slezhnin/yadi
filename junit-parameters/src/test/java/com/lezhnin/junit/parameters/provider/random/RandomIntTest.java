package com.lezhnin.junit.parameters.provider.random;

import static org.assertj.core.api.Assertions.assertThat;
import com.lezhnin.junit.parameters.AnnotatedArgumentSource;
import com.lezhnin.junit.parameters.Limit;
import com.lezhnin.junit.parameters.Parameters;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

class RandomIntTest {

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomInt.class)
    void testRandomInt(final Integer randomInteger) {
        assertThat(randomInteger).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomInt.class)
    void testRandomIntPositive(@Limit("positive") final Integer randomInteger) {
        assertThat(randomInteger).isNotNull();
        assertThat(randomInteger).isGreaterThanOrEqualTo(0);
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomInt.class)
    void testRandomIntNegative(@Limit("negative") final Integer randomInteger) {
        assertThat(randomInteger).isNotNull();
        assertThat(randomInteger).isLessThan(0);
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomInt.class)
    void testRandomIntGT(@Limit("> 1") final Integer randomInteger) {
        assertThat(randomInteger).isNotNull();
        assertThat(randomInteger).isGreaterThan(1);
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomInt.class)
    void testRandomIntLT(@Limit("< 1") final Integer randomInteger) {
        assertThat(randomInteger).isNotNull();
        assertThat(randomInteger).isLessThan(1);
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomInt.class)
    void testRandomIntGTE(@Limit(">= 1") final Integer randomInteger) {
        assertThat(randomInteger).isNotNull();
        assertThat(randomInteger).isGreaterThanOrEqualTo(1);
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomInt.class)
    void testRandomIntLTE(@Limit("<= 1") final Integer randomInteger) {
        assertThat(randomInteger).isNotNull();
        assertThat(randomInteger).isLessThanOrEqualTo(1);
    }

    @ParameterizedTest
    @ArgumentsSource(AnnotatedArgumentSource.class)
    @Parameters(RandomInt.class)
    void testRandomIntRange(@Limit({"positive", "<= 1000"}) final Integer randomInteger) {
        assertThat(randomInteger).isNotNull();
        assertThat(randomInteger).isGreaterThanOrEqualTo(0).isLessThanOrEqualTo(1000);
    }
}