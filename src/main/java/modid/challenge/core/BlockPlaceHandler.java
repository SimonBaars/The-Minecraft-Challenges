package modid.challenge.core;

import modid.challenge.structureloader.DropFuncBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class BlockPlaceHandler {
	public static void placeBlocks(Level worldIn, Level serverWorld, Block block, int posx, int posy, int posz, int sizex, int sizey, int sizez) {
		for (int x = 0; x < sizex; x++) {
			for (int y = 0; y < sizey; y++) {
				for (int z = 0; z < sizez; z++) {
					BlockPos pos = new BlockPos(posx - x, posy + y, posz - z);
					DropFuncBlock.setBlock(serverWorld, block.defaultBlockState(), pos, true, false);
				}
			}
		}
	}
}
