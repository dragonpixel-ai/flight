package com.flightworks.flightdeck;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlightComputerTest {
    @Test void computesIndicatedSpeedAndNormalizesHeading() {
        var telemetry = new FlightComputer.Telemetry(100, 1.225, 1200, 2,
                -10, 3, -2, 2400, 0.75, false, true);
        var instruments = FlightComputer.update(telemetry);
        assertEquals(100, instruments.indicatedAirspeedMps(), 1e-9);
        assertEquals(350, instruments.headingDegrees(), 1e-9);
        assertEquals(0.75, instruments.fuelFraction(), 1e-9);
        assertTrue(instruments.gearDown());
    }
}
