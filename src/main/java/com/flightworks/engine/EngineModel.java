package com.flightworks.engine;

/**
 * Small deterministic piston/turbine-style engine output model.
 * It is a simulation component only; game-side engine blocks own persistence and ticking.
 */
public final class EngineModel {
    private final double maximumPowerWatts;
    private final double maximumRpm;
    private final double idleRpm;
    private final double specificFuelConsumptionKgPerKWh;

    public EngineModel(double maximumPowerWatts, double maximumRpm, double idleRpm,
                       double specificFuelConsumptionKgPerKWh) {
        requirePositive(maximumPowerWatts, "maximumPowerWatts");
        requirePositive(maximumRpm, "maximumRpm");
        requirePositive(idleRpm, "idleRpm");
        requirePositive(specificFuelConsumptionKgPerKWh, "specificFuelConsumptionKgPerKWh");
        if (idleRpm >= maximumRpm) throw new IllegalArgumentException("Idle RPM must be below maximum RPM");
        this.maximumPowerWatts = maximumPowerWatts;
        this.maximumRpm = maximumRpm;
        this.idleRpm = idleRpm;
        this.specificFuelConsumptionKgPerKWh = specificFuelConsumptionKgPerKWh;
    }

    public Output evaluate(double throttle, double engineHealth) {
        if (!Double.isFinite(throttle) || throttle < 0.0 || throttle > 1.0) {
            throw new IllegalArgumentException("Throttle must be between 0 and 1");
        }
        if (!Double.isFinite(engineHealth) || engineHealth < 0.0 || engineHealth > 1.0) {
            throw new IllegalArgumentException("Engine health must be between 0 and 1");
        }
        double rpm = idleRpm + (maximumRpm - idleRpm) * throttle;
        double power = maximumPowerWatts * throttle * engineHealth;
        double angularVelocity = rpm * (2.0 * Math.PI / 60.0);
        double torque = angularVelocity == 0.0 ? 0.0 : power / angularVelocity;
        double fuelKgPerHour = power / 1000.0 * specificFuelConsumptionKgPerKWh;
        return new Output(rpm, power, torque, fuelKgPerHour);
    }

    private static void requirePositive(double value, String name) {
        if (!Double.isFinite(value) || value <= 0.0) {
            throw new IllegalArgumentException(name + " must be finite and greater than zero");
        }
    }

    public record Output(double rpm, double powerWatts, double torqueNewtonMetres,
                         double fuelKgPerHour) {}
}
