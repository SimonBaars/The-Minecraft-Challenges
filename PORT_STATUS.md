# Minecraft mods — Fabric 26.2 port status

Updated: 2026-09-05 ~7:00 PM PT — DOTU: disappear vine+spread + candycane growth filmed (63–65); IMS FreeFall+/ride; JurassiCraft Anky whip/slam; Tractor Mostly OK.

Status meaning: **Open** = still needs faithful finish work. Smoke playtest ≠ finished.

| Mod | Coverage | Status | Open gaps |
|-----|----------|--------|-----------|
| Pig-Companion-Mod | High | Mostly OK | Crafting **PASS** in-game (potato_on_a_stick + upgraded_saddle); string recipes OK; **push still blocked** (local commits only) |
| TNT-Mod | High | Mostly OK | Nuke/Hydrogen playtested (scaled radii on llvmpipe; defaults restored); all 18 recipes unlock + `/reload` OK |
| Instant-Massive-Structures | High structs | Mostly OK | Creative tab + textures; **live ticker**: ferris + mill + watermill + windmill + helicopter + cinema (43/20t) + **FreeFall** (21 frames, variable waits + `/ride` Y-curve); aviation/boat/bus + Ferris cart-path ride still open |
| The-Minecraft-Challenges | High | Mostly OK | All 9 start-smoke; sustained `/challenge` fixed (teleportTo + Arena/King bounds); Jump~15s/King20s+; leaderboard host dead |
| Chat-Bot-Mod | Medium | Mostly OK | SP+all keybinds re-verified; Cleverbot **dead/deferred** (API suspended); LAN **needs 2 clients** (host guest-chat path fixed, untested live) |
| JurassiCraft | Subset | **Open** | Techne + walk/idle + leap/roar/bite + Anky whip/slam + head twitch + most sounds; egg hatch + fossil/amber worldgen; cultivate 2-high; full Animator keyframes / aquatic polish / no-ogg species still open |
| Tractor | High | Mostly OK | Techne model + craft verified; CarMod.txt / engine-wheel **N/A closed** (legacy never registered Item classes; water flag is code constant only) |
| The-Creep | ~65–70% | Mostly OK | Noise+carver creep dim (not full legacy octave/populate); vine cage hardened |
| The-Great-Mushroom-War | Playable | Mostly OK | Instant castle = legacy ~843/844 voxel dump (no separate schematic); Kashroom `KashroomModel` (legacy ModelCreeper; no Techne ModelKashroom) |
| Dimensions-of-the-Unknown | ~76% | Mostly OK | Pads + adventure blocks + tab OK; dims 1–9 noise+carver; SafeTNT primes; trap **6 variants** filmed; **Entity*2 minimal** filmed; **disappear** vine-on-step + mystical-dirt spread tick + **candycane** growth tick filmed (63–65); custom AI / sheep-fur / mega-creeper / Herobrine still open |
| CodeArena | ~43% | **Open** | **Demo AST only** — real AST needs CloneRefactor jar (no standalone checkout/jar under `/workspace`; `_forge_legacy` sources only; do not clone). Swing CodeEditor **N/A/deferred** (Forge desktop UI, not portable to Fabric client). ModelCode* = vanilla only (spider+cave skins done); coliseum opt-in `/place` |

Path: `/workspace/minecraft-mods/PORT_STATUS.md`
