package com.flightworks.physics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ControlSurfaceModelTest {
    @Test void deflectionsProduceSignedMoments() {
        var model = new ControlSurfaceModel(0.8, 0.12, 0.08);
        var moments = model.evaluate(new FlightControls(1, -1, 0.5, 0, 0, true),
                500, 16, 1.5, 10);
        assertTrue(moments.pitchNewtonMetres() > 0);
        assertTrue(moments.rollNewtonMetres() < 0);
        assertTrue(moments.yawNewtonMetres() > 0);
    }
}
