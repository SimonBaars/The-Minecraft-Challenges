package modid.challenge.challenges;

import com.mojang.serialization.MapCodec;
import modid.challenge.core.ChallengeMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockTouchable extends Block {
	private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);

	public BlockTouchable(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return MapCodec.unit(this);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, net.minecraft.world.entity.InsideBlockEffectApplier effectApplier, boolean unknown) {
		var challenge = ChallengeMod.eventHandler.challenge;
		if (challenge != null
			&& !challenge.inRoomGrace()
			&& (challenge.getScore() > 1 || challenge instanceof ChallengeSeven)
			&& !level.isClientSide()) {
			if (entity instanceof Player player) {
				ChallengeMod.eventHandler.challenge.endChallenge(player);
			} else {
				entity.discard();
			}
		}
	}
}
