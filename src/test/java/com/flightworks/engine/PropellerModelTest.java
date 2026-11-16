package com.flightworks.engine;

import com.flightworks.physics.FlightConditions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PropellerModelTest {
    @Test void producesFinitePositiveStaticThrust() {
        var result = new PropellerModel(2.0, 0.82, 8.0)
                .evaluate(100_000, 0, FlightConditions.standardSeaLevel());
        assertTrue(Double.isFinite(result.thrustNewtons()));
        assertTrue(result.thrustNewtons() > 0);
    }

    @Test void zeroPowerProducesZeroThrust() {
        var result = new PropellerModel(2.0, 0.82, 8.0)
                .evaluate(0, 30, FlightConditions.standardSeaLevel());
        assertEquals(0, result.thrustNewtons(), 1e-9);
    }
}
