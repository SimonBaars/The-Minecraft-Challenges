package modid.challenge.challenges;

import com.mojang.serialization.MapCodec;
import modid.challenge.core.ChallengeMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockChallengeThree extends Block {
	public BlockChallengeThree(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return MapCodec.unit(this);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!level.isClientSide() && ChallengeMod.eventHandler.challenge == null) {
			new ChallengeThree(pos.getX(), pos.getY(), pos.getZ());
		}
		return InteractionResult.SUCCESS;
	}
}
