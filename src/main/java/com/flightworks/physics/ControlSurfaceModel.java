package com.flightworks.physics;

/** Converts pilot control inputs into bounded aerodynamic moment coefficients. */
public final class ControlSurfaceModel {
    private final double maximumPitchMoment;
    private final double maximumRollMoment;
    private final double maximumYawMoment;

    public ControlSurfaceModel(double maximumPitchMoment, double maximumRollMoment,
                               double maximumYawMoment) {
        positive(maximumPitchMoment, "maximumPitchMoment");
        positive(maximumRollMoment, "maximumRollMoment");
        positive(maximumYawMoment, "maximumYawMoment");
        this.maximumPitchMoment = maximumPitchMoment;
        this.maximumRollMoment = maximumRollMoment;
        this.maximumYawMoment = maximumYawMoment;
    }

    public Moments evaluate(FlightControls controls, double dynamicPressure, double referenceArea,
                            double referenceChord, double wingspan) {
        nonNegative(dynamicPressure, "dynamicPressure");
        positive(referenceArea, "referenceArea");
        positive(referenceChord, "referenceChord");
        positive(wingspan, "wingspan");
        double pitch = dynamicPressure * referenceArea * referenceChord
                * maximumPitchMoment * controls.elevator();
        double roll = dynamicPressure * referenceArea * wingspan
                * maximumRollMoment * controls.aileron();
        double yaw = dynamicPressure * referenceArea * wingspan
                * maximumYawMoment * controls.rudder();
        return new Moments(pitch, roll, yaw);
    }

    public record Moments(double pitchNewtonMetres, double rollNewtonMetres, double yawNewtonMetres) {}

    private static void positive(double v, String n) {
        if (!Double.isFinite(v) || v <= 0) throw new IllegalArgumentException(n + " must be finite and > 0");
    }
    private static void nonNegative(double v, String n) {
        if (!Double.isFinite(v) || v < 0) throw new IllegalArgumentException(n + " must be finite and >= 0");
    }
}
