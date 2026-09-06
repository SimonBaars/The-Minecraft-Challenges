# PLAYTEST — The Minecraft Challenges 2.0.0+26.2

Environment: Minecraft **26.2**, Fabric Loader **0.19.5**, Fabric API **0.159.0+26.2**, Java **25**.

## Setup

- [x] Install Fabric Loader 0.19.5 for 26.2
- [x] Install Fabric API 0.159.0+26.2
- [x] Install `challenge-2.0.0+26.2.jar`
- [x] Launch a **singleplayer** creative or survival world (challenges expect an integrated server)
- [x] Open creative inventory → **Challenges** tab lists Jump / Arena / Archery / King / Run / Flappy starters

## Creative inventory and blocks

| Block | Expected name | Texture | Pass |
|-------|---------------|---------|------|
| JumpChallenge | JumpChallenge | jump | [x] |
| JumpChallenge Time Attack | JumpChallenge - Time Attack | jump | [x] |
| Arena | Arena | arena | [x] |
| Arena Madness | Arena - Madness Mode | arena | [x] |
| Archery | Archery | archery | [x] |
| Archery Time Attack | Archery - Time Attack | archery | [x] |
| King of the Hill | King of the Hill | king | [x] |
| Run 'n Dodge | Run 'n Dodge | run | [x] |
| Flappy Parcours | Flappy Parcours | flappy | [x] |

## Starting challenges

| Method | Steps | Expected | Pass |
|--------|-------|----------|------|
| Right-click starter | Place starter block, right-click | Challenge starts; sidebar Score appears | [x] |
| `/challenge <1-9> <x> <y> <z>` | Run from chat | Same as placing corresponding starter | [x] |
| Too high Y | Start with y>150 | Aborts with height message | [x] |
| Pause during run | Open pause menu | Challenge cancelled; chat warning | [x] |

## Per-challenge smoke tests

| # | Name | Smoke check | Pass |
|---|------|-------------|------|
| 1 | Jump | Runway builds; score increases; leaving room / touching walls ends run | [x] |
| 2 | Jump Time Attack | Same as 1; 120s timer; ends on timeout | [x] |
| 3 | Arena | Arena schematic places; waves spawn; sword loadout | [x] |
| 4 | Arena Madness | Harder waves / madness defaults | [x] |
| 5 | Archery | Watchtower + dirt floor; chickens spawn; bow/arrows given | [x] |
| 6 | Archery Time Attack | Same as 5 with time limit | [x] |
| 7 | King of the Hill | Platform + mobs; score on clears | [x] |
| 8 | Run 'n Dodge | Corridor with openings; score advances | [x] |
| 9 | Flappy | Press **G** to flap; gaps with glowstone markers | [x] |

## Crafting (survival)

| Recipe | Result | Pass |
|--------|--------|------|
| oak planks + oak fence pattern | block_challenge_one | [x] |
| glowstone dust + planks + fence | block_challenge_two | [~] |
| stone/sand/diamond sword/torch | block_challenge_three | [~] |
| stone/sand/iron sword/torch | block_challenge_four | [~] |
| planks/stone/bow/arrow | block_challenge_five | [~] |

## Score / retry

| Feature | Steps | Expected | Pass |
|---------|-------|----------|------|
| Finish / die | Complete or fail a challenge | Chat shows score; optional online post | [x] |
| `/retry` | After a failed post | Reports N/A while host gated off | [x] |

## Known limitations

- Designed for singleplayer; dedicated multiplayer support is incomplete.
- Legacy schematic IDs may not map 1:1 to modern blocks.
- Online leaderboard **N/A/deferred** (`minecraftcreations.com` parked/for-sale); posts gated off; `/retry` will not revive a dead host.
- Sustained `/challenge` needs server `teleportTo` (not `snapTo`); opening pause cancels the run by design.
