# PLAYTEST RESULTS — The-Minecraft-Challenges 2.0.0+26.2

Date: 2026-09-05 evening (PT) / 2026-09-06 early UTC  
World: `chalplay` (singleplayer, Creative/Survival)  
Client: `JAVA_HOME=/workspace/jdk-25` `./gradlew runClient` (offline FabricMC / Player692 — no Microsoft login)

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
| Sustained full runs | **Fragile** | `/challenge` often ends within ~1–2s (`moved wrongly` / left gameroom / fall) after start |

## Still broken / open

- Sustained play after `/challenge` start is unreliable (instant Game Over common); place+right-click starter is more stable (verified for Jump previously).
- Leaderboard posting cannot succeed while the legacy host is a parked page (chat spam fixed in source; needs client restart to pick up).
- Pause-during-run cancel and exhaustive survival crafting of all 5 recipes not filmed this pass.
- Night Vision (or any potion) aborts challenges by design (`resetPlayer`); do not use NV for playtests.

Client left running in `chalplay` on DISPLAY used for this agent.
