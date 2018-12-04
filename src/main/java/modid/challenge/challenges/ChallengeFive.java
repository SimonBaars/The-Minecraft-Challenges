package modid.challenge.challenges;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import modid.challenge.core.Challenge;
import modid.challenge.structureloader.SchematicStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings.GameType;

public class ChallengeFive extends Challenges {
	int sizex=40;
	int sizey=5;
	int sizez=40;
	int towersizex;
	int towersizez;
	int randGrootte = 4;
	ArrayList<Entity> activeMonsters = new ArrayList<Entity>();
	int wave = 0;
	final int nWaves = 20;
	private SchematicStructure checkStructure;
	private boolean doReplaceStuff = true;
	int ticks = 0;
	private int toGoTicks = 10;
	public ChallengeFive(int x, int y, int z) {
		super(x,y,z,GameType.SURVIVAL,EnumDifficulty.PEACEFUL);
		showScore();
		initArena();
		for(EntityPlayerMP player : players){
			player.setPosition(x,y+27,z);
			//player.motionY=1;
		}
		//Minecraft.getMinecraft().thePlayer.setPosition(x,y+27,z);
		resetPlayer();
	}
	
	void initArena(){
		items.add(Items.bow);
		for(int i = 0; i<8; i++){
			items.add(Items.arrow);
		}
		SchematicStructure structure = new SchematicStructure("watchtower");
		structure.readFromFile();
		sizey+=structure.height;
		placeBlocks(Challenge.BlockChickenDirt, x+(sizex/2)-1,y,z+(sizez/2)-1,sizex,1,sizez);
		placeBlocks(Blocks.air, x+(sizex/2)-1,y+1,z+(sizez/2)-1,sizex,sizey+1,sizez);
		towersizex=structure.width;
		towersizez=structure.length;
		structure.process(serverWorld, worldIn, x+structure.width/2, y+1, z+structure.length/2);
		this.checkStructure=structure;
		Challenge.checkMode=true;
	}
	
	void spawnMob(){
		int amount=numberOfPlayers;
		for(int i = 0; i<amount; i++){
			EntityChicken chicken = new EntityChicken(serverWorld);
			switch((int)(Math.random()*4)){
			case 0:chicken.setLocationAndAngles(this.x+((int)(Math.random()*(sizex/randGrootte)))+((randGrootte-1)*(sizex/randGrootte))-1-(sizex/2), y+sizey, this.z+((int)(Math.random()*sizez))-1-(sizez/2), 0,0);break;
			case 1:chicken.setLocationAndAngles(this.x+((int)(Math.random()*sizex))-1-(sizex/2), y+sizey, this.z+((int)(Math.random()*(sizez/randGrootte)))+((randGrootte-1)*(sizez/randGrootte))-1-(sizez/2), 0,0);break;
			case 2:chicken.setLocationAndAngles(this.x+((int)(Math.random()*(sizex/randGrootte)))-1-(sizex/2), y+sizey, this.z+((int)(Math.random()*sizez))-1-(sizez/2), 0,0);break;
			case 3:chicken.setLocationAndAngles(this.x+((int)(Math.random()*sizez))-1-(sizex/2), y+sizey, this.z+((int)(Math.random()*(sizez/randGrootte)))-1-(sizez/2), 0,0);break;
			}
			//chicken.setLocationAndAngles(this.x+((int)(Math.random()*(sizex-2)))+1, y+2, this.z+((int)(Math.random()*(sizez-2)))+1, 0, 0);
			chicken.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(chicken)), (IEntityLivingData)null);
			chicken.setHealth(1);
			//monster.spawnEntityInWorld(monster);
			serverWorld.spawnEntityInWorld(chicken);
			activeMonsters.add(chicken);
		}
	}
	

	void destroy(){
		placeBlocks(Blocks.air, x+(sizex/2)-1,y,z+(sizez/2)-1,sizex,sizey,sizez);
		for(int i = 0; i<activeMonsters.size(); i++){
			activeMonsters.get(i).setDead();
		}
		Challenge.checkMode=false;
	}
	
	boolean closeToGameRoom(int howClose, int x, int y, int z){
		x = x-this.x+(towersizex/2)-howClose;
		y = y-this.y-23-howClose;
		z = z-this.z+(towersizez/2)-howClose;
		//System.out.println(x+", "+y+", "+z+", "+this.x+", "+this.y+", "+this.z);
		return (x>=0 && x<=towersizex+(2*howClose) && y>=0 && y<=5+(2*howClose) && z>=0 && z<=towersizez+(2*howClose));
	}
	
	public boolean run(){
		if(Minecraft.getMinecraft().thePlayer!=null){
		   //checkStructure.process(serverWorld, worldIn, cornerx+fieldx, y-1, cornerz+fieldz);	
			placeBlocks(Challenge.BlockChickenDirt, x+(sizex/2)-1,y,z+(sizez/2)-1,sizex,1,sizez);
			checkStructure.process(serverWorld, worldIn, x+towersizex/2, y+1, z+towersizez/2);
			if(!Challenge.checkMode){
				return true;
			}
		if(resetPlayer()){
			return true;
		}
		//setWaterWorld();
		serverWorld.setWorldTime(5000);
		ticks++;
		if(ticks==toGoTicks){
			spawnMob();
			ticks=0;
		}
		if(activeMonsters.size()==0){
			spawnMob();
			ticks=0;
		}else {
			for(int i = 0; i<activeMonsters.size(); i++){
				if(activeMonsters.get(i).isDead){
					activeMonsters.remove(i);
					increaseScore();
					if(toGoTicks>3){
						toGoTicks--;
					} else {
						waitTime-=100;
						if(waitTime==1500 && toGoTicks>1){
							waitTime=2500;
							toGoTicks--;
						}
					}
					i--;
				} else {
					Random rand = new Random();
					double d0 = rand.nextGaussian() * 0.02D;
	                double d1 = rand.nextGaussian() * 0.02D;
	                double d2 = rand.nextGaussian() * 0.02D;
	                worldIn.spawnParticle(EnumParticleTypes.HEART, activeMonsters.get(i).posX-2.5+(Math.random()*5), activeMonsters.get(i).posY-2.5+(Math.random()*5), activeMonsters.get(i).posZ-2.5+(Math.random()*5), d0, d1, d2, new int[0]);
	            
				}
			}
		}
		
		showScore();
		
		
		register();
		}
		return false;
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
