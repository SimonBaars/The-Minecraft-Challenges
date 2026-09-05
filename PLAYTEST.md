# PLAYTEST — The Minecraft Challenges 2.0.0+26.2

Environment: Minecraft **26.2**, Fabric Loader **0.19.5**, Fabric API **0.159.0+26.2**, Java **25**.

## Setup

- [ ] Install Fabric Loader 0.19.5 for 26.2
- [ ] Install Fabric API 0.159.0+26.2
- [ ] Install `challenge-2.0.0+26.2.jar`
- [ ] Launch a **singleplayer** creative or survival world (challenges expect an integrated server)
- [ ] Open creative inventory → **Challenges** tab lists Jump / Arena / Archery / King / Run / Flappy starters

## Creative inventory and blocks

| Block | Expected name | Texture | Pass |
|-------|---------------|---------|------|
| JumpChallenge | JumpChallenge | jump | [ ] |
| JumpChallenge Time Attack | JumpChallenge - Time Attack | jump | [ ] |
| Arena | Arena | arena | [ ] |
| Arena Madness | Arena - Madness Mode | arena | [ ] |
| Archery | Archery | archery | [ ] |
| Archery Time Attack | Archery - Time Attack | archery | [ ] |
| King of the Hill | King of the Hill | king | [ ] |
| Run 'n Dodge | Run 'n Dodge | run | [ ] |
| Flappy Parcours | Flappy Parcours | flappy | [ ] |

## Starting challenges

| Method | Steps | Expected | Pass |
|--------|-------|----------|------|
| Right-click starter | Place starter block, right-click | Challenge starts; sidebar Score appears | [ ] |
| `/challenge <1-9> <x> <y> <z>` | Run from chat | Same as placing corresponding starter | [ ] |
| Too high Y | Start with y>150 | Aborts with height message | [ ] |
| Pause during run | Open pause menu | Challenge cancelled; chat warning | [ ] |

## Per-challenge smoke tests

| # | Name | Smoke check | Pass |
|---|------|-------------|------|
| 1 | Jump | Runway builds; score increases; leaving room / touching walls ends run | [ ] |
| 2 | Jump Time Attack | Same as 1; 120s timer; ends on timeout | [ ] |
| 3 | Arena | Arena schematic places; waves spawn; sword loadout | [ ] |
| 4 | Arena Madness | Harder waves / madness defaults | [ ] |
| 5 | Archery | Watchtower + dirt floor; chickens spawn; bow/arrows given | [ ] |
| 6 | Archery Time Attack | Same as 5 with time limit | [ ] |
| 7 | King of the Hill | Platform + mobs; score on clears | [ ] |
| 8 | Run 'n Dodge | Corridor with openings; score advances | [ ] |
| 9 | Flappy | Press **G** to flap; gaps with glowstone markers | [ ] |

## Crafting (survival)

| Recipe | Result | Pass |
|--------|--------|------|
| oak planks + oak fence pattern | block_challenge_one | [ ] |
| glowstone dust + planks + fence | block_challenge_two | [ ] |
| stone/sand/diamond sword/torch | block_challenge_three | [ ] |
| stone/sand/iron sword/torch | block_challenge_four | [ ] |
| planks/stone/bow/arrow | block_challenge_five | [ ] |

## Score / retry

| Feature | Steps | Expected | Pass |
|---------|-------|----------|------|
| Finish / die | Complete or fail a challenge | Chat shows score; optional online post | [ ] |
| `/retry` | After a failed post | Retries last score upload | [ ] |

## Known limitations

- Designed for singleplayer; dedicated multiplayer support is incomplete.
- Legacy schematic IDs may not map 1:1 to modern blocks.
- External leaderboard host may be unreachable; use `/retry` after connectivity fixes.
