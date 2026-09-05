package modid.challenge.challenges;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import modid.challenge.core.BlockPlaceHandler;
import modid.challenge.core.ChallengeMod;
import modid.challenge.core.ClientHooks;
import modid.challenge.core.ScoreThread;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.network.chat.Component;

public abstract class Challenges {
	protected Level worldIn;
	protected Level serverWorld;
	protected int x, y, z;
	protected ServerPlayer[] players;
	protected ArrayList<ServerPlayer> alivePlayers = new ArrayList<>();
	protected ArrayList<ServerPlayer> deadPlayers = new ArrayList<>();
	protected ArrayList<Item> items = new ArrayList<>();
	public long lastTickTime = 0;
	protected GameType defGameType;
	public int waitTime = 2000;
	public int numberOfPlayers;
	protected int score = 0;
	protected GameType oldGameType = GameType.SURVIVAL;
	protected Difficulty defDifficulty;
	protected ArrayList<ItemStack> oldInventory = new ArrayList<>();
	protected ScoreAccess displayScore;
	protected ScoreAccess displayHighscore;
	protected Objective scoreBoard;

	public Challenges(int x, int y, int z, GameType defGameType, Difficulty defDifficulty) {
		this.defGameType = defGameType;
		this.x = x;
		this.y = y;
		this.z = z;
		this.defDifficulty = defDifficulty;
		ChallengeMod.eventHandler.challenge = this;
		ClientHooks.setDifficulty(defDifficulty);
		this.worldIn = ClientHooks.clientLevel();
		this.serverWorld = ClientHooks.overworld();
		var local = ClientHooks.localPlayer();
		if (local != null) {
			for (ItemStack stack : local.getInventory().getNonEquipmentItems()) {
				oldInventory.add(stack.copy());
			}
		}
		if (serverWorld instanceof net.minecraft.server.level.ServerLevel sl && local != null) {
			var box = local.getBoundingBox().inflate(128.0);
			for (Mob mob : sl.getEntitiesOfClass(Mob.class, box)) {
				mob.discard();
			}
			for (ItemEntity item : sl.getEntitiesOfClass(ItemEntity.class, box)) {
				item.discard();
			}
			sl.getGameRules().set(GameRules.SPAWN_MOBS, false, ClientHooks.integratedServer());
			sl.getGameRules().set(GameRules.SPAWN_MONSTERS, false, ClientHooks.integratedServer());
		}
		ClientHooks.chat("The challenge has started!");
		numberOfPlayers = ClientHooks.playerCount();
		if (numberOfPlayers == 0) {
			System.out.println("Something went wrong while initializing players...");
		}
		List<ServerPlayer> serverPlayers = ClientHooks.serverPlayers();
		players = serverPlayers.toArray(new ServerPlayer[0]);
		for (ServerPlayer player : players) {
			this.oldGameType = player.gameMode();
			alivePlayers.add(player);
			player.setGameMode(defGameType);
		}
		lastTickTime = System.currentTimeMillis();
		Scoreboard board = worldIn.getScoreboard();
		Objective existing = board.getObjective("Score");
		if (existing != null) {
			board.removeObjective(existing);
		}
		scoreBoard = board.addObjective("Score", ObjectiveCriteria.DUMMY, Component.literal("Score"), ObjectiveCriteria.RenderType.INTEGER, false, null);
		board.setDisplayObjective(DisplaySlot.SIDEBAR, scoreBoard);
		displayScore = board.getOrCreatePlayerScore(ScoreHolder.forNameOnly("Score"), scoreBoard);
		displayScore.set(0);
		displayHighscore = board.getOrCreatePlayerScore(ScoreHolder.forNameOnly("Current Highscore"), scoreBoard);
		displayHighscore.set(ChallengeMod.highscores[Math.max(0, getChallengeNum() - 1)]);
	}

	public int getScore() {
		return -(score / 10);
	}

	public boolean resetPlayer() {
		if (y > 150) {
			ClientHooks.chat("You cannot create this challenge this high...");
			removeThisChallenge();
			return true;
		}
		if (serverWorld != null && serverWorld.getDifficulty() != defDifficulty) {
			ClientHooks.chat("Please do not change the difficulty...");
			removeThisChallenge();
			return true;
		}
		for (ServerPlayer player : players) {
			ItemStack selected = player.getInventory().getSelectedItem();
			if (selected.isEmpty() || selected.getItem() != Items.BOW) {
				player.getInventory().clearContent();
				for (Item item : items) {
					player.getInventory().add(new ItemStack(item, item.getDefaultMaxStackSize()));
				}
			}
			player.getFoodData().setFoodLevel(20);
			if (alivePlayers.contains(player) && player.gameMode() != defGameType) {
				ClientHooks.chat("You cannot do this challenge in any other gamemode than survival...");
				removeThisChallenge();
				return true;
			}
			if (deadPlayers.contains(player) && player.gameMode() != GameType.SPECTATOR) {
				ClientHooks.chat("You be in any other gamemode than SPECTATOR now...");
				removeThisChallenge();
				return true;
			}
			if (!player.getActiveEffects().isEmpty()) {
				ClientHooks.chat("You cannot do this challenge when potion effects are active");
				removeThisChallenge();
				return true;
			}
			if (alivePlayers.contains(player) && !withinGameRoom((int) player.getX(), (int) player.getY(), (int) player.getZ())) {
				System.out.println("You left the gameroom? (this might be by error)");
				endChallenge(player);
				return true;
			}
		}
		var local = ClientHooks.localPlayer();
		if (local != null) {
			ItemStack selected = local.getInventory().getSelectedItem();
			if (selected.isEmpty() || selected.getItem() != Items.BOW) {
				local.getInventory().clearContent();
				for (Item item : items) {
					local.getInventory().add(new ItemStack(item, item.getDefaultMaxStackSize()));
				}
			}
		}
		if (numberOfPlayers != ClientHooks.playerCount()) {
			ClientHooks.chat("No players may leave or join the game while a challenge is running.");
			removeThisChallenge();
			return true;
		}
		return false;
	}

	void increaseScore() {
		score -= 10;
	}

	boolean withinGameRoom(int x, int y, int z) {
		return closeToGameRoom(0, x, y, z);
	}

	abstract boolean closeToGameRoom(int howClose, int x, int y, int z);

	void showScore() {
		if (displayScore != null) {
			displayScore.set(getScore());
		}
	}

	public void removeThisChallenge() {
		if (ClientHooks.localPlayer() != null) {
			for (ServerPlayer player : players) {
				player.setGameMode(oldGameType);
				player.getInventory().clearContent();
				int j = 0;
				for (ItemStack stack : oldInventory) {
					if (j < player.getInventory().getNonEquipmentItems().size()) {
						player.getInventory().setItem(j, stack.copy());
					}
					j++;
				}
			}
			try {
				var file = ClientHooks.worldSaveDir();
				if (file != null) {
					Files.deleteIfExists(file.resolve("challenge.txt"));
				}
			} catch (Exception ignored) {
			}
			if (scoreBoard != null && worldIn != null) {
				worldIn.getScoreboard().removeObjective(scoreBoard);
			}
			if (serverWorld instanceof net.minecraft.server.level.ServerLevel sl) {
				sl.getGameRules().set(GameRules.SPAWN_MOBS, true, ClientHooks.integratedServer());
				sl.getGameRules().set(GameRules.SPAWN_MONSTERS, true, ClientHooks.integratedServer());
			}
			destroy();
		}
		ChallengeMod.eventHandler.challenge = null;
	}

	abstract void destroy();

	public void placeBlocks(Block block, int posx, int posy, int posz, int sizex, int sizey, int sizez) {
		BlockPlaceHandler.placeBlocks(worldIn, serverWorld, block, posx, posy, posz, sizex, sizey, sizez);
	}

	public abstract boolean run();

	public void endChallenge(Player entityIn) {
		ServerPlayer deadPlayer = ClientHooks.serverPlayerByName(entityIn.getName().getString());
		if (deadPlayer == null) {
			deadPlayer = ClientHooks.serverPlayerFor(entityIn);
		}
		if (deadPlayer != null && alivePlayers.contains(deadPlayer)) {
			int challengenum = getChallengeNum();
			ClientHooks.chat("It's Game Over for " + entityIn.getName().getString() + "!");
			ClientHooks.chat(entityIn.getName().getString() + " has ended with a score of " + getScore());
			ScoreThread scoreThread = new ScoreThread(getScore(), challengenum, entityIn);
			scoreThread.start();
			if (getScore() > ChallengeMod.highscores[challengenum - 1]) {
				ChallengeMod.highscores[challengenum - 1] = getScore();
			}
			alivePlayers.remove(deadPlayer);
			deadPlayers.add(deadPlayer);
		}
		if (alivePlayers.isEmpty()) {
			removeThisChallenge();
		} else if (deadPlayer != null) {
			deadPlayer.setGameMode(GameType.SPECTATOR);
		}
	}

	private int getChallengeNum() {
		if (this instanceof ChallengeOne) return 1;
		if (this instanceof ChallengeTwo) return 2;
		if (this instanceof ChallengeThree) return 3;
		if (this instanceof ChallengeFour) return 4;
		if (this instanceof ChallengeFive) return 5;
		if (this instanceof ChallengeSix) return 6;
		if (this instanceof ChallengeSeven) return 7;
		if (this instanceof ChallengeEight) return 8;
		if (this instanceof ChallengeNine) return 9;
		return 1;
	}

	public void endChallengeForAllPlayers() {
		for (ServerPlayer player : players) {
			endChallenge(player);
		}
	}
}
