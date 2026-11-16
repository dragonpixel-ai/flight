package com.flightworks.physics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AerodynamicModelTest {
    private AerodynamicModel model() {
        return new AerodynamicModel(16.2, 5.2, Math.toRadians(-2),
                1.5, 0.025, 0.045, Math.toRadians(16), 0.7, 0.08);
    }

    @Test void liftIncreasesWithAngleBeforeStall() {
        var controls = FlightControls.neutral();
        var low = model().evaluate(Math.toRadians(2), controls);
        var high = model().evaluate(Math.toRadians(8), controls);
        assertTrue(high.liftCoefficient() > low.liftCoefficient());
        assertFalse(high.stalled());
    }

    @Test void stallLimitsAndReducesLift() {
        var model = model();
        var before = model.evaluate(Math.toRadians(15), FlightControls.neutral());
        var after = model.evaluate(Math.toRadians(35), FlightControls.neutral());
        assertTrue(after.stalled());
        assertTrue(after.liftCoefficient() < before.liftCoefficient());
    }

    @Test void flapsIncreaseLiftAndDrag() {
        var model = model();
        var clean = model.evaluate(Math.toRadians(4), FlightControls.neutral());
        var flaps = model.evaluate(Math.toRadians(4),
                new FlightControls(0, 0, 0, 0, 1, true));
        assertTrue(flaps.liftCoefficient() > clean.liftCoefficient());
        assertTrue(flaps.dragCoefficient() > clean.dragCoefficient());
    }
}
