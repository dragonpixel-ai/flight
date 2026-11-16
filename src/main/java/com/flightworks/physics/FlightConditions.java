package com.flightworks.physics;

/** Atmospheric conditions used by the aircraft force model. SI units throughout. */
public record FlightConditions(double airDensityKgPerM3, double gravityMps2) {
    public FlightConditions {
        if (!Double.isFinite(airDensityKgPerM3) || airDensityKgPerM3 < 0.0) {
            throw new IllegalArgumentException("Air density must be finite and non-negative");
        }
        if (!Double.isFinite(gravityMps2) || gravityMps2 < 0.0) {
            throw new IllegalArgumentException("Gravity must be finite and non-negative");
        }
    }

    public static FlightConditions standardSeaLevel() {
        return new FlightConditions(1.225, 9.80665);
    }
}
