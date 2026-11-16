package com.flightworks.electrical;

/**
 * Server-authoritative aircraft electrical bus. Generator power serves loads first;
 * the battery supplies deficits and stores surplus generation.
 */
public final class ElectricalSystem {
    private final BatteryModel battery;
    private final GeneratorModel generator;

    public ElectricalSystem(BatteryModel battery, GeneratorModel generator) {
        if (battery == null || generator == null) throw new IllegalArgumentException("battery and generator are required");
        this.battery = battery;
        this.generator = generator;
    }

    public synchronized Snapshot tick(double engineRpm, boolean generatorEnabled,
                                      double requestedLoadWatts, double deltaSeconds) {
        if (!Double.isFinite(requestedLoadWatts) || requestedLoadWatts < 0)
            throw new IllegalArgumentException("requestedLoadWatts must be finite and >= 0");
        if (!Double.isFinite(deltaSeconds) || deltaSeconds <= 0)
            throw new IllegalArgumentException("deltaSeconds must be finite and > 0");

        double generated = generator.outputWatts(engineRpm, generatorEnabled);
        double generatorToLoads = Math.min(generated, requestedLoadWatts);
        double deficit = requestedLoadWatts - generatorToLoads;
        double batteryToLoads = battery.supply(deficit, deltaSeconds);
        double surplus = generated - generatorToLoads;
        double batteryAccepted = battery.charge(surplus, deltaSeconds);
        double supplied = generatorToLoads + batteryToLoads;
        return new Snapshot(generated, requestedLoadWatts, supplied,
                Math.max(0.0, requestedLoadWatts - supplied), batteryToLoads,
                batteryAccepted, battery.chargeFraction());
    }

    public record Snapshot(double generatedWatts, double requestedLoadWatts,
                           double suppliedLoadWatts, double shedLoadWatts,
                           double batteryDischargeWatts, double batteryChargeWatts,
                           double batteryChargeFraction) {
        public boolean hasPowerShortage() { return shedLoadWatts > 1.0e-9; }
    }
}
