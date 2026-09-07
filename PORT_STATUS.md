# PORT_STATUS — The Minecraft Challenges

Target: Fabric Minecraft **26.2** / Loader **0.19.5** / API **0.159.0+26.2** / Java **25**

| Area | Status | Notes |
|------|--------|-------|
| Build | Green | `./gradlew build` with `JAVA_HOME=/workspace/jdk-25` |
| Start smoke (1–9) | Pass | `/challenge` + creative starters |
| Sustained runs | Pass | `connection.teleport` + grace; Jump/King prior; **Archery/Flappy/Arena** re-verified 10s+ |
| Online leaderboard | **N/A / deferred** | `minecraftcreations.com` parked/for-sale (AboveDomains); posts gated off |
| Schematic metadata | **Fixed** | `LegacyBlockStates` mapper (from IMS) applied; load keeps numeric id+meta, place uses `fromLegacy` |

## Documented N/A (not Open)

- **Global online leaderboard** (`ScoreThread` → `http://minecraftcreations.com/scorepostc/`, boards at `/c1`–`/c9`, `highscore.php`, `challenge7.txt`) — host dead, not temporarily down. Evidence 2026-09-05 ~7:35 PM PT: DNS `103.224.182.239`; `GET /`, `POST /scorepostc/`, `GET /c1`, `GET .../highscore.php`, `GET /challenge7.txt` all HTTP 200 ~1045 B identical for-sale HTML (`abovedomains.com` / “domain may be for sale”). No portable API. Default `ONLINE_LEADERBOARD_ENABLED=false` (opt-in `-Dchallenge.onlineLeaderboard=true` if revived). In-session sidebar highs still update; no local file/NBT world-rank substitute (would not be the legacy global board).

See `PLAYTEST_RESULTS.md` for playtest evidence.
