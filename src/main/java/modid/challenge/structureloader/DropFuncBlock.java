package modid.challenge.structureloader;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DropFuncBlock {
	public static boolean setBlock(Level world, BlockState state, BlockPos pos, boolean update, boolean isLive) {
		try {
			int flags = update ? 3 : 2;
			world.setBlock(pos, state, flags);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
