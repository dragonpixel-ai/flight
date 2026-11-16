package com.flightworks.electrical;

/** Engine-driven electrical generator with a simple RPM cut-in and output ceiling. */
public final class GeneratorModel {
    private final double maximumOutputWatts;
    private final double cutInRpm;
    private final double ratedRpm;

    public GeneratorModel(double maximumOutputWatts, double cutInRpm, double ratedRpm) {
        if (!Double.isFinite(maximumOutputWatts) || maximumOutputWatts <= 0)
            throw new IllegalArgumentException("maximumOutputWatts must be finite and > 0");
        if (!Double.isFinite(cutInRpm) || cutInRpm < 0)
            throw new IllegalArgumentException("cutInRpm must be finite and >= 0");
        if (!Double.isFinite(ratedRpm) || ratedRpm <= cutInRpm)
            throw new IllegalArgumentException("ratedRpm must be greater than cutInRpm");
        this.maximumOutputWatts = maximumOutputWatts;
        this.cutInRpm = cutInRpm;
        this.ratedRpm = ratedRpm;
    }

    public double outputWatts(double engineRpm, boolean enabled) {
        if (!Double.isFinite(engineRpm) || engineRpm < 0)
            throw new IllegalArgumentException("engineRpm must be finite and >= 0");
        if (!enabled || engineRpm <= cutInRpm) return 0.0;
        double fraction = Math.min(1.0, (engineRpm - cutInRpm) / (ratedRpm - cutInRpm));
        return maximumOutputWatts * fraction;
    }
}
