# PLAYTEST RESULTS — The-Minecraft-Challenges 2.0.0+26.2

Date: 2026-09-05 evening (PT) / 2026-09-06 early UTC  
World: `chalplay` (singleplayer, Creative/Survival)  
Client: `JAVA_HOME=/workspace/jdk-25` `./gradlew runClient` (offline FabricMC / Player692 — no Microsoft login)


## Sustained-run fix session (2026-09-05 evening PT)

### Root cause
1. **`ServerPlayer.snapTo` without client sync** → server logged `moved wrongly!`, rebases the player outside the arena → immediate `left the gameroom` / Game Over (often ~1–2s after `/challenge`).
2. **Arena (3/4) room math diverged from Forge**: `cornerx/z` were `x+32/z+38` with a wrong `closeToGameRoom` formula and schematic process at `(x,y,z)` instead of Forge `(x+32,y-1,z+37)` / field `30×40` / corners `x-15,z-20`. Spawn was outside the playable field by construction.
3. **King (7) room padding** lost Forge’s `y-1` / `z-2` / `+2` edge slack.
4. **Start order**: teleport before placing the runway → brief void; **BlockTouchable** could kill as soon as score>1 once grace ended.

### Fixes
- `ClientHooks.teleportPlayer` → `teleportTo` + zero velocity / reset fall.
- `Challenges.teleportPlayers` + **2.5s room grace** (re-teleport if outside during grace); floor-based coords; BlockTouchable respects grace.
- Place structure/runway **then** teleport; Jump spawn on floor (`y+1`); longer initial `waitTime` on fast-tick challenges.
- Restore Forge Arena/King room bounds + schematic offsets; Arena mob spawn uses Forge `+` offsets.

### Sustained playtest evidence
| Challenge | Result | Evidence |
|-----------|--------|----------|
| 1 Jump | **~15s** run, score **20**, then normal kill-wall Game Over (no `moved wrongly`) | `playtest-shots/jump-live-start.png`, `jump-sustain-10s.png` (score 13), `jump-sustain-end-score20.png` |
| 7 King | **20s+** stable, scoreboard still active (score 1), no Game Over | `playtest-shots/king-sustain-t0.png`, `king-sustain-10s.png`, `king-sustain-20s.png` |

Idle Jump still dies in a few seconds when not running forward (advancing back wall) — expected gameplay, not a port bug.

## Fixes applied this session

- **`ClientHooks.chat`**: schedule onto the client thread (`mc.execute`) so server-tick / ScoreThread calls no longer crash with `Rendersystem called from wrong thread` (repro: potion effects active → abort chat from `resetPlayer`).
- **`ScoreThread`**: sanitize leaderboard HTTP responses so parked-domain HTML is not dumped into chat.
- **Recipes**: add `"count": 1` on all five shaped results (26.2-friendly).

## All 9 challenges — start smoke (PASS)

| # | Name | Evidence | Shot |
|---|------|----------|------|
| 1 | Jump | Prior session: runway + Score sidebar to score 6 | `playtest-shots/jump-started.webp` |
| 2 | Jump Time Attack | Log: `Started challenge 2` + `The challenge has started!` | `playtest-shots/jump-ta-started.webp` |
| 3 | Arena | Log: `Started challenge 3` | `playtest-shots/arena-started.webp` |
| 4 | Arena Madness | Log: `Started challenge 4` | `playtest-shots/arena-madness-started.webp` |
| 5 | Archery | Prior session: bow/arrows + chicken + Score | `playtest-shots/archery-started.webp` |
| 6 | Archery Time Attack | Log: `Started challenge 6` (fell from platform during run) | `playtest-shots/archery-ta-started.webp` |
| 7 | King of the Hill | Log: `Started challenge 7` | `playtest-shots/king-started.webp` |
| 8 | Run 'n Dodge | Log: `Started challenge 8` | `playtest-shots/run-started.webp` |
| 9 | Flappy Parcours | Log: `Started challenge 9`; custom red challenge blocks visible | `playtest-shots/flappy-started.webp` |

Creative tab (9 textured starters): `playtest-shots/creative-tab.webp` (prior). Platform setup: `playtest-shots/setup-platform.webp`.

## Other checks

| Check | Result | Notes |
|-------|--------|-------|
| Height limit `y>150` | **PASS** | Chat: `You cannot create this challenge this high...` — `playtest-shots/height-limit-abort.webp` |
| Recipe datapack load | **PASS** | `Loaded 1590 recipes`; `/recipe give @s challenge:block_challenge_one` → unlocked |
| Recipes 6–9 | N/A | Legacy Forge only registered crafts for challenges 1–5 |
| Full crafting grid craft | **Partial** | Unlock verified; in-GUI craft click not cleanly captured |
| Online leaderboard | **Broken host** | `minecraftcreations.com` returns for-sale HTML (sanitized in code for next launch) |
| Sustained full runs | **Improved** | Jump ~15s score 20; King 20s+ no early Game Over (teleport + bounds fix) |

## Still broken / open

- Sustained `/challenge` after teleport/bounds fix: Jump ~15s / King 20s+ verified; skill deaths and pause-cancel still end runs. Leaderboard host still parked.
- Leaderboard posting cannot succeed while the legacy host is a parked page (chat spam fixed in source; needs client restart to pick up).
- Pause-during-run cancel verified (Escape ends challenge). Exhaustive survival crafting of all 5 recipes not filmed this pass.
- Night Vision (or any potion) aborts challenges by design (`resetPlayer`); do not use NV for playtests.

Client left running in `chalplay` on DISPLAY=:3 after this pass.
