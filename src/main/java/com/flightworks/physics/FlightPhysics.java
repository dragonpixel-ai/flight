package com.flightworks.physics;

/**
 * Deterministic first-pass aircraft force model.
 *
 * This calculates forces only. The Aeronautics/Sable integration layer must
 * submit these forces to the owning sublevel; this class never changes entity
 * position or velocity and does not implement a second movement system.
 */
public final class FlightPhysics {
    private FlightPhysics() {}

    public static ForceResult calculate(AircraftState aircraft, FlightConditions conditions) {
        double dynamicPressure = 0.5 * conditions.airDensityKgPerM3()
                * aircraft.airspeedMps() * aircraft.airspeedMps();

        double lift = dynamicPressure * aircraft.wingAreaM2() * aircraft.liftCoefficient();
        double drag = dynamicPressure * aircraft.wingAreaM2() * aircraft.dragCoefficient();
        double weight = aircraft.massKg() * conditions.gravityMps2();

        return new ForceResult(
                lift,
                drag,
                aircraft.thrustNewtons(),
                weight,
                lift - weight,
                aircraft.thrustNewtons() - drag
        );
    }

    /** Standard incompressible dynamic pressure, in Pascals. */
    public static double dynamicPressure(double airDensityKgPerM3, double airspeedMps) {
        if (!Double.isFinite(airDensityKgPerM3) || airDensityKgPerM3 < 0.0
                || !Double.isFinite(airspeedMps) || airspeedMps < 0.0) {
            throw new IllegalArgumentException("Density and airspeed must be finite and non-negative");
        }
        return 0.5 * airDensityKgPerM3 * airspeedMps * airspeedMps;
    }
}
