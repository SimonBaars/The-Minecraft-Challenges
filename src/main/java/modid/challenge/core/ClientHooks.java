package modid.challenge.core;

import java.nio.file.Path;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import org.jspecify.annotations.Nullable;

public final class ClientHooks {
	private ClientHooks() {}

	public static Minecraft mc() {
		return Minecraft.getInstance();
	}

	public static @Nullable LocalPlayer localPlayer() {
		return mc().player;
	}

	public static @Nullable Level clientLevel() {
		return mc().level;
	}

	public static @Nullable IntegratedServer integratedServer() {
		return mc().getSingleplayerServer();
	}

	public static @Nullable ServerLevel overworld() {
		IntegratedServer server = integratedServer();
		return server == null ? null : server.overworld();
	}

	public static void chat(String message) {
		LocalPlayer player = localPlayer();
		if (player != null) {
			player.sendSystemMessage(Component.literal(message));
		}
	}

	public static boolean isPaused() {
		return mc().isPaused();
	}

	public static @Nullable Path worldSaveDir() {
		IntegratedServer server = integratedServer();
		if (server == null) {
			return null;
		}
		return server.getWorldPath(LevelResource.ROOT);
	}

	public static List<ServerPlayer> serverPlayers() {
		IntegratedServer server = integratedServer();
		if (server == null) {
			return List.of();
		}
		return server.getPlayerList().getPlayers();
	}

	public static int playerCount() {
		IntegratedServer server = integratedServer();
		return server == null ? 0 : server.getPlayerCount();
	}

	public static @Nullable ServerPlayer serverPlayerFor(Player player) {
		IntegratedServer server = integratedServer();
		if (server == null) {
			return null;
		}
		return server.getPlayerList().getPlayer(player.getUUID());
	}

	public static @Nullable ServerPlayer serverPlayerByName(String name) {
		IntegratedServer server = integratedServer();
		if (server == null) {
			return null;
		}
		return server.getPlayerList().getPlayerByName(name);
	}

	public static void setDifficulty(Difficulty difficulty) {
		IntegratedServer server = integratedServer();
		if (server != null) {
			server.setDifficulty(difficulty, true);
		}
	}
}
