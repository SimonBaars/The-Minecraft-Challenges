package modid.challenge.challenges;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import modid.challenge.core.Challenge;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;

public class ChallengeSeven extends Challenges {
	int sizex=30;
	int sizey=5;
	int sizez=30;
	final int fieldx = 10;
	final int fieldy = 5;
	final int fieldz = 10;
	int cornerx;
	int cornerz;
	ArrayList<EntityLiving> activeMonsters = new ArrayList<EntityLiving>();
	int wave = 0;
	final int nWaves = 20;
	private boolean doReplaceStuff = false;
	//int ticks = 0;
	//int toGoTicks=20;
	
	
	public ChallengeSeven(int x, int y, int z) {
		super(x,y,z,GameType.ADVENTURE,EnumDifficulty.HARD);
		showScore();
		createArena();
		cornerx=x+(fieldx/2)-1;
		cornerz=z+(fieldy/2)-1;
		for(EntityPlayerMP player : players){
			player.setPosition(x, y+2	,z);
		}
		resetPlayer();
	}
	
	void createArena(){
		placeBlocks(Challenge.BlockTouchable, x+(sizex/2)-1,y,z+(sizez/2)-1,sizex,1,sizez);
		placeBlocks(Blocks.air, x+(sizex/2)-1,y+1,z+(sizex/2)-1,sizex,sizey-1,sizez);
		placeBlocks(Challenge.BlockBlackRock, x+(fieldx/2)-1,y+1,z+(fieldz/2)-1,fieldx,1,fieldz);
	}
	
	void spawnMobs(int monsterId, int amount){
		amount=(amount*((wave/nWaves)+1))*numberOfPlayers;
		for(int i = 0; i<amount; i++){
			EntityLiving monster = getMonster(monsterId, serverWorld);
			//System.out.println((((int)(Math.random()*(fieldx-2)))-1)+", "+(((int)(Math.random()*(fieldz-2)))-1));
			monster.setLocationAndAngles(cornerx-((int)(Math.random()*(fieldx-2)))-1, y+3, cornerz+3-((int)(Math.random()*(fieldz-2)))-1, 0, 0);
			
			monster.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(monster)), (IEntityLivingData)null);
			//monster.spawnEntityInWorld(monster);
			serverWorld.spawnEntityInWorld(monster);
			activeMonsters.add(monster);
		}
	}
	
	private EntityLiving getMonster(int monsterId, World world) {
		switch(monsterId){
		case 0: return new EntityZombie(world);
		case 1: return new EntitySpider(world);
		case 2: return new EntityBlaze(world);
		case 3: return new EntityCaveSpider(world);
		case 4: return new EntityCreeper(world);
		case 5: return new EntityEnderman(world);
		case 6: return new EntityEndermite(world);
		case 7: return new EntityGiantZombie(world);
		case 8: return new EntityGuardian(world);
		case 9: return new EntityMagmaCube(world);
		case 10: return new EntityPigZombie(world);
		case 11: return new EntitySilverfish(world);
		case 12: return new EntitySkeleton(world); /*skelly.setCurrentItemOrArmor(0, new ItemStack(Items.bow)); return skelly*/
		case 13: return new EntitySlime(world);
		case 14: return new EntityWitch(world);
		case 15: return new EntityWolf(world);
		case 16: EntityRabbit rabbit = new EntityRabbit(world); rabbit.setRabbitType(99); return rabbit;
		}
		return null;
	}

	void destroy(){
		placeBlocks(Blocks.air, x+(sizex/2)-1, y,z+(sizez/2)-1,sizex,sizey,sizez);
		for(int i = 0; i<activeMonsters.size(); i++){
			activeMonsters.get(i).setDead();
		}
	}
	
	boolean closeToGameRoom(int howClose, int x, int y, int z){
		x = x-cornerx+fieldx-howClose;
		y = y-this.y-1-howClose;
		z = z-cornerz+fieldz-howClose-2;
		//System.out.println(cornerx+", "+cornerz+", "+fieldx+", "+fieldz);
		//System.out.println(x+", "+y+", "+z+", "+(fieldx+(2*howClose)+2)+", "+(fieldy+(2*howClose))+", "+(fieldz+(2*howClose)+2));
		return (x>=0 && x<=fieldx+(2*howClose)+2 && y>=0 && y<=fieldy+(2*howClose) && z>=0 && z<=fieldz+(2*howClose)+2);
	}
	
	public boolean run(){
		waitTime=1000;
		//ticks++;
		for(EntityPlayerMP player : players){
			player.setHealth(player.getMaxHealth());			
		}
		/*for(int i = 0; i<100; i++){
		placeBlocks(Blocks.emerald_block, cornerx-((int)(Math.random()*(fieldx-2)))-1, y+1, cornerz+3-((int)(Math.random()*(fieldz-2)))-1,1,1,1);
		}*/
		if(/*ticks==toGoTicks || !doReplaceStuff || */activeMonsters.size()==0){
			int realWave = wave%nWaves;
			switch(realWave){
			case 0:  spawnMobs(16,2); break;
			case 1: spawnMobs(12,2); break;
			case 2: spawnMobs(13,4); break;
			case 3: spawnMobs(11,3); break;
			case 4: spawnMobs(15,5); break;
			case 5: spawnMobs(0,6); break;
			case 6: spawnMobs(4,5); break;
			case 7: spawnMobs(8, 2); break;
			case 8: spawnMobs(9, 10); break;
			case 9: spawnMobs(2, 5); break;
			case 10: spawnMobs(10,6); break;
			case 11: spawnMobs(1,20); break;
			case 12: spawnMobs(16,6); break;
			case 13: spawnMobs(6,5); break;
			case 14: spawnMobs(9, 7); spawnMobs(13, 7); break;
			case 15: spawnMobs(2,10); break;
			case 16:  spawnMobs(0,7);spawnMobs(4,2); break;
			case 17: spawnMobs(8,5); break;
			case 18: spawnMobs(12,6); break;
			case 19: int[] finalRound = {0,1,2,4,6,9,10,11,12,13,15,16}; for(int i = 0; i<finalRound.length; i++){ spawnMobs(finalRound[i],1); }
			} 
			wave++;
			doReplaceStuff=true;
			//ticks=0;
		}
		serverWorld.setWorldTime(14000);
		if(Minecraft.getMinecraft().thePlayer!=null){
			if(doReplaceStuff){
				createArena();
			}
		if(resetPlayer()){
			return true;
		}
		for(EntityPlayerMP player : players){
		player.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.diamond_helmet, 1));
		player.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.diamond_chestplate, 1));
		player.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.diamond_leggings, 1));
		player.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.diamond_boots, 1));
		}
			for(int i = 0; i<activeMonsters.size(); i++){
				if(activeMonsters.get(i).isDead){
					activeMonsters.remove(i);
					increaseScore();
					i--;
				} else if(!closeToGameRoom(1, activeMonsters.get(i).getPosition().getX(),activeMonsters.get(i).getPosition().getY(),activeMonsters.get(i).getPosition().getZ())){
					 activeMonsters.get(i).setDead();
					activeMonsters.remove(i);
					increaseScore();
					i--;
				}else {
					activeMonsters.get(i).setHealth(activeMonsters.get(i).getMaxHealth());
				}
			}
		showScore();
		waitTime--;
		
		
		register();
		}
		return false;
	}
	
	private void removeWaterWorld() {
		// TODO Auto-generated method stub
		doReplaceStuff=true;
		placeBlocks(Blocks.air, cornerx+fieldx-1, y+1,cornerz+fieldz,fieldx,2,fieldz);
	}

	private void setWaterWorld() {
		// TODO Auto-generated method stub
		doReplaceStuff=false;
		placeBlocks(Blocks.water, cornerx+fieldx-1, y+1,cornerz+fieldz,fieldx,2,fieldz);
		for(int i  = 0; i<fieldx; i+=3){
			for(int j = 0; j<2; j++){
				for(int k  = 0; k<fieldz; k+=3){
					placeBlocks(Blocks.sand, cornerx+fieldx-1-i, y+1+j,cornerz+fieldz-k,1,2,1);
				}
			}
		}
	}

	void register(){
		//System.out.println("Registered "+at);
		PrintWriter writer;
		try {
			(new File("saves/"+Minecraft.getMinecraft().getIntegratedServer().getFolderName())).mkdirs();
			writer = new PrintWriter("saves/"+Minecraft.getMinecraft().getIntegratedServer().getFolderName()+"/challenge.txt", "UTF-8");
			writer.println(x+(sizex/2)-1);
			writer.println(y);
			writer.println(z+(sizez/2)-1);
			writer.println(sizex);
			writer.println(sizey);
			writer.println(sizez);
		writer.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
