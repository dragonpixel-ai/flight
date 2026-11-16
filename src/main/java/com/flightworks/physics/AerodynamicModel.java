package com.flightworks.physics;

/**
 * Configurable wing model with a linear lift curve, a soft stall transition,
 * induced drag and flap effects. Inputs and outputs use SI units.
 */
public final class AerodynamicModel {
    private final double wingAreaM2;
    private final double liftSlopePerRadian;
    private final double zeroLiftAngleRadians;
    private final double maximumLiftCoefficient;
    private final double baseDragCoefficient;
    private final double inducedDragFactor;
    private final double stallAngleRadians;
    private final double flapLiftIncrement;
    private final double flapDragIncrement;

    public AerodynamicModel(double wingAreaM2, double liftSlopePerRadian,
            double zeroLiftAngleRadians, double maximumLiftCoefficient,
            double baseDragCoefficient, double inducedDragFactor,
            double stallAngleRadians, double flapLiftIncrement, double flapDragIncrement) {
        positive(wingAreaM2, "wingAreaM2");
        positive(liftSlopePerRadian, "liftSlopePerRadian");
        positive(maximumLiftCoefficient, "maximumLiftCoefficient");
        nonNegative(baseDragCoefficient, "baseDragCoefficient");
        nonNegative(inducedDragFactor, "inducedDragFactor");
        positive(stallAngleRadians, "stallAngleRadians");
        nonNegative(flapLiftIncrement, "flapLiftIncrement");
        nonNegative(flapDragIncrement, "flapDragIncrement");
        finite(zeroLiftAngleRadians, "zeroLiftAngleRadians");
        this.wingAreaM2 = wingAreaM2;
        this.liftSlopePerRadian = liftSlopePerRadian;
        this.zeroLiftAngleRadians = zeroLiftAngleRadians;
        this.maximumLiftCoefficient = maximumLiftCoefficient;
        this.baseDragCoefficient = baseDragCoefficient;
        this.inducedDragFactor = inducedDragFactor;
        this.stallAngleRadians = stallAngleRadians;
        this.flapLiftIncrement = flapLiftIncrement;
        this.flapDragIncrement = flapDragIncrement;
    }

    public Result evaluate(double angleOfAttackRadians, FlightControls controls) {
        finite(angleOfAttackRadians, "angleOfAttackRadians");
        double effectiveAngle = angleOfAttackRadians + controls.elevator() * Math.toRadians(8);
        double flapLift = controls.flaps() * flapLiftIncrement;
        double rawLift = liftSlopePerRadian * (effectiveAngle - zeroLiftAngleRadians) + flapLift;
        double cl = Math.max(-maximumLiftCoefficient, Math.min(maximumLiftCoefficient, rawLift));

        double stallRatio = Math.abs(effectiveAngle) / stallAngleRadians;
        if (stallRatio > 1.0) {
            // Smoothly reduce lift beyond stall instead of allowing unbounded lift.
            double fade = 1.0 / (1.0 + 2.5 * (stallRatio - 1.0) * (stallRatio - 1.0));
            cl *= fade;
        }

        double cd = baseDragCoefficient + inducedDragFactor * cl * cl
                + controls.flaps() * flapDragIncrement;
        return new Result(wingAreaM2, cl, cd, effectiveAngle, stallRatio >= 1.0);
    }

    public record Result(double wingAreaM2, double liftCoefficient,
                         double dragCoefficient, double effectiveAngleRadians, boolean stalled) {}

    private static void positive(double v, String n) {
        if (!Double.isFinite(v) || v <= 0) throw new IllegalArgumentException(n + " must be finite and > 0");
    }
    private static void nonNegative(double v, String n) {
        if (!Double.isFinite(v) || v < 0) throw new IllegalArgumentException(n + " must be finite and >= 0");
    }
    private static void finite(double v, String n) {
        if (!Double.isFinite(v)) throw new IllegalArgumentException(n + " must be finite");
    }
}
