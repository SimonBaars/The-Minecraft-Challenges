package modid.challenge.challenges;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;

import modid.challenge.core.ChallengeMod;
import modid.challenge.core.ClientHooks;
import modid.challenge.core.MobFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

public class ChallengeSeven extends Challenges {
	int sizex = 30;
	int sizey = 5;
	int sizez = 30;
	final int fieldx = 10;
	final int fieldy = 5;
	final int fieldz = 10;
	int cornerx;
	int cornerz;
	public ArrayList<Mob> activeMonsters = new ArrayList<>();
	int wave = 0;
	final int nWaves = 20;

	public ChallengeSeven(int x, int y, int z) {
		super(x, y, z, GameType.ADVENTURE, Difficulty.HARD);
		showScore();
		createArena();
		cornerx = x + (fieldx / 2) - 1;
		cornerz = z + (fieldy / 2) - 1; // forge uses fieldy here
		teleportPlayers(x, y + 2, z);
		resetPlayer();
	}

	void createArena() {
		placeBlocks(ChallengeMod.BlockTouchable, x + (sizex / 2) - 1, y, z + (sizez / 2) - 1, sizex, 1, sizez);
		placeBlocks(Blocks.AIR, x + (sizex / 2) - 1, y + 1, z + (sizex / 2) - 1, sizex, sizey - 1, sizez);
		placeBlocks(ChallengeMod.BlockBlackRock, x + (fieldx / 2) - 1, y + 1, z + (fieldz / 2) - 1, fieldx, 1, fieldz);
	}

	void spawnMobs(int monsterId, int amount) {
		amount = (amount * ((wave / nWaves) + 1)) * Math.max(1, numberOfPlayers);
		if (!(serverWorld instanceof ServerLevel sl)) return;
		for (int i = 0; i < amount; i++) {
			Mob monster = MobFactory.create(monsterId, serverWorld);
			if (monster == null) continue;
			monster.snapTo(cornerx - ((int) (Math.random() * (fieldx - 2))) - 1, y + 3, cornerz + 3 - ((int) (Math.random() * (fieldz - 2))) - 1, 0, 0);
			sl.addFreshEntity(monster);
			activeMonsters.add(monster);
		}
	}

	@Override
	void destroy() {
		placeBlocks(Blocks.AIR, x + (sizex / 2) - 1, y, z + (sizez / 2) - 1, sizex, sizey, sizez);
		for (Mob m : activeMonsters) m.discard();
	}

	@Override
	boolean closeToGameRoom(int howClose, int x, int y, int z) {
		x = x - cornerx + fieldx - howClose;
		y = y - this.y - 1 - howClose;
		z = z - cornerz + fieldz - howClose - 2;
		return x >= 0 && x <= fieldx + (2 * howClose) + 2 && y >= 0 && y <= fieldy + (2 * howClose) && z >= 0 && z <= fieldz + (2 * howClose) + 2;
	}

	@Override
	public boolean run() {
		if (ClientHooks.localPlayer() == null) return false;
		if (resetPlayer()) return true;
		Iterator<Mob> it = activeMonsters.iterator();
		while (it.hasNext()) {
			Mob e = it.next();
			if (!e.isAlive()) it.remove();
		}
		if (activeMonsters.isEmpty()) {
			wave++;
			increaseScore();
			showScore();
			spawnMobs(wave % 17, 2 + wave / 3);
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
