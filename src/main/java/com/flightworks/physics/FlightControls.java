package com.flightworks.physics;

/** Pilot inputs. Control deflections are normalized to [-1, 1], throttle/flaps to [0, 1]. */
public record FlightControls(
        double elevator,
        double aileron,
        double rudder,
        double throttle,
        double flaps,
        boolean landingGearDown
) {
    public FlightControls {
        unitSigned(elevator, "elevator");
        unitSigned(aileron, "aileron");
        unitSigned(rudder, "rudder");
        unitInterval(throttle, "throttle");
        unitInterval(flaps, "flaps");
    }

    public static FlightControls neutral() {
        return new FlightControls(0, 0, 0, 0, 0, true);
    }

    private static void unitSigned(double value, String name) {
        if (!Double.isFinite(value) || value < -1 || value > 1)
            throw new IllegalArgumentException(name + " must be between -1 and 1");
    }

    private static void unitInterval(double value, String name) {
        if (!Double.isFinite(value) || value < 0 || value > 1)
            throw new IllegalArgumentException(name + " must be between 0 and 1");
    }
}
