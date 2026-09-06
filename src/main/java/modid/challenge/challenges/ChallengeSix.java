package modid.challenge.challenges;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;

import modid.challenge.core.ChallengeMod;
import modid.challenge.core.ClientHooks;
import modid.challenge.structureloader.SchematicStructure;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

public class ChallengeSix extends Challenges {
	int sizex = 40;
	int sizey = 5;
	int sizez = 40;
	int towersizex;
	int towersizez;
	int randGrootte = 4;
	public ArrayList<Entity> activeMonsters = new ArrayList<>();
	int wave = 0;
	final int nWaves = 20;
	private int toGoTicks = 40; // ~20s before first chicken @ waitTime 500
	long startTime;


	public ChallengeSix(int x, int y, int z) {
		super(x, y, z, GameType.SURVIVAL, Difficulty.PEACEFUL);
		showScore();
		initArena();
		teleportPlayers(x, y + 26, z);
		resetPlayer();
		startTime = System.currentTimeMillis();
		waitTime = 500;
		roomGraceMs = 4000;
	}

	void initArena() {
		items.add(Items.BOW);
		for (int i = 0; i < 8; i++) items.add(Items.ARROW);
		SchematicStructure structure = new SchematicStructure("watchtower");
		structure.readFromFile();
		sizey += structure.height;
		placeBlocks(ChallengeMod.BlockChickenDirt, x + (sizex / 2) - 1, y, z + (sizez / 2) - 1, sizex, 1, sizez);
		placeBlocks(Blocks.AIR, x + (sizex / 2) - 1, y + 1, z + (sizez / 2) - 1, sizex, sizey + 1, sizez);
		towersizex = structure.width;
		towersizez = structure.length;
		structure.process(serverWorld, worldIn, x + structure.width / 2, y + 1, z + structure.length / 2);
		ChallengeMod.checkMode = true;
	}

	void spawnMob() {
		if (!(serverWorld instanceof ServerLevel sl)) return;
		int amount = Math.max(1, numberOfPlayers);
		for (int i = 0; i < amount; i++) {
			Chicken chicken = EntityTypes.CHICKEN.create(serverWorld, EntitySpawnReason.EVENT);
			if (chicken == null) continue;
			switch ((int) (Math.random() * 4)) {
				case 0 -> chicken.snapTo(this.x + ((int) (Math.random() * (sizex / randGrootte))) + ((randGrootte - 1) * (sizex / randGrootte)) - 1 - (sizex / 2), y + sizey, this.z + ((int) (Math.random() * sizez)) - 1 - (sizez / 2), 0, 0);
				case 1 -> chicken.snapTo(this.x + ((int) (Math.random() * sizex)) - 1 - (sizex / 2), y + sizey, this.z + ((int) (Math.random() * (sizez / randGrootte))) + ((randGrootte - 1) * (sizez / randGrootte)) - 1 - (sizez / 2), 0, 0);
				case 2 -> chicken.snapTo(this.x + ((int) (Math.random() * (sizex / randGrootte))) - 1 - (sizex / 2), y + sizey, this.z + ((int) (Math.random() * sizez)) - 1 - (sizez / 2), 0, 0);
				default -> chicken.snapTo(this.x + ((int) (Math.random() * sizez)) - 1 - (sizex / 2), y + sizey, this.z + ((int) (Math.random() * (sizez / randGrootte))) - 1 - (sizez / 2), 0, 0);
			}
			chicken.setHealth(1);
			sl.addFreshEntity(chicken);
			activeMonsters.add(chicken);
		}
	}

	@Override
	void destroy() {
		placeBlocks(Blocks.AIR, x + (sizex / 2) - 1, y, z + (sizez / 2) - 1, sizex, sizey, sizez);
		for (Entity e : activeMonsters) e.discard();
		ChallengeMod.checkMode = false;
	}

	@Override
	boolean closeToGameRoom(int howClose, int x, int y, int z) {
		int tx = Math.max(4, towersizex);
		int tz = Math.max(4, towersizez);
		x = x - this.x + (tx / 2) + 2 - howClose;
		y = y - this.y - 23 - howClose;
		z = z - this.z + (tz / 2) + 2 - howClose;
		return x >= 0 && x <= tx + 4 + (2 * howClose) && y >= 0 && y <= 12 + (2 * howClose) && z >= 0 && z <= tz + 4 + (2 * howClose);
	}

	@Override
	public boolean run() {
		if (ClientHooks.localPlayer() == null) return false;
		if (resetPlayer()) return true;
		long left = 120 - (System.currentTimeMillis() - startTime) / 1000;
		if (left <= 0) {
			ClientHooks.chat("Time's up!");
			endChallengeForAllPlayers();
			return true;
		}

		Iterator<Entity> it = activeMonsters.iterator();
		while (it.hasNext()) {
			Entity e = it.next();
			if (!e.isAlive()) {
				it.remove();
				increaseScore();
				showScore();
			}
		}
		toGoTicks--;
		if (toGoTicks <= 0) {
			toGoTicks = 10;
			wave++;
			spawnMob();
			register();
		}
		return false;
	}

	void register() {
		try {
			var dir = ClientHooks.worldSaveDir();
			if (dir == null) return;
			Files.createDirectories(dir);
			try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(dir.resolve("challenge.txt")))) {
				writer.println(x + (sizex / 2) - 1);
				writer.println(y);
				writer.println(z + (sizez / 2) - 1);
				writer.println(sizex);
				writer.println(sizey);
				writer.println(sizez);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
