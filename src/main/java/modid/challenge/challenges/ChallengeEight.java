package modid.challenge.challenges;

import java.io.PrintWriter;
import java.nio.file.Files;

import modid.challenge.core.ChallengeMod;
import modid.challenge.core.ClientHooks;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

public class ChallengeEight extends Challenges {
	int jumpRow = 0;
	int runwaySize = 40;
	int runwayWidth = 10;
	int maxHeight = 8;
	int rowSize = 5;
	boolean randomlyPlaced = false;
	byte runwaySmaller = 0;
	int openingSize = 3;

	public ChallengeEight(int x, int y, int z) {
		super(x, y, z, GameType.ADVENTURE, Difficulty.PEACEFUL);
		showScore();
		firstRow();
		teleportPlayers(x, y + 1, z - (runwaySize / 2));
		waitTime = 200;
		resetPlayer();
	}

	void firstRow() {
		placeBlocks(Blocks.AIR, x + (runwayWidth / 2), y - 2, z - (runwaySize / 4), runwayWidth, maxHeight, runwaySize);
		placeBlocks(ChallengeMod.BlockTouchable, x + (runwayWidth / 2), y - 2, z - (runwaySize / 4), runwayWidth, 1, runwaySize);
		placeBlocks(ChallengeMod.BlockTouchable, x + (runwayWidth / 2), y - 2, z - (runwaySize / 4) + 1, runwayWidth, maxHeight, 1);
		placeBlocks(ChallengeMod.BlockTouchable, x + (runwayWidth / 2) + 1, y - 1, z - (runwaySize / 4), 1, maxHeight - 1, runwaySize);
		placeBlocks(ChallengeMod.BlockTouchable, x - (runwayWidth / 2) + 1, y - 1, z - (runwaySize / 4), 1, maxHeight - 1, runwaySize);
		placeBlocks(ChallengeMod.BlockTouchable, x + (runwayWidth / 2) + 1, y - 2, z - ((int) (5.00 * (runwaySize / 4.00))), runwayWidth + 2, maxHeight, 1);
		placeBlocks(ChallengeMod.BlockBlackRock, x + (runwayWidth / 2), y - 1, z - (runwaySize / 4), runwayWidth, 1, runwaySize);
	}

	void placeRow(boolean isJumpRow) {
		int score = getScore();
		int rowZ = z - ((int) (5.00 * (runwaySize / 4.00))) - score;
		placeBlocks(Blocks.AIR, x + (runwayWidth / 2), y - 1, rowZ, runwayWidth, maxHeight, 1);
		placeBlocks(Blocks.AIR, x + (runwayWidth / 2) + 1, y - 2, z - (runwaySize / 4) + 2 - score, runwayWidth + 2, maxHeight, 1);
		placeBlocks(ChallengeMod.BlockTouchable, x + (runwayWidth / 2), y - 2, z - (runwaySize / 4) - score + 1, runwayWidth, maxHeight, 2);
		placeBlocks(ChallengeMod.BlockTouchable, x + (runwayWidth / 2) + 1, y - 1, rowZ, 1, maxHeight - 1, 1);
		placeBlocks(ChallengeMod.BlockTouchable, x - (runwayWidth / 2) + 1, y - 1, rowZ, 1, maxHeight - 1, 1);
		placeBlocks(ChallengeMod.BlockTouchable, x + (runwayWidth / 2) + 1, y - 2, rowZ - 1, runwayWidth + 2, maxHeight, 1);
		placeBlocks(ChallengeMod.BlockBlackRock, x + (runwayWidth / 2), y - 1, rowZ, runwayWidth, 1, 1);
		if (isJumpRow) {
			placeBlocks(ChallengeMod.BlockTouchable, x + (runwayWidth / 2), y - 1, rowZ, runwayWidth, maxHeight / 2, 1);
			placeBlocks(Blocks.AIR, x + (runwayWidth / 2) - ((int) (Math.random() * (runwayWidth - openingSize))), y - 1, rowZ, openingSize, maxHeight / 2, 1);
		}
	}

	@Override
	void destroy() {
		placeBlocks(Blocks.AIR, x + (runwayWidth / 2) + 1, y - 2, z - (runwaySize / 4) - getScore() + 2, runwayWidth + 2, maxHeight + 1, runwaySize + 4);
	}

	@Override
	boolean closeToGameRoom(int howClose, int x, int y, int z) {
		x = x - this.x + (runwayWidth / 2) - howClose;
		y = y - this.y + 2 - howClose;
		z = z - this.z + ((int) (5.00 * (runwaySize / 4.00))) + 1 + getScore() - howClose;
		return x >= 0 && x <= runwayWidth + 2 + (2 * howClose) && y >= 0 && y <= maxHeight + 2 + (2 * howClose) && z >= 0 && z <= runwaySize + 2 + (2 * howClose);
	}

	@Override
	public boolean run() {
		if (ClientHooks.localPlayer() == null) return false;
		if (resetPlayer()) return true;
		placeRow(jumpRow == 0);
		increaseScore();
		showScore();
		jumpRow++;
		if (jumpRow >= rowSize) jumpRow = 0;
		register();
		return false;
	}

	void register() {
		try {
			var dir = ClientHooks.worldSaveDir();
			if (dir == null) return;
			Files.createDirectories(dir);
			try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(dir.resolve("challenge.txt")))) {
				writer.println(x + (runwayWidth / 2) + 1);
				writer.println(y - 2);
				writer.println(z - (runwaySize / 4) - getScore() + 2);
				writer.println(runwayWidth + 2);
				writer.println(maxHeight + 1);
				writer.println(runwaySize + 4);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
