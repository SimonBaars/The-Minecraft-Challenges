# Building The Minecraft Challenges — Fabric 26.2

## Requirements

- **Java 25** (JDK 25.0.4.1 or later)
- **Gradle** 9.5.1+ (wrapper included)
- **Fabric Loom** 1.17.20 (configured in build)

## Quick Build

```bash
export JAVA_HOME=/path/to/jdk-25
./gradlew clean build
```

**Output:** `build/libs/challenge-2.0.0+26.2.jar` (140KB)

## Compilation

```bash
./gradlew compileJava
```

The build includes a `verifyMinecraftJar` task that checks the minecraft-merged jar before compilation:

```
> Task :verifyMinecraftJar
Found minecraft-merged jar: .gradle/loom-cache/minecraftMaven/net/minecraft/minecraft-merged-043a8b3edf/26.2/minecraft-merged-043a8b3edf-26.2.jar
Size: 35937781 bytes

> Task :compileJava

BUILD SUCCESSFUL
```

## Troubleshooting

### Empty/Stub minecraft-merged Jar

**Symptom:** Build fails with:
```
> Task :verifyMinecraftJar FAILED
minecraft-merged jar is suspiciously small (22 bytes)
```

**Cause:** Loom cache corruption or incomplete download. The jar is an empty EOCD stub (~22 bytes) instead of the full merged Minecraft jar (~35-36 MB).

**Fix:** Wipe Loom caches and rebuild:

```bash
rm -rf ~/.gradle/caches/fabric-loom .gradle/loom-cache
./gradlew clean compileJava
```

This forces Loom to re-download and re-merge the Minecraft client+server jars from Mojang. The `verifyMinecraftJar` task should then pass with `Size: 35937781 bytes`.

**If problem persists after cache wipe:** Check network connectivity to `https://piston-meta.mojang.com` and `https://piston-data.mojang.com` (Loom downloads official Minecraft jars from these endpoints).

## Project Structure

- **Minecraft**: 26.2 (unobfuscated, no mappings needed)
- **Fabric Loader**: 0.19.5
- **Fabric API**: 0.159.0+26.2
- **Architecture**: Client-only mod (singleplayer / integrated server)
- **Source sets**: Single `main` source set (no `splitEnvironmentSourceSets()`)

All gameplay code uses client classes (`Minecraft`, `LocalPlayer`, `IntegratedServer`) as this is a client-side singleplayer mod.

## Artifacts

```bash
./gradlew build
```

Produces:
- `build/libs/challenge-2.0.0+26.2.jar` — Mod jar (140KB)
- `build/libs/challenge-2.0.0+26.2-sources.jar` — Sources jar (92KB)

## Development

Run the client:
```bash
./gradlew runClient
```

This launches Minecraft 26.2 with the mod loaded in the `chalplay` world (via `--quickPlaySingleplayer` program arg).

## Verification

To verify the minecraft-merged jar without compilation:

```bash
./gradlew verifyMinecraftJar
```

Expected output shows jar size ~35-36 MB. If < 1000 bytes, the jar is a stub and caches should be wiped.
