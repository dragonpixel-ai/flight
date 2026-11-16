package com.flightworks.electrical;

/** Energy-storage model. Energy is tracked in watt-hours; power is in watts. */
public final class BatteryModel {
    private final double capacityWh;
    private final double maximumChargeWatts;
    private final double maximumDischargeWatts;
    private double energyWh;

    public BatteryModel(double capacityWh, double initialChargeFraction,
                        double maximumChargeWatts, double maximumDischargeWatts) {
        positive(capacityWh, "capacityWh");
        fraction(initialChargeFraction, "initialChargeFraction");
        positive(maximumChargeWatts, "maximumChargeWatts");
        positive(maximumDischargeWatts, "maximumDischargeWatts");
        this.capacityWh = capacityWh;
        this.energyWh = capacityWh * initialChargeFraction;
        this.maximumChargeWatts = maximumChargeWatts;
        this.maximumDischargeWatts = maximumDischargeWatts;
    }

    public synchronized double supply(double requestedWatts, double deltaSeconds) {
        nonNegative(requestedWatts, "requestedWatts");
        positive(deltaSeconds, "deltaSeconds");
        double availablePower = Math.min(maximumDischargeWatts, energyWh * 3600.0 / deltaSeconds);
        double supplied = Math.min(requestedWatts, availablePower);
        energyWh = Math.max(0.0, energyWh - supplied * deltaSeconds / 3600.0);
        return supplied;
    }

    public synchronized double charge(double requestedWatts, double deltaSeconds) {
        nonNegative(requestedWatts, "requestedWatts");
        positive(deltaSeconds, "deltaSeconds");
        double roomPower = (capacityWh - energyWh) * 3600.0 / deltaSeconds;
        double accepted = Math.min(Math.min(requestedWatts, maximumChargeWatts), roomPower);
        energyWh = Math.min(capacityWh, energyWh + accepted * deltaSeconds / 3600.0);
        return accepted;
    }

    public synchronized double chargeFraction() { return energyWh / capacityWh; }
    public synchronized double energyWh() { return energyWh; }
    public double capacityWh() { return capacityWh; }

    private static void positive(double v, String n) {
        if (!Double.isFinite(v) || v <= 0) throw new IllegalArgumentException(n + " must be finite and > 0");
    }
    private static void nonNegative(double v, String n) {
        if (!Double.isFinite(v) || v < 0) throw new IllegalArgumentException(n + " must be finite and >= 0");
    }
    private static void fraction(double v, String n) {
        if (!Double.isFinite(v) || v < 0 || v > 1) throw new IllegalArgumentException(n + " must be between 0 and 1");
    }
}
