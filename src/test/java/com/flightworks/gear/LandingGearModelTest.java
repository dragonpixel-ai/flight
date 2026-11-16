package com.flightworks.gear;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LandingGearModelTest {
    @Test void compressionCreatesSpringForce() {
        var gear = new LandingGearModel(20_000, 1_500, 0.5);
        var result = gear.evaluate(true, 0.2, 0);
        assertEquals(4_000, result.upwardForceNewtons(), 1e-9);
        assertFalse(result.bottomedOut());
    }

    @Test void retractedGearProducesNoContactForce() {
        var gear = new LandingGearModel(20_000, 1_500, 0.5);
        assertEquals(0, gear.evaluate(false, 0.2, 0).upwardForceNewtons());
    }
}
