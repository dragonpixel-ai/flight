package com.flightworks.physics;

/** Resultant aerodynamic and propulsion forces in Newtons. */
public record ForceResult(
        double liftNewtons,
        double dragNewtons,
        double thrustNewtons,
        double weightNewtons,
        double verticalNetNewtons,
        double longitudinalNetNewtons
) {
    public double verticalAccelerationMps2(double massKg) {
        if (!Double.isFinite(massKg) || massKg <= 0.0) {
            throw new IllegalArgumentException("Mass must be finite and greater than zero");
        }
        return verticalNetNewtons / massKg;
    }

    public double longitudinalAccelerationMps2(double massKg) {
        if (!Double.isFinite(massKg) || massKg <= 0.0) {
            throw new IllegalArgumentException("Mass must be finite and greater than zero");
        }
        return longitudinalNetNewtons / massKg;
    }
}
