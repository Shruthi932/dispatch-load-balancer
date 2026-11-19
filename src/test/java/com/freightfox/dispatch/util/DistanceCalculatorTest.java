
package com.freightfox.dispatch.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistanceCalculatorTest {

    @Test
    void shouldReturnZeroForSameCoordinates() {

        double distance = DistanceCalculator.calculateDistance(
                28.6139, 77.2090,
                28.6139, 77.2090
        );

        assertEquals(0.0, distance, 0.001);
    }

    @Test
    void shouldReturnPositiveDistanceForDifferentCoordinates() {

        double distance = DistanceCalculator.calculateDistance(
                28.6139, 77.2090,
                28.5355, 77.3910
        );

        assertTrue(distance > 0);
    }

    @Test
    void shouldCalculateDistanceApproximatelyCorrectly() {

        double distance = DistanceCalculator.calculateDistance(
                28.6139, 77.2090,
                28.5355, 77.3910
        );

        assertEquals(18.9, distance, 1.0);
    }
}
