package com.flightworks.physics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlightPhysicsTest {
    private final FlightConditions air = FlightConditions.standardSeaLevel();

    @Test void liftAndDragGrowWithSquareOfAirspeed() {
        var slow = new AircraftState(900, 20, 0, 16, 0.8, 0.04, 0);
        var fast = new AircraftState(900, 40, 0, 16, 0.8, 0.04, 0);
        var a = FlightPhysics.calculate(slow, air);
        var b = FlightPhysics.calculate(fast, air);
        assertEquals(a.liftNewtons() * 4, b.liftNewtons(), 1e-8);
        assertEquals(a.dragNewtons() * 4, b.dragNewtons(), 1e-8);
    }

    @Test void zeroAirspeedProducesNoAerodynamicForce() {
        var state = new AircraftState(900, 0, 0, 16, 0.8, 0.04, 500);
        var result = FlightPhysics.calculate(state, air);
        assertEquals(0, result.liftNewtons());
        assertEquals(0, result.dragNewtons());
        assertEquals(500, result.thrustNewtons());
    }

    @Test void verticalAccelerationIsNetForceOverMass() {
        var state = new AircraftState(1000, 30, 0, 20, 1.0, 0.05, 0);
        var result = FlightPhysics.calculate(state, air);
        assertEquals((result.liftNewtons() - result.weightNewtons()) / 1000,
                result.verticalAccelerationMps2(1000), 1e-10);
    }

    @Test void rejectsInvalidMassForAcceleration() {
        var result = new ForceResult(0, 0, 0, 0, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> result.verticalAccelerationMps2(0));
    }
}
