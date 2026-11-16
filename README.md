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
