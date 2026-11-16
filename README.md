# Create: Flightworks

A Minecraft 1.21.1 / NeoForge addon project for aircraft engineering.

## Current implementation

- NeoForge mod entry point and metadata.
- SI-unit flight force model: dynamic pressure, lift, drag, thrust, weight and net accelerations.
- Engine output model: throttle, RPM, power, torque, fuel consumption and health scaling.
- JUnit 5 tests for force scaling, idle engine behavior, throttle response and invalid inputs.
- GitHub Actions Java 21 build with a JAR and a separate sources JAR.

## Build

```bash
gradle --no-daemon clean build
```

Artifacts are uploaded by GitHub Actions after tests pass.

## Integration roadmap

The current physics classes are deterministic calculation components only. They do not move entities or create a second flight simulation. Next steps are verified API adapters for Aeronautics/Sable sublevels, registered engine/propeller blocks and block entities, persistent aircraft configuration, then cockpit instruments and GeckoLib visuals.

## Target

- Minecraft 1.21.1
- Java 21
- NeoForge 21.1.x
- Mod ID: `create_flightworks`
- Main package: `com.flightworks`


## Additional simulation modules

The next implementation layer adds:

- `FlightControls`: validated normalized pilot inputs for elevator, aileron, rudder, throttle and flaps, plus landing-gear state.
- `AerodynamicModel`: configurable lift curve, lift limit, soft stall loss, induced drag and flap lift/drag.
- `PropellerModel`: converts shaft power to a bounded static/forward-flight thrust estimate.
- `FuelTank`: synchronized fuel consumption/refuelling with capacity and empty-tank protection.

JUnit tests cover stall behaviour, flap effects, propeller output and fuel limits. These remain deterministic simulation/domain classes; they are not yet wired to Minecraft blocks, Create kinetic networks, or Sable sublevel force submission.


## Flight controls, cockpit and landing gear

- `ControlSurfaceModel` maps elevator, aileron and rudder deflections to signed pitch/roll/yaw moments.
- `FlightComputer` converts authoritative telemetry into an immutable cockpit instrument snapshot, including density-corrected indicated airspeed and normalized heading.
- `LandingGearModel` computes spring-damper contact force and bottom-out state for the host physics integration.
- Added JUnit coverage for control moments, instrument calculations and landing-gear contact behaviour.

These are still integration-ready domain models, not registered Minecraft blocks or a completed Aeronautics/Sable adapter. The Sable API callback shape was checked against its upstream source; the next integration step is to add a version-pinned dependency and implement the callback in a real BlockEntity once the matching artifact coordinates and Create/Aeronautics versions are verified.
