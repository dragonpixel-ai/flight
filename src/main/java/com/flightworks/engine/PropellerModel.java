package com.flightworks.engine;

import com.flightworks.physics.FlightConditions;

/** Converts shaft power into propeller thrust using a bounded efficiency model. */
public final class PropellerModel {
    private final double diskAreaM2;
    private final double efficiency;
    private final double minimumWorkingSpeedMps;

    public PropellerModel(double diameterMetres, double efficiency, double minimumWorkingSpeedMps) {
        if (!Double.isFinite(diameterMetres) || diameterMetres <= 0)
            throw new IllegalArgumentException("Propeller diameter must be finite and > 0");
        if (!Double.isFinite(efficiency) || efficiency <= 0 || efficiency > 1)
            throw new IllegalArgumentException("Efficiency must be in (0, 1]");
        if (!Double.isFinite(minimumWorkingSpeedMps) || minimumWorkingSpeedMps <= 0)
            throw new IllegalArgumentException("Minimum working speed must be finite and > 0");
        double radius = diameterMetres / 2.0;
        this.diskAreaM2 = Math.PI * radius * radius;
        this.efficiency = efficiency;
        this.minimumWorkingSpeedMps = minimumWorkingSpeedMps;
    }

    public Result evaluate(double shaftPowerWatts, double airspeedMps, FlightConditions conditions) {
        if (!Double.isFinite(shaftPowerWatts) || shaftPowerWatts < 0)
            throw new IllegalArgumentException("Shaft power must be finite and non-negative");
        if (!Double.isFinite(airspeedMps) || airspeedMps < 0)
            throw new IllegalArgumentException("Airspeed must be finite and non-negative");

        double usefulPower = shaftPowerWatts * efficiency;
        double staticThrust = Math.sqrt(2.0 * conditions.airDensityKgPerM3()
                * diskAreaM2 * usefulPower);
        double forwardThrust = usefulPower / Math.max(airspeedMps, minimumWorkingSpeedMps);
        // Blend the static and forward-flight estimates to avoid a singularity at zero speed.
        double blend = Math.min(1.0, airspeedMps / (minimumWorkingSpeedMps * 2.0));
        double thrust = staticThrust * (1.0 - blend) + forwardThrust * blend;
        return new Result(thrust, usefulPower, staticThrust);
    }

    public record Result(double thrustNewtons, double usefulPowerWatts, double staticThrustNewtons) {}
}
