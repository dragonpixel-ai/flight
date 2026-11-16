package com.flightworks.flightdeck;

/** Immutable instrument snapshot. All speeds are metres/second and altitude is metres. */
public record FlightInstruments(
        double indicatedAirspeedMps,
        double altitudeM,
        double verticalSpeedMps,
        double headingDegrees,
        double pitchDegrees,
        double bankDegrees,
        double engineRpm,
        double fuelFraction,
        boolean stallWarning,
        boolean gearDown
) {
    public FlightInstruments {
        finite(indicatedAirspeedMps, "indicatedAirspeedMps");
        finite(altitudeM, "altitudeM");
        finite(verticalSpeedMps, "verticalSpeedMps");
        finite(headingDegrees, "headingDegrees");
        finite(pitchDegrees, "pitchDegrees");
        finite(bankDegrees, "bankDegrees");
        finite(engineRpm, "engineRpm");
        if (!Double.isFinite(fuelFraction) || fuelFraction < 0 || fuelFraction > 1)
            throw new IllegalArgumentException("fuelFraction must be between 0 and 1");
        headingDegrees = normalizeHeading(headingDegrees);
    }

    public static double normalizeHeading(double degrees) {
        if (!Double.isFinite(degrees)) throw new IllegalArgumentException("Heading must be finite");
        double normalized = degrees % 360.0;
        return normalized < 0 ? normalized + 360.0 : normalized;
    }

    private static void finite(double value, String name) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException(name + " must be finite");
    }
}
