package com.flightworks.electrical;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ElectricalSystemTest {
    @Test void generatorSuppliesLoadsBeforeBattery() {
        var battery = new BatteryModel(1000, 1.0, 500, 500);
        var system = new ElectricalSystem(battery, new GeneratorModel(800, 500, 2500));
        var snapshot = system.tick(2500, true, 600, 1);
        assertEquals(800, snapshot.generatedWatts(), 1e-9);
        assertEquals(600, snapshot.suppliedLoadWatts(), 1e-9);
        assertEquals(0, snapshot.batteryDischargeWatts(), 1e-9);
        assertEquals(1.0, snapshot.batteryChargeFraction(), 1e-9);
    }

    @Test void batteryBridgesGeneratorDeficit() {
        var battery = new BatteryModel(1000, 1.0, 500, 500);
        var system = new ElectricalSystem(battery, new GeneratorModel(300, 500, 2500));
        var snapshot = system.tick(2500, true, 600, 3600);
        assertEquals(600, snapshot.suppliedLoadWatts(), 1e-9);
        assertEquals(300, snapshot.batteryDischargeWatts(), 1e-9);
        assertFalse(snapshot.hasPowerShortage());
        assertEquals(700, battery.energyWh(), 1e-9);
    }

    @Test void reportsLoadShedWhenBatteryCannotMeetDemand() {
        var battery = new BatteryModel(100, 0.0, 100, 100);
        var system = new ElectricalSystem(battery, new GeneratorModel(0 + 1, 500, 2500));
        var snapshot = system.tick(0, false, 250, 1);
        assertEquals(100, snapshot.suppliedLoadWatts(), 1e-9);
        assertEquals(150, snapshot.shedLoadWatts(), 1e-9);
        assertTrue(snapshot.hasPowerShortage());
    }

    @Test void batteryNeverExceedsCapacity() {
        var battery = new BatteryModel(100, 0.5, 1000, 1000);
        assertEquals(100, battery.charge(1000, 3600), 1e-9);
        assertEquals(100, battery.energyWh(), 1e-9);
    }

    @Test void generatorIsDisabledBelowCutIn() {
        var generator = new GeneratorModel(500, 700, 2500);
        assertEquals(0, generator.outputWatts(650, true), 1e-9);
        assertEquals(0, generator.outputWatts(2500, false), 1e-9);
    }
}
