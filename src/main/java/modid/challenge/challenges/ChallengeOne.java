package modid.challenge.challenges;

import java.io.PrintWriter;
import java.nio.file.Files;

import modid.challenge.core.ChallengeMod;
import modid.challenge.core.ClientHooks;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

public class ChallengeOne extends Challenges {
	int jumpRow = 0;
	int runwaySize = 20;
	int runwayWidth = 4;
	final int maxHeight = 9;
	int rowSize = 1;
	int currentHeight = 1;
	boolean randomlyPlaced = false;
	boolean runwaySmaller = false;

	public ChallengeOne(int x, int y, int z) {
		super(x, y, z, GameType.ADVENTURE, Difficulty.PEACEFUL);
		showScore();
		firstRow();
		// Center of runway floor (placeBlocks grows -X/-Z from origin)
		teleportPlayers(x - (runwayWidth / 2) - 1, y + 1, z - (runwaySize / 2));
		resetPlayer();
	}

	void firstRow() {
		placeBlocks(Blocks.AIR, x - (runwayWidth / 2), y - 2, z - (runwaySize / 4), runwayWidth, maxHeight, runwaySize);
		placeBlocks(ChallengeMod.BlockTouchable, x - (runwayWidth / 2), y - 2, z - (runwaySize / 4), runwayWidth, 1, runwaySize);
		placeBlocks(ChallengeMod.BlockTouchable, x - (runwayWidth / 2), y - 2, z - (runwaySize / 4) + 1, runwayWidth, maxHeight, 1);
		placeBlocks(ChallengeMod.BlockBlueRock, x - (runwayWidth / 2) + 1, y - 1, z - (runwaySize / 4), 1, maxHeight - 1, runwaySize);
		placeBlocks(ChallengeMod.BlockBlueRock, x - ((int) (3.00 * (runwayWidth / 2.00))), y - 1, z - (runwaySize / 4), 1, maxHeight - 1, runwaySize);
		placeBlocks(ChallengeMod.BlockTouchable, x - (runwayWidth / 2) + 1, y - 2, z - ((int) (5.00 * (runwaySize / 4.00))), runwayWidth + 2, maxHeight, 1);
		placeBlocks(ChallengeMod.BlockBlackRock, x - (runwayWidth / 2), y - 1, z - (runwaySize / 4), runwayWidth, 1, runwaySize);
	}

	void placeRow(boolean isJumpRow) {
		int score = getScore();
		int rowZ = z - ((int) (5.00 * (runwaySize / 4.00))) - score;
		placeBlocks(Blocks.AIR, x - (runwayWidth / 2), y - 1, rowZ, runwayWidth, maxHeight, 1);
		placeBlocks(Blocks.AIR, x - (runwayWidth / 2) + 1, y - 2, z - (runwaySize / 4) + 2 - score, runwayWidth + 2, maxHeight, 1);
		placeBlocks(ChallengeMod.BlockTouchable, x - (runwayWidth / 2), y - 2, z - (runwaySize / 4) - score + 1, runwayWidth, maxHeight, 2);
		placeBlocks(ChallengeMod.BlockBlueRock, x - (runwayWidth / 2) + 1, y - 1, rowZ, 1, maxHeight - 1, 1);
		placeBlocks(ChallengeMod.BlockBlueRock, x - ((int) (3.00 * (runwayWidth / 2.00))), y - 1, rowZ, 1, maxHeight - 1, 1);
		placeBlocks(ChallengeMod.BlockTouchable, x - (runwayWidth / 2) + 1, y - 2, rowZ - 1, runwayWidth + 2, maxHeight, 1);
		if (isJumpRow) {
			if (randomlyPlaced) {
				placeBlocks(ChallengeMod.BlockBlackRock, x - (runwayWidth / 2) - ((int) (Math.random() * runwayWidth)), y - 2 + currentHeight, rowZ, 1, 1, 1);
			} else {
				placeBlocks(ChallengeMod.BlockBlackRock, x - (runwayWidth / 2), y - 2 + currentHeight, rowZ, runwayWidth, 1, 1);
			}
		}
	}

	@Override
	void destroy() {
		placeBlocks(Blocks.AIR, x - (runwayWidth / 2) + 1, y - 2, z - (runwaySize / 4) - getScore() + 2, runwayWidth + 2, maxHeight + 1, runwaySize + 4);
	}

	@Override
	boolean closeToGameRoom(int howClose, int x, int y, int z) {
		x = x - this.x + (runwaySize / 4) + 1 - howClose;
		y = y - this.y + 2 - howClose;
		z = z - this.z + ((int) (5.00 * (runwaySize / 4.00))) + 1 + getScore() - howClose;
		return (x >= 0 && x <= runwayWidth + 2 + (2 * howClose) && y >= 0 && y <= maxHeight + 2 + (2 * howClose) && z >= 0 && z <= runwaySize + 2 + (2 * howClose));
	}

	@Override
	public boolean run() {
		if (waitTime == 2000) {
			waitTime = 700;
		}
		if (ClientHooks.localPlayer() != null) {
			if (resetPlayer()) {
				return true;
			}
			if (jumpRow == 0) {
				currentHeight += calculateRandomHeight();
			}
			placeRow(jumpRow == 0);
			increaseScore();
			showScore();
			waitTime--;
			int score = getScore();
			switch (getScore()) {
				case 50, 10, 90, 300 -> randomlyPlaced = true;
				case 30, 70 -> { rowSize++; randomlyPlaced = false; }
				case 120 -> { randomlyPlaced = true; rowSize = 2; }
				default -> {}
			}
			if (score >= 100 && score < 120) {
				randomlyPlaced = ((int) (Math.random() * 2)) == 0;
				rowSize = ((int) (Math.random() * 3)) + 1;
			} else if (score >= 130 && score <= 250 && score % 10 == 0) {
				randomlyPlaced = ((int) (Math.random() * 2)) == 0;
				rowSize = ((int) (Math.random() * 3)) + 1;
			} else if (score >= 260 && score < 300) {
				rowSize = 3;
				randomlyPlaced = ((int) (Math.random() * 2)) == 0;
			}
			if (runwaySmaller && jumpRow != 0) {
				runwaySize--;
				runwaySmaller = false;
			}
			jumpRow++;
			if (jumpRow >= rowSize) {
				jumpRow = 0;
			}
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
				writer.println(x - (runwayWidth / 2) + 1);
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

	private int calculateRandomHeight() {
		if (currentHeight == 1) {
			return (int) (Math.random() * 2);
		} else if (currentHeight == maxHeight - 3) {
			return -1 + ((int) (Math.random() * 2));
		}
		return -1 + ((int) (Math.random() * 3));
	}
}
