package modid.challenge.core;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public final class MobFactory {
	private MobFactory() {}

	public static @Nullable Mob create(int monsterId, Level world) {
		return switch (monsterId) {
			case 0 -> EntityTypes.ZOMBIE.create(world, EntitySpawnReason.EVENT);
			case 1 -> EntityTypes.SPIDER.create(world, EntitySpawnReason.EVENT);
			case 2 -> EntityTypes.BLAZE.create(world, EntitySpawnReason.EVENT);
			case 3 -> EntityTypes.CAVE_SPIDER.create(world, EntitySpawnReason.EVENT);
			case 4 -> EntityTypes.CREEPER.create(world, EntitySpawnReason.EVENT);
			case 5 -> EntityTypes.ENDERMAN.create(world, EntitySpawnReason.EVENT);
			case 6 -> EntityTypes.ENDERMITE.create(world, EntitySpawnReason.EVENT);
			case 7 -> EntityTypes.GIANT.create(world, EntitySpawnReason.EVENT);
			case 8 -> EntityTypes.GUARDIAN.create(world, EntitySpawnReason.EVENT);
			case 9 -> EntityTypes.MAGMA_CUBE.create(world, EntitySpawnReason.EVENT);
			case 10 -> EntityTypes.ZOMBIFIED_PIGLIN.create(world, EntitySpawnReason.EVENT);
			case 11 -> EntityTypes.SILVERFISH.create(world, EntitySpawnReason.EVENT);
			case 12 -> EntityTypes.SKELETON.create(world, EntitySpawnReason.EVENT);
			case 13 -> EntityTypes.SLIME.create(world, EntitySpawnReason.EVENT);
			case 14 -> EntityTypes.WITCH.create(world, EntitySpawnReason.EVENT);
			case 15 -> EntityTypes.WOLF.create(world, EntitySpawnReason.EVENT);
			case 16 -> EntityTypes.RABBIT.create(world, EntitySpawnReason.EVENT);
			default -> null;
		};
	}
}
