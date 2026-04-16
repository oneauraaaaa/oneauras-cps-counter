# oneaura's CPS Counter

This repository contains two standalone Fabric mod projects:

- `1.21.6-1.21.11/` for Minecraft `1.21.6` through `1.21.11`
- `26.1/` for Minecraft `26.1`

Each folder has its own Gradle setup, wrapper, dependencies, and build lifecycle.

## Build

Legacy project:

```bash
cd 1.21.6-1.21.11
./gradlew build
```

26.1 project:

```bash
cd 26.1
./gradlew build
```

## Jar naming

- Legacy output: `oneauras-cps-counter-v<mod_version>-v1.21.6-1.21.11.jar`
- 26.1 output: `oneauras-cps-counter-v<mod_version>-26.1.jar`
