package modid.challenge.structureloader;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StructureUtils {
	public static BlockPos getWorldPos(BlockPos structPos, Vec3 structCenter, Vec3 harvestPos) {
		return BlockPos.containing(getWorldPos(new Vec3(structPos.getX() + 0.5, structPos.getY(), structPos.getZ() + 0.5), structCenter, harvestPos));
	}

	public static Vec3 getWorldPos(Vec3 structPos, Vec3 structCenter, Vec3 harvestPos) {
		return harvestPos.add(structPos).subtract(structCenter);
	}

	public static boolean setBlock(BlockPlacer blockPlacer, BlockState blockState, BlockPos structPos, Vec3 structCenter, Vec3 harvestPos) {
		return blockPlacer.add(blockState, StructureUtils.getWorldPos(structPos, structCenter, harvestPos));
	}

	public static void setEntity(Level world, Entity entity, Vec3 structCenter, Vec3 harvestPos) {
		Vec3 pos = getWorldPos(entity.position(), structCenter, harvestPos);
		entity.snapTo(pos.x, pos.y, pos.z);
		world.addFreshEntity(entity);
	}
}
