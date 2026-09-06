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
| Online leaderboard | **N/A / deferred** | Host parked/for-sale (AboveDomains HTML); posts gated (`ONLINE_LEADERBOARD_ENABLED=false`) |
| Sustained full runs | **Pass** | Jump/King prior; Archery 12s+; Flappy idle 13s+; Arena ~13s combat |



## Sustained Archery / Flappy / Arena (2026-09-05 evening PT / 2026-09-06 ~02:00 UTC)

### Root causes (this pass)
1. **Flappy / Run** port advanced the course every tick instead of Forge’s `doIncreaseDistance()` (only when player is ahead on −Z). Idle/slow runs left the sliding room in ~4s → instant Game Over.
2. **Teleport sync**: prefer `ServerPlayer.connection.teleport(...)` over `teleportTo` alone.
3. **Archery** room padding was tight on the 13×13 watchtower deck; first chicken wave could land before a 10s idle shot (spawn cadence lengthened).

### Fixes
- Restore Forge `doIncreaseDistance` for ChallengeNine (Flappy) and ChallengeEight (Run).
- Face Flappy/Run spawns north (yaw 180); longer room grace; Flappy spawn `y+2`.
- Archery/Archery-TA: padded `closeToGameRoom`, spawn `y+26`, `waitTime=500`, delayed first chicken (`toGoTicks=40`).
- Arena/Madness: grace + slight bound pad; `connection.teleport` helper with optional yaw.

### Sustained playtest evidence
| Challenge | Result | Evidence |
|-----------|--------|----------|
| 5 Archery | **12s+** on tower, Score sidebar active (bow/arrows), no room Game Over | `playtest-shots/archery-sustain-t0.png`, `archery-sustain-6s.png`, `archery-sustain-12s.png` |
| 9 Flappy | **13s+ idle** Score still 0 / sidebar active (course does not advance until ahead) | `playtest-shots/flappy-sustain-t0.png`, `flappy-sustain-5s.png`, `flappy-sustain-10s.png`, `flappy-sustain-12s.png` |
| 3 Arena | **~13s** Score 1 in arena before combat death (not instant room exit) | `playtest-shots/arena-sustain-t0.png`, `arena-sustain-10s.png` |

Prior Jump ~15s / King 20s+ still valid. Active Flappy scoring run earlier reached score **59** before a skill/wall death (~8s) — confirms advance-on-ahead path works.

## Still broken / open

- Sustained `/challenge`: Jump/King/Archery/Flappy/Arena verified 10s+ (see tables). Skill deaths and pause-cancel still end runs.
- Pause-during-run cancel verified (Escape ends challenge). Exhaustive survival crafting of all 5 recipes not filmed this pass.
- Night Vision (or any potion) aborts challenges by design (`resetPlayer`); do not use NV for playtests.

## Documented N/A (not Open)

### Online leaderboard (closed 2026-09-05 ~7:35 PM PT)

Legacy Forge posted AES-encrypted scores to `http://minecraftcreations.com/scorepostc/` and showed world ranks at `/cN`; `UpdateThread` pulled personal highs from `highscore.php` and an update flag from `challenge7.txt`.

**Evidence (host dead, not flaky):**
- DNS: `minecraftcreations.com` → `103.224.182.239`
- `GET /`, `POST /scorepostc/`, `GET /c1`, `GET /scorepostc/highscore.php?player=test`, `GET /challenge7.txt` → all HTTP **200**, ~**1045** bytes, identical AboveDomains **for-sale** parking HTML
- Not a portable API; a local file/NBT board would not recreate global rankings

**Port disposition:** **N/A / deferred**. Default `ScoreThread.ONLINE_LEADERBOARD_ENABLED=false` (opt-in `-Dchallenge.onlineLeaderboard=true` if revived). `/retry` reports the same status. In-session sidebar “Current Highscore” still updates in memory. Mostly OK unchanged.

Client left running in `chalplay` on DISPLAY=:3 after prior playtest pass.
