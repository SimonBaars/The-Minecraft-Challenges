package modid.challenge.challenges;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;

import modid.challenge.core.ChallengeMod;
import modid.challenge.core.ClientHooks;
import modid.challenge.core.MobFactory;
import modid.challenge.structureloader.SchematicStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ChallengeThree extends Challenges {
	int sizex = 65;
	int sizey = 20;
	int sizez = 75;
	int fieldx = 30;
	int fieldy = 5;
	int fieldz = 40;
	int cornerx;
	int cornerz;
	ArrayList<Entity> activeMonsters = new ArrayList<>();
	int wave = 0;
	final int nWaves = 20;
	private SchematicStructure checkStructure;

	public ChallengeThree(int x, int y, int z) {
		super(x, y, z, GameType.SURVIVAL, Difficulty.NORMAL);
		// Forge room: playable field centered on start, schematic offset separately
		cornerx = x - 15;
		cornerz = z - 20;
		showScore();
		initArena();
		teleportPlayers(x, y + 2, z);
		waitTime = 500;
		roomGraceMs = 4000;
		resetPlayer();
	}

	void initArena() {
		SchematicStructure structure = new SchematicStructure("arena");
		structure.readFromFile();
		sizex = structure.length;
		sizey = structure.height;
		sizez = structure.width;
		structure.process(serverWorld, worldIn, x + 32, y - 1, z + 37);
		structure = new SchematicStructure("arenacheck");
		structure.readFromFile();
		structure.isLive = true;
		this.checkStructure = structure;
		ChallengeMod.checkMode = true;
		items.add(Items.WOODEN_SWORD);
	}

	void spawnMobs(int monsterId, int amount) {
		amount = (amount * ((wave / nWaves) + 1)) * Math.max(1, numberOfPlayers);
		if (!(serverWorld instanceof ServerLevel sl)) return;
		for (int i = 0; i < amount; i++) {
			Mob monster = MobFactory.create(monsterId, serverWorld);
			if (monster == null) continue;
			monster.snapTo(cornerx + ((int) (Math.random() * (fieldx - 2))) + 1, y + 2, cornerz + ((int) (Math.random() * (fieldz - 2))) + 1, 0, 0);
			sl.addFreshEntity(monster);
			activeMonsters.add(monster);
		}
	}

	@Override
	void destroy() {
		placeBlocks(Blocks.AIR, x + 32, y - 1, z + 38, sizex, sizey, sizez);
		for (Entity e : activeMonsters) e.discard();
		ChallengeMod.checkMode = false;
	}

	@Override
	boolean closeToGameRoom(int howClose, int x, int y, int z) {
		x = x - cornerx - howClose;
		y = y - this.y - 1 - howClose;
		z = z - cornerz - howClose;
		return x >= -1 && x <= fieldx + 1 + (2 * howClose) && y >= 0 && y <= fieldy + 2 + (2 * howClose) && z >= -1 && z <= fieldz + 3 + (2 * howClose);
	}

	@Override
	public boolean run() {
		if (ClientHooks.localPlayer() == null) return false;
		if (resetPlayer()) return true;
		Iterator<Entity> it = activeMonsters.iterator();
		while (it.hasNext()) {
			Entity e = it.next();
			if (!e.isAlive()) it.remove();
		}
		if (activeMonsters.isEmpty()) {
			wave++;
			increaseScore();
			showScore();
			nextWave();
			register();
		}
		return false;
	}

	void nextWave() {
		items.clear();
		switch (Math.min(wave, 19)) {
			case 0 -> { items.add(Items.WOODEN_SWORD); spawnMobs(16, 2); }
			case 1 -> spawnMobs(0, 3);
			case 2 -> spawnMobs(1, 3);
			case 3 -> spawnMobs(4, 2);
			case 4 -> spawnMobs(12, 2);
			case 5 -> spawnMobs(11, 4);
			case 6 -> spawnMobs(13, 2);
			case 7 -> { items.add(Items.STONE_SWORD); spawnMobs(8, 2); }
			case 8 -> spawnMobs(3, 3);
			case 9 -> spawnMobs(15, 3);
			case 10 -> spawnMobs(14, 2);
			case 11 -> spawnMobs(2, 2);
			case 12 -> spawnMobs(5, 2);
			case 13 -> { items.add(Items.IRON_SWORD); spawnMobs(6, 4); }
			case 14 -> spawnMobs(9, 2);
			case 15 -> spawnMobs(10, 3);
			case 16 -> { items.add(Items.DIAMOND_SWORD); spawnMobs(0, 8); }
			case 17 -> spawnMobs(7, 1);
			case 18 -> spawnMobs(1, 6);
			default -> {
				items.add(Items.FLINT_AND_STEEL);
				int[] finalRound = {0, 1, 2, 4, 6, 9, 10, 11, 12, 13, 15, 16};
				for (int id : finalRound) spawnMobs(id, 2);
			}
		}
	}

	void register() {
		try {
			var dir = ClientHooks.worldSaveDir();
			if (dir == null) return;
			Files.createDirectories(dir);
			try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(dir.resolve("challenge.txt")))) {
				writer.println(x + 32);
				writer.println(y - 1);
				writer.println(z + 38);
				writer.println(sizex);
				writer.println(sizey);
				writer.println(sizez);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
