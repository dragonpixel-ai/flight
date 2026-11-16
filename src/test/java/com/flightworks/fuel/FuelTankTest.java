package com.flightworks.fuel;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuelTankTest {
    @Test void consumptionNeverGoesNegative() {
        var tank = new FuelTank(100, 25);
        assertEquals(25, tank.consume(40));
        assertEquals(0, tank.fuelKg(), 1e-9);
        assertTrue(tank.isEmpty());
    }

    @Test void refuelNeverExceedsCapacity() {
        var tank = new FuelTank(100, 90);
        assertEquals(10, tank.refuel(30));
        assertEquals(100, tank.fuelKg(), 1e-9);
        assertEquals(1, tank.fractionRemaining(), 1e-9);
    }
}
