package modid.challenge.core;

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
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod (modid = "Challenge",name = "Challenge", version = "1.8")
//@NetworkMod(clientSideRequired = true, serverSideRequired = false)

public class Challenge {
	public static String modid = "Challenge";
	
	public static boolean updateChecked = true;
	
	@SidedProxy(clientSide = "modid.challenge.core.ChallengeClient",serverSide = "modid.challenge.core.ChallengeProxy")
	public static ChallengeProxy proxy;
	
	public static modid.challenge.core.EventHandler eventHandler;
	public static boolean checkMode = false;
	
	public static int[] highscores = new int[9];
	
	public static CreativeTabs Challenges = new CreativeTabs("Challenges"){
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Challenge.BlockChallengeOne);
		}		
	};
	
	public static Block BlockTouchable = new BlockTouchable().setHardness(1.0F).setUnlocalizedName("BlockTouchable");
	public static Block BlockChallengeOne = new BlockChallengeOne(201).setHardness(1.0F).setUnlocalizedName("BlockChallengeOne").setCreativeTab(Challenge.Challenges);
	public static Block BlockChallengeTwo = new BlockChallengeTwo(202).setHardness(1.0F).setUnlocalizedName("BlockChallengeTwo").setCreativeTab(Challenge.Challenges);
	public static Block BlockChallengeThree = new BlockChallengeThree(203).setHardness(1.0F).setUnlocalizedName("BlockChallengeThree").setCreativeTab(Challenge.Challenges);
	public static Block BlockChallengeFour = new BlockChallengeFour(204).setHardness(1.0F).setUnlocalizedName("BlockChallengeFour").setCreativeTab(Challenge.Challenges);
	public static Block BlockChallengeFive = new BlockChallengeFive(205).setHardness(1.0F).setUnlocalizedName("BlockChallengeFive").setCreativeTab(Challenge.Challenges);
	public static Block BlockChallengeSix = new BlockChallengeSix(206).setHardness(1.0F).setUnlocalizedName("BlockChallengeSix").setCreativeTab(Challenge.Challenges);
	public static Block BlockChallengeSeven = new BlockChallengeSeven(207).setHardness(1.0F).setUnlocalizedName("BlockChallengeSeven").setCreativeTab(Challenge.Challenges);
	public static Block BlockChallengeEight = new BlockChallengeEight(208).setHardness(1.0F).setUnlocalizedName("BlockChallengeEight").setCreativeTab(Challenge.Challenges);
	public static Block BlockChallengeNine = new BlockChallengeNine(209).setHardness(1.0F).setUnlocalizedName("BlockChallengeNine").setCreativeTab(Challenge.Challenges);
	/*public static Block BlockChallengeTen = new BlockChallengeTen(210).setHardness(1.0F).setUnlocalizedName("BlockChallengeTen").setCreativeTab(Challenge.Challenges);*/
	public static Block BlockBlackRock = new BlockBlackRock().setHardness(1.0F).setLightLevel(1.0F).setUnlocalizedName("BlockBlackRock");
	public static Block BlockChickenDirt = new BlockChickenDirt().setHardness(1.0F).setUnlocalizedName("BlockChickenDirt");
	public static Block BlockBlueRock = new BlockBlueRock().setHardness(1.0F).setUnlocalizedName("BlockBlueRock");

	public  void reg(Block block) {
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
	
	public void regAll(){		
		reg(BlockTouchable);
		reg(BlockChallengeOne);
		reg(BlockChallengeTwo);
		reg(BlockChallengeThree);
		reg(BlockChallengeFour);
		reg(BlockChallengeFive);
		reg(BlockChallengeSix);
		reg(BlockChallengeSeven);
		reg(BlockChallengeEight);
		reg(BlockChallengeNine);
		/*reg(BlockChallengeTen);*/
		reg(BlockBlackRock);
		reg(BlockChickenDirt);
		reg(BlockBlueRock);

	}
	
	@EventHandler
	public void stopWorldEvent(FMLServerStoppingEvent event){
		if(Challenge.eventHandler.challenge!=null){
			Challenge.eventHandler.challenge.removeThisChallenge();
		}
	}

@EventHandler
	public void init(FMLInitializationEvent event){
	eventHandler=new modid.challenge.core.EventHandler();
	FMLCommonHandler.instance().bus().register(eventHandler);
	MinecraftForge.EVENT_BUS.register(new modid.challenge.core.ForgeEventHandler());
	KeyBindings.init();
	regAll();
	proxy.registerRenderInformation();
	/*
		regAll();
		System.out.println("Challenge has rendered sooooo much blocks");
*/
	}
	   
	   @EventHandler
		public void preInit(FMLPreInitializationEvent e){
		   
		proxy.registerRenderInformation();
		
		



		GameRegistry.registerBlock(BlockTouchable, "BlockTouchable");
		GameRegistry.registerBlock(BlockChallengeOne, "BlockChallengeOne");
		GameRegistry.registerBlock(BlockChallengeTwo, "BlockChallengeTwo");
		GameRegistry.registerBlock(BlockChallengeThree, "BlockChallengeThree");
		GameRegistry.registerBlock(BlockChallengeFour, "BlockChallengeFour");
		GameRegistry.registerBlock(BlockChallengeFive, "BlockChallengeFive");
			GameRegistry.registerBlock(BlockChallengeSix, "BlockChallengeSix");
		GameRegistry.registerBlock(BlockChallengeSeven, "BlockChallengeSeven");
		GameRegistry.registerBlock(BlockChallengeEight, "BlockChallengeEight");
		GameRegistry.registerBlock(BlockChallengeNine, "BlockChallengeNine");
		/*GameRegistry.registerBlock(BlockChallengeTen, "BlockChallengeTen");*/
		GameRegistry.registerBlock(BlockBlackRock, "BlockBlackRock");
		GameRegistry.registerBlock(BlockChickenDirt, "BlockChickenDirt");
		GameRegistry.registerBlock(BlockBlueRock, "BlockBlueRock");

		GameRegistry.addRecipe(new ItemStack(BlockChallengeOne, 1), new Object[] { 
			      "A A", "B B",    Character.valueOf('A'), Blocks.planks, Character.valueOf('B'), Blocks.oak_fence, Character.valueOf('C'), Blocks.grass, Character.valueOf('D'), Blocks.golden_rail, Character.valueOf('E'), Blocks.redstone_torch, Character.valueOf('F'), Blocks.stone_slab
			      });
		
		GameRegistry.addRecipe(new ItemStack(BlockChallengeTwo, 1), new Object[] { 
			      "CCC", "A A", "B B",    Character.valueOf('A'), Blocks.planks, Character.valueOf('B'), Blocks.oak_fence, Character.valueOf('C'), Items.glowstone_dust, Character.valueOf('D'), Blocks.golden_rail, Character.valueOf('E'), Blocks.redstone_torch, Character.valueOf('F'), Blocks.stone_slab
			      });
		
		GameRegistry.addRecipe(new ItemStack(BlockChallengeThree, 1), new Object[] { 
			      "DCD", "BBB", "AAA",    Character.valueOf('A'), Blocks.stone, Character.valueOf('B'), Blocks.sand, Character.valueOf('C'), Items.diamond_sword, Character.valueOf('D'), Blocks.torch, Character.valueOf('E'), Blocks.redstone_torch, Character.valueOf('F'), Blocks.stone_slab
			      });
		
		GameRegistry.addRecipe(new ItemStack(BlockChallengeFour, 1), new Object[] { 
			      "DCD", "BBB", "AAA",    Character.valueOf('A'), Blocks.stone, Character.valueOf('B'), Blocks.sand, Character.valueOf('C'), Items.iron_sword, Character.valueOf('D'), Blocks.torch, Character.valueOf('E'), Blocks.redstone_torch, Character.valueOf('F'), Blocks.stone_slab
			      });
		
		GameRegistry.addRecipe(new ItemStack(BlockChallengeFive, 1), new Object[] { 
			      "DCD", "ABA", "ABA",    Character.valueOf('A'), Blocks.planks, Character.valueOf('B'), Blocks.stone, Character.valueOf('C'), Items.bow, Character.valueOf('D'), Items.arrow, Character.valueOf('E'), Blocks.redstone_torch, Character.valueOf('F'), Blocks.stone_slab
			      });

	}
	   
	   @EventHandler
	   public void serverLoad(FMLServerStartingEvent event)
	   {
	     event.registerServerCommand(new ChallengeCommand());
	     event.registerServerCommand(new RetryCommand());
	   }
	  
}
