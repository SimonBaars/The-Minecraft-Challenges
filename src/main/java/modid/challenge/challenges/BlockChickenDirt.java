package modid.challenge.challenges;

import com.mojang.serialization.MapCodec;
import modid.challenge.core.ChallengeMod;
import modid.challenge.core.ClientHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockChickenDirt extends Block {
	private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);

	public BlockChickenDirt(BlockBehaviour.Properties properties) {
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
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		if (level.isClientSide() || ChallengeMod.eventHandler.challenge == null) {
			return;
		}
		if (ChallengeMod.eventHandler.challenge instanceof ChallengeFive five) {
			if (entity instanceof Chicken && five.activeMonsters.contains(entity)) {
				ClientHooks.chat("A chicken hit the dirt!");
				ChallengeMod.eventHandler.challenge.endChallengeForAllPlayers();
			}
		} else if (ChallengeMod.eventHandler.challenge instanceof ChallengeSix six) {
			if (entity instanceof Chicken && six.activeMonsters.contains(entity)) {
				six.activeMonsters.remove(entity);
				entity.discard();
			}
		}
	}
}
