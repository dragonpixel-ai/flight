package com.flightworks.gear;

/** Spring-damper landing gear calculation; applies forces at a contact point in the host physics engine. */
public final class LandingGearModel {
    private final double springRateNPerM;
    private final double dampingNsPerM;
    private final double maximumCompressionM;

    public LandingGearModel(double springRateNPerM, double dampingNsPerM, double maximumCompressionM) {
        positive(springRateNPerM, "springRateNPerM");
        positive(dampingNsPerM, "dampingNsPerM");
        positive(maximumCompressionM, "maximumCompressionM");
        this.springRateNPerM = springRateNPerM;
        this.dampingNsPerM = dampingNsPerM;
        this.maximumCompressionM = maximumCompressionM;
    }

    public ContactForce evaluate(boolean deployed, double compressionM, double compressionVelocityMps) {
        if (!Double.isFinite(compressionM) || compressionM < 0)
            throw new IllegalArgumentException("compressionM must be finite and non-negative");
        if (!Double.isFinite(compressionVelocityMps))
            throw new IllegalArgumentException("compressionVelocityMps must be finite");
        if (!deployed || compressionM <= 0) return new ContactForce(0, false);
        double compression = Math.min(compressionM, maximumCompressionM);
        double force = springRateNPerM * compression
                - dampingNsPerM * compressionVelocityMps;
        force = Math.max(0, force);
        return new ContactForce(force, compressionM >= maximumCompressionM);
    }

    public record ContactForce(double upwardForceNewtons, boolean bottomedOut) {}

    private static void positive(double v, String n) {
        if (!Double.isFinite(v) || v <= 0) throw new IllegalArgumentException(n + " must be finite and > 0");
    }
}
