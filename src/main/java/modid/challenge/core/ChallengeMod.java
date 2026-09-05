package modid.challenge.core;

import java.util.function.Function;

import modid.challenge.challenges.BlockBlackRock;
import modid.challenge.challenges.BlockBlueRock;
import modid.challenge.challenges.BlockChallengeEight;
import modid.challenge.challenges.BlockChallengeFive;
import modid.challenge.challenges.BlockChallengeFour;
import modid.challenge.challenges.BlockChallengeNine;
import modid.challenge.challenges.BlockChallengeOne;
import modid.challenge.challenges.BlockChallengeSeven;
import modid.challenge.challenges.BlockChallengeSix;
import modid.challenge.challenges.BlockChallengeThree;
import modid.challenge.challenges.BlockChallengeTwo;
import modid.challenge.challenges.BlockChickenDirt;
import modid.challenge.challenges.BlockTouchable;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChallengeMod implements ModInitializer {
	public static final String MOD_ID = "challenge";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean updateChecked = true;
	public static boolean checkMode = false;
	public static int[] highscores = new int[9];
	public static EventHandler eventHandler;

	public static Block BLOCK_TOUCHABLE;
	public static Block BLOCK_CHALLENGE_ONE;
	public static Block BLOCK_CHALLENGE_TWO;
	public static Block BLOCK_CHALLENGE_THREE;
	public static Block BLOCK_CHALLENGE_FOUR;
	public static Block BLOCK_CHALLENGE_FIVE;
	public static Block BLOCK_CHALLENGE_SIX;
	public static Block BLOCK_CHALLENGE_SEVEN;
	public static Block BLOCK_CHALLENGE_EIGHT;
	public static Block BLOCK_CHALLENGE_NINE;
	public static Block BLOCK_BLACK_ROCK;
	public static Block BLOCK_CHICKEN_DIRT;
	public static Block BLOCK_BLUE_ROCK;

	public static Block BlockTouchable;
	public static Block BlockChallengeOne;
	public static Block BlockChallengeTwo;
	public static Block BlockChallengeThree;
	public static Block BlockChallengeFour;
	public static Block BlockChallengeFive;
	public static Block BlockChallengeSix;
	public static Block BlockChallengeSeven;
	public static Block BlockChallengeEight;
	public static Block BlockChallengeNine;
	public static Block BlockBlackRock;
	public static Block BlockChickenDirt;
	public static Block BlockBlueRock;

	public static final ResourceKey<CreativeModeTab> CHALLENGES_TAB =
		ResourceKey.create(Registries.CREATIVE_MODE_TAB, id("challenges"));

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing The Minecraft Challenges for Minecraft 26.2");
		registerBlocks();
		registerCreativeTab();
		eventHandler = new EventHandler();
		registerEvents();
		ChallengeCommands.register();
		LOGGER.info("The Minecraft Challenges initialized");
	}

	private void registerBlocks() {
		BLOCK_TOUCHABLE = registerBlock("block_touchable", BlockTouchable::new, true, false);
		BLOCK_CHALLENGE_ONE = registerBlock("block_challenge_one", BlockChallengeOne::new, false, false);
		BLOCK_CHALLENGE_TWO = registerBlock("block_challenge_two", BlockChallengeTwo::new, false, false);
		BLOCK_CHALLENGE_THREE = registerBlock("block_challenge_three", BlockChallengeThree::new, false, false);
		BLOCK_CHALLENGE_FOUR = registerBlock("block_challenge_four", BlockChallengeFour::new, false, false);
		BLOCK_CHALLENGE_FIVE = registerBlock("block_challenge_five", BlockChallengeFive::new, false, false);
		BLOCK_CHALLENGE_SIX = registerBlock("block_challenge_six", BlockChallengeSix::new, false, false);
		BLOCK_CHALLENGE_SEVEN = registerBlock("block_challenge_seven", BlockChallengeSeven::new, false, false);
		BLOCK_CHALLENGE_EIGHT = registerBlock("block_challenge_eight", BlockChallengeEight::new, false, false);
		BLOCK_CHALLENGE_NINE = registerBlock("block_challenge_nine", BlockChallengeNine::new, false, false);
		BLOCK_BLACK_ROCK = registerBlock("block_black_rock", BlockBlackRock::new, true, true);
		BLOCK_CHICKEN_DIRT = registerBlock("block_chicken_dirt", BlockChickenDirt::new, true, false);
		BLOCK_BLUE_ROCK = registerBlock("block_blue_rock", BlockBlueRock::new, true, false);

		BlockTouchable = BLOCK_TOUCHABLE;
		BlockChallengeOne = BLOCK_CHALLENGE_ONE;
		BlockChallengeTwo = BLOCK_CHALLENGE_TWO;
		BlockChallengeThree = BLOCK_CHALLENGE_THREE;
		BlockChallengeFour = BLOCK_CHALLENGE_FOUR;
		BlockChallengeFive = BLOCK_CHALLENGE_FIVE;
		BlockChallengeSix = BLOCK_CHALLENGE_SIX;
		BlockChallengeSeven = BLOCK_CHALLENGE_SEVEN;
		BlockChallengeEight = BLOCK_CHALLENGE_EIGHT;
		BlockChallengeNine = BLOCK_CHALLENGE_NINE;
		BlockBlackRock = BLOCK_BLACK_ROCK;
		BlockChickenDirt = BLOCK_CHICKEN_DIRT;
		BlockBlueRock = BLOCK_BLUE_ROCK;
	}

	private void registerCreativeTab() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CHALLENGES_TAB,
			CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
				.title(Component.translatable("itemGroup.challenge.challenges"))
				.icon(() -> new ItemStack(BLOCK_CHALLENGE_ONE))
				.displayItems((params, output) -> {
					output.accept(BLOCK_CHALLENGE_ONE.asItem());
					output.accept(BLOCK_CHALLENGE_TWO.asItem());
					output.accept(BLOCK_CHALLENGE_THREE.asItem());
					output.accept(BLOCK_CHALLENGE_FOUR.asItem());
					output.accept(BLOCK_CHALLENGE_FIVE.asItem());
					output.accept(BLOCK_CHALLENGE_SIX.asItem());
					output.accept(BLOCK_CHALLENGE_SEVEN.asItem());
					output.accept(BLOCK_CHALLENGE_EIGHT.asItem());
					output.accept(BLOCK_CHALLENGE_NINE.asItem());
				})
				.build());
	}

	private void registerEvents() {
		ServerLevelEvents.LOAD.register((server, world) -> updateChecked = false);
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (eventHandler != null) eventHandler.onServerTick();
		});
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			if (entity instanceof net.minecraft.world.entity.player.Player player
				&& eventHandler != null && eventHandler.challenge != null) {
				eventHandler.challenge.endChallenge(player);
			}
		});
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
			if (eventHandler != null && eventHandler.challenge != null) {
				Block b = state.getBlock();
				if (b == BLOCK_TOUCHABLE || b == BLOCK_BLACK_ROCK || b == BLOCK_BLUE_ROCK || b == BLOCK_CHICKEN_DIRT) {
					player.sendSystemMessage(Component.literal("Don't break challenge related blocks while a challenge is running"));
					eventHandler.challenge.removeThisChallenge();
					return false;
				}
			}
			return true;
		});
	}

	private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, boolean unbreakable, boolean glow) {
		Identifier id = id(name);
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
		BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
			.mapColor(glow ? MapColor.COLOR_BLACK : MapColor.STONE)
			.sound(SoundType.STONE)
			.setId(blockKey);
		if (unbreakable) {
			props = props.strength(-1.0F, 3600000.0F);
		} else {
			props = props.strength(1.0F);
		}
		if (glow) {
			props = props.lightLevel(s -> 15);
		}
		Block block = factory.apply(props);
		Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
		Item.Properties itemProps = new Item.Properties().useBlockDescriptionPrefix().setId(itemKey);
		Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, itemProps));
		return block;
	}
}
