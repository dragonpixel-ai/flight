package com.flightworks.physics;

/** Immutable aircraft state snapshot. Speeds are metres per second, angles are radians. */
public record AircraftState(
        double massKg,
        double airspeedMps,
        double angleOfAttackRadians,
        double wingAreaM2,
        double liftCoefficient,
        double dragCoefficient,
        double thrustNewtons
) {
    public AircraftState {
        requireNonNegative(massKg, "massKg");
        requireNonNegative(airspeedMps, "airspeedMps");
        requireFinite(angleOfAttackRadians, "angleOfAttackRadians");
        requireNonNegative(wingAreaM2, "wingAreaM2");
        requireFinite(liftCoefficient, "liftCoefficient");
        requireNonNegative(dragCoefficient, "dragCoefficient");
        requireNonNegative(thrustNewtons, "thrustNewtons");
    }

    private static void requireNonNegative(double value, String name) {
        if (!Double.isFinite(value) || value < 0.0) {
            throw new IllegalArgumentException(name + " must be finite and non-negative");
        }
    }

    private static void requireFinite(double value, String name) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(name + " must be finite");
        }
    }
}
