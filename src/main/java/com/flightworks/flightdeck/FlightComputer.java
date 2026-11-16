package com.flightworks.flightdeck;

/** Produces a cockpit instrument snapshot from authoritative server-side aircraft telemetry. */
public final class FlightComputer {
    private FlightComputer() {}

    public static FlightInstruments update(Telemetry telemetry) {
        if (telemetry == null) throw new IllegalArgumentException("telemetry cannot be null");
        double indicatedSpeed = Math.sqrt(Math.max(0.0,
                2.0 * telemetry.airDensityKgPerM3() / 1.225
                        * telemetry.trueAirspeedMps() * telemetry.trueAirspeedMps()));
        return new FlightInstruments(
                indicatedSpeed,
                telemetry.altitudeM(),
                telemetry.verticalSpeedMps(),
                telemetry.headingDegrees(),
                telemetry.pitchDegrees(),
                telemetry.bankDegrees(),
                telemetry.engineRpm(),
                telemetry.fuelFraction(),
                telemetry.stall(),
                telemetry.gearDown()
        );
    }

    public record Telemetry(double trueAirspeedMps, double airDensityKgPerM3,
                            double altitudeM, double verticalSpeedMps,
                            double headingDegrees, double pitchDegrees, double bankDegrees,
                            double engineRpm, double fuelFraction,
                            boolean stall, boolean gearDown) {
        public Telemetry {
            nonNegative(trueAirspeedMps, "trueAirspeedMps");
            if (!Double.isFinite(airDensityKgPerM3) || airDensityKgPerM3 <= 0)
                throw new IllegalArgumentException("airDensityKgPerM3 must be finite and > 0");
            finite(altitudeM, "altitudeM");
            finite(verticalSpeedMps, "verticalSpeedMps");
            finite(headingDegrees, "headingDegrees");
            finite(pitchDegrees, "pitchDegrees");
            finite(bankDegrees, "bankDegrees");
            nonNegative(engineRpm, "engineRpm");
            if (!Double.isFinite(fuelFraction) || fuelFraction < 0 || fuelFraction > 1)
                throw new IllegalArgumentException("fuelFraction must be between 0 and 1");
        }
        private static void nonNegative(double v, String n) {
            if (!Double.isFinite(v) || v < 0) throw new IllegalArgumentException(n + " must be finite and >= 0");
        }
        private static void finite(double v, String n) {
            if (!Double.isFinite(v)) throw new IllegalArgumentException(n + " must be finite");
        }
    }
}
