package modid.challenge.structureloader;

import java.util.ArrayList;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPlacer {
	private final Level world;
	private final ArrayList<BlockPos> updatePos = new ArrayList<>();
	private final ArrayList<BlockState> updateState = new ArrayList<>();
	private final boolean isLive;

	public BlockPlacer(Level world, boolean isLive) {
		this.world = world;
		this.isLive = isLive;
	}

	public boolean add(BlockState blockState, BlockPos blockPos) {
		boolean blockAdded = DropFuncBlock.setBlock(this.world, blockState, blockPos, false, isLive);
		this.updatePos.add(blockPos);
		this.updateState.add(blockState);
		return blockAdded;
	}

	public void update() {
		for (int i = 0; i < this.updatePos.size(); i++) {
			BlockPos pos = this.updatePos.get(i);
			world.sendBlockUpdated(pos, world.getBlockState(pos), this.updateState.get(i), 3);
		}
	}
}
