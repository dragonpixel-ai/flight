# Create: Flightworks

Minecraft 1.21.1 / NeoForge 21.1.x addon project for aircraft engineering built around Create and compatible physics-based sublevel systems.

## Development baseline

- Minecraft: 1.21.1
- Java: 21
- NeoForge: 21.1.219
- Mod ID: `create_flightworks`
- Main package: `com.flightworks`

## Build

```bash
gradle --no-daemon clean build
```

GitHub Actions builds pushes and pull requests to `main`, and supports manual runs from the Actions tab. Successful builds publish the mod JAR as a workflow artifact.

## Engineering constraints

- Integrate with the host aircraft/sublevel physics implementation rather than creating a competing flight simulation.
- Do not teleport aircraft or overwrite their velocity as a flight-control shortcut.
- Keep client rendering and animation code isolated from dedicated-server initialization.
- Add optional integrations only after exact target API versions are verified.
