# Migration: Forge 1.9 → Fabric Minecraft 26.2

Local port only (no GitHub push). Branch: `fabric-26.2`.

## Version matrix

| Component | From (Forge 1.9) | To (Fabric 26.2) |
|-----------|------------------|------------------|
| Minecraft | 1.9 | **26.2** |
| Loader / API | Forge 12.16.0.1859 | Fabric Loader **0.19.5**, Fabric API **0.159.0+26.2** |
| Mappings | MCP snapshot_20160312 | **Official Mojmap** (unobfuscated 26.x) |
| Build | ForgeGradle 2.1 | `net.fabricmc.fabric-loom` **1.17.20** |
| Gradle | (legacy wrapper) | **9.5.1** |
| Java | 8-era | **25** |
| Mod version | 1.0 / mcmod 1.8 | **2.0.0+26.2** |

## Tooling

1. Replaced ForgeGradle with Fabric Loom (`net.fabricmc.fabric-loom`); no Yarn remap on 26.x.
2. Dependencies use `implementation` for loader + Fabric API.
3. `fabric.mod.json` replaces `mcmod.info`; Java `>=25`, Minecraft `~26.2`.
4. Recipes moved to `data/challenge/recipe/`; lang to `en_us.json`; client item models under `assets/challenge/items/`.
5. Block/item registry IDs are snake_case (`block_challenge_one`, …).

## Architecture changes

| Forge 1.9 | Fabric 26.2 |
|-----------|-------------|
| `@Mod` + `@SidedProxy` | `ModInitializer` + `ClientModInitializer` |
| `GameRegistry.registerBlock` | `Registry.register` + `ResourceKey` + `Properties.setId` |
| `GameRegistry.addRecipe` | datapack shaped recipes |
| FML / Forge event buses | Fabric `ServerTickEvents`, `ServerLevelEvents`, `ServerLivingEntityEvents`, `PlayerBlockBreakEvents`, `ClientTickEvents` |
| `ICommand` | Brigadier via `CommandRegistrationCallback` (`/challenge`, `/retry`) |
| LWJGL2 `KeyBinding` + ClientRegistry | `KeyMapping` + `KeyMappingHelper` (fabric-key-mapping-api) |
| `CreativeTabs` | Registered `CreativeModeTab` (`challenge:challenges`) |
| `World` / `EntityPlayer` / `IBlockState` | `Level` / `Player` / `BlockState` |
| Direct chunk `ExtendedBlockStorage` writes | `Level.setBlock` via `DropFuncBlock` |
| Apache HttpClient score post | `HttpURLConnection` |
| Numeric schematic block IDs + metadata | Best-effort `LegacyBlocks` id map (metadata ignored) |

## Gameplay notes

- Challenges remain **singleplayer / integrated-server oriented** (same design as the Forge mod).
- Online score posting **N/A/deferred**: `minecraftcreations.com` is parked/for-sale; gated by `ScoreThread.ONLINE_LEADERBOARD_ENABLED` (default false).
- Arena / watchtower schematics load through legacy `.structure` files; unknown numeric IDs fall back to stone.
- Flappy challenge flap key defaults to **G**.

## Build

```bash
export JAVA_HOME=/path/to/jdk-25
./gradlew build
```

Artifact: `build/libs/challenge-2.0.0+26.2.jar`

Original Forge sources retained under `_forge_src/` for reference (local only).
