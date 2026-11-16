package com.flightworks.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EngineModelTest {
    private final EngineModel engine = new EngineModel(150_000, 3000, 700, 0.25);

    @Test void zeroThrottleProducesIdleAndNoPower() {
        var output = engine.evaluate(0, 1);
        assertEquals(700, output.rpm(), 1e-9);
        assertEquals(0, output.powerWatts());
        assertEquals(0, output.fuelKgPerHour());
    }

    @Test void powerIncreasesWithThrottle() {
        var quarter = engine.evaluate(0.25, 1);
        var threeQuarter = engine.evaluate(0.75, 1);
        assertTrue(threeQuarter.powerWatts() > quarter.powerWatts());
        assertTrue(threeQuarter.rpm() > quarter.rpm());
    }

    @Test void damageReducesPower() {
        assertEquals(engine.evaluate(1, 1).powerWatts() * 0.5,
                engine.evaluate(1, 0.5).powerWatts(), 1e-8);
    }

    @Test void rejectsOutOfRangeThrottle() {
        assertThrows(IllegalArgumentException.class, () -> engine.evaluate(1.1, 1));
    }
}
