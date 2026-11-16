package com.flightworks.fuel;

/** Thread-safe fuel store. Consumption is atomic and never drives quantity below zero. */
public final class FuelTank {
    private final double capacityKg;
    private double fuelKg;

    public FuelTank(double capacityKg, double initialFuelKg) {
        if (!Double.isFinite(capacityKg) || capacityKg <= 0)
            throw new IllegalArgumentException("Capacity must be finite and > 0");
        if (!Double.isFinite(initialFuelKg) || initialFuelKg < 0 || initialFuelKg > capacityKg)
            throw new IllegalArgumentException("Initial fuel must be within tank capacity");
        this.capacityKg = capacityKg;
        this.fuelKg = initialFuelKg;
    }

    public synchronized double consume(double requestedKg) {
        if (!Double.isFinite(requestedKg) || requestedKg < 0)
            throw new IllegalArgumentException("Requested fuel must be finite and non-negative");
        double consumed = Math.min(fuelKg, requestedKg);
        fuelKg -= consumed;
        return consumed;
    }

    public synchronized double refuel(double requestedKg) {
        if (!Double.isFinite(requestedKg) || requestedKg < 0)
            throw new IllegalArgumentException("Refuel amount must be finite and non-negative");
        double added = Math.min(capacityKg - fuelKg, requestedKg);
        fuelKg += added;
        return added;
    }

    public synchronized double fuelKg() { return fuelKg; }
    public double capacityKg() { return capacityKg; }
    public synchronized double fractionRemaining() { return fuelKg / capacityKg; }
    public synchronized boolean isEmpty() { return fuelKg <= 1.0e-9; }
}
