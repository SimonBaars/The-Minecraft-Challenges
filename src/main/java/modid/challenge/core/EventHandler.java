package modid.challenge.core;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;

import modid.challenge.challenges.ChallengeNine;
import modid.challenge.challenges.Challenges;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EventHandler {
	public Challenges challenge;

	public void onServerTick() {
		if (!ChallengeMod.updateChecked && ClientHooks.clientLevel() != null) {
			load();
			ChallengeMod.updateChecked = true;
		}

		if (challenge != null && ClientHooks.localPlayer() != null) {
			if (System.currentTimeMillis() > challenge.lastTickTime + challenge.waitTime) {
				if (!challenge.run() && challenge != null) {
					challenge.lastTickTime = System.currentTimeMillis();
				}
			}
		}
	}

	public void onClientTick() {
		if (ClientHooks.isPaused()) {
			if (challenge != null) {
				challenge.removeThisChallenge();
				ClientHooks.chat("No pausing while a challenge is running.");
			}
		}
	}

	public void onFlap() {
		if (challenge instanceof ChallengeNine) {
			var player = ClientHooks.localPlayer();
			if (player != null) {
				player.moveRelative(1.1F, new Vec3(0.0, 0.0, 1.1));
				player.setDeltaMovement(player.getDeltaMovement().x, 1.0, player.getDeltaMovement().z);
			}
			if (player != null) {
				var serverPlayer = ClientHooks.serverPlayerFor(player);
				if (serverPlayer != null) {
					serverPlayer.moveRelative(1.1F, new Vec3(0.0, 0.0, 1.1));
					serverPlayer.setDeltaMovement(serverPlayer.getDeltaMovement().x, 1.0, serverPlayer.getDeltaMovement().z);
				}
			}
		}
	}

	public void load() {
		if (!ChallengeMod.updateChecked) {
			ChallengeMod.updateChecked = true;
			new UpdateThread().start();
		}
		if (challenge != null && ClientHooks.localPlayer() != null) {
			challenge.removeThisChallenge();
			challenge = null;
			System.out.println("Removed unclosed challenge");
		} else if (fileExists()) {
			try {
				Path file = challengeFile();
				String[] array = new String[6];
				try (BufferedReader in = Files.newBufferedReader(file)) {
					for (int j = 0; j < array.length; j++) {
						array[j] = in.readLine();
					}
				}
				var world = ClientHooks.clientLevel();
				var serverWorld = ClientHooks.overworld();
				if (world != null && serverWorld != null) {
					BlockPlaceHandler.placeBlocks(world, serverWorld, Blocks.AIR,
						Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]),
						Integer.parseInt(array[3]), Integer.parseInt(array[4]), Integer.parseInt(array[5]));
				}
				Files.deleteIfExists(file);
				challenge = null;
				if (ClientHooks.localPlayer() != null) {
					ClientHooks.localPlayer().getInventory().clearContent();
				}
			} catch (Exception ignored) {
			}
		}
	}

	boolean fileExists() {
		Path f = challengeFile();
		return f != null && Files.isRegularFile(f);
	}

	@Nullable
	Path challengeFile() {
		Path root = ClientHooks.worldSaveDir();
		return root == null ? null : root.resolve("challenge.txt");
	}
}
