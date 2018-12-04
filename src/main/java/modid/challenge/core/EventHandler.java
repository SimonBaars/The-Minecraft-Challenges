package modid.challenge.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import modid.challenge.challenges.ChallengeNine;
import modid.challenge.challenges.Challenges;
import modid.challenge.structureloader.LightUpdateThread;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {
	/*public ArrayList<SchematicStructure> postProcessors = new ArrayList<SchematicStructure>();
	public ArrayList<ICreatorBlock> creators = new ArrayList<ICreatorBlock>();
	public ArrayList<LiveStructure> liveCreators = new ArrayList<LiveStructure>();
	public ArrayList<Integer> scheduledExplosions = new ArrayList<Integer>();*/
	public LightUpdateThread lightUpdate;
	//long lastUpdateTime=0;
	public Challenges challenge;
	//public boolean challengeRunning = false;
	//public long previousTick = 0;
	//private Map<BlockPos, IBlockState> clientBlocks = new HashMap();
	//private Map<BlockPos, IBlockState> serverBlocks = new HashMap();
	//public static boolean lockProcesses = false;
	
	public EventHandler(){
	}
	/*@SubscribeEvent
	public void update(TickEvent.WorldTickEvent event){
		//System.out.println("world");
		if(postProcessors.size()>0){
			postProcessors.get(0).postProcess();
			//System.out.println("Post processing done");
			postProcessors.remove(0);
		}
		if(event.phase==Phase.END){
		/*Challenge.canWrite=false;
		} else {
			Challenge.canWrite=true;
			System.out.println("Thread may place a block");
		}
		//System.out.println(Challenge.canWrite);
	}*/
	
	 @SubscribeEvent
	    public void onKeyInput(InputEvent.KeyInputEvent event) {
	        if(KeyBindings.flap.isPressed() && challenge!=null && challenge instanceof ChallengeNine){
	        		Minecraft.getMinecraft().thePlayer.moveFlying(0.0F, 1.1F, 1.1F);
	        		Minecraft.getMinecraft().thePlayer.motionY=1;
	        		EntityPlayerMP player = (EntityPlayerMP) Minecraft.getMinecraft().getIntegratedServer().getEntityWorld().getPlayerEntityByName(Minecraft.getMinecraft().thePlayer.getName());
	        		player.moveFlying(0.0F, 1.1F, 1.1F);
	        		player.motionY=1;
	        }
	    }

	
		
	public void load(){
		if(!Challenge.updateChecked){
			Challenge.updateChecked=true;
			UpdateThread updateThread = new UpdateThread();
			updateThread.start();
		}
			lightUpdate= new LightUpdateThread(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().getIntegratedServer().worldServerForDimension(0));
			lightUpdate.start();
			if(challenge!=null && Minecraft.getMinecraft().thePlayer!=null){
				challenge.removeThisChallenge();
				challenge=null;
				System.out.println("Removed unclosed challenge");
			}
			else if(fileExists()){
				String[] array = new String[6];
				BufferedReader in;
				try {
					in = new BufferedReader(new FileReader("saves/"+Minecraft.getMinecraft().getIntegratedServer().getFolderName()+"/challenge.txt"));
				

				for(int j = 0; j<array.length; j++){
					//System.out.println("Loop 4");
					try {
						array[j]=in.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				in.close();
				BlockPlaceHandler.placeBlocks(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().getIntegratedServer().worldServerForDimension(0), Blocks.air, Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]), Integer.parseInt(array[3]), Integer.parseInt(array[4]), Integer.parseInt(array[5]));
				new File("saves/"+Minecraft.getMinecraft().getIntegratedServer().getFolderName()+"/challenge.txt").delete();
				challenge=null;
				Minecraft.getMinecraft().thePlayer.inventory.clear();
				} catch (Exception e){
				
			}
			}
	}
	
	@SubscribeEvent
	public void tickEvent(TickEvent.ClientTickEvent world){
		if(Minecraft.getMinecraft().isGamePaused()){
			if(Challenge.eventHandler.challenge!=null){
				Challenge.eventHandler.challenge.removeThisChallenge();
				Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("No pausing while a challenge is running."));
			}
		}
		/*if(!lockProcesses && clientBlocks.size()>0){
			Set<Entry<BlockPos,IBlockState>> entrySet = clientBlocks.entrySet();
			for(Entry<BlockPos,IBlockState> entry : entrySet){
				BlockPlaceHandler.setBlock(Minecraft.getMinecraft().theWorld, entry.getKey(), entry.getValue());
			}
			clientBlocks.clear();
		}*/
	}
	
	
	@SubscribeEvent
	public void update(TickEvent.ServerTickEvent event){
		if(!Challenge.updateChecked && Minecraft.getMinecraft().theWorld!=null){
			load();
			Challenge.updateChecked=true;
		}
		
		if(challenge!=null && Minecraft.getMinecraft().thePlayer!=null){
			/*if(System.currentTimeMillis()>challenge.lastTickTime+challenge.waitTime+1000){
				Minecraft.getMinecraft().thePlayer.sendChatMessage("You paused the game or your computer processed this mod's actions too slowly");
				challenge.removeThisChallenge();
				return;
			}*/
			if(System.currentTimeMillis()>challenge.lastTickTime+challenge.waitTime){
				if(!challenge.run() && challenge !=null){
					challenge.lastTickTime=System.currentTimeMillis();
				}
			}
			//previousTick=System.currentTimeMillis();
		}
		/*if(!lockProcesses && serverBlocks.size()>0){
			Set<Entry<BlockPos,IBlockState>> entrySet = serverBlocks.entrySet();
			for(Entry<BlockPos,IBlockState> entry : entrySet){
				BlockPlaceHandler.setBlock(Minecraft.getMinecraft().getIntegratedServer().getEntityWorld(), entry.getKey(), entry.getValue());
			}
			serverBlocks.clear();
		}*/
	}
	
	boolean fileExists(){
		File f = new File("saves/"+Minecraft.getMinecraft().getIntegratedServer().getFolderName()+"/challenge.txt");
		if(f.exists() && !f.isDirectory()) { 
		    return true;
		}
		return false;
	}

	/*public void createDelayedClientBlock(BlockPos blockPos, IBlockState defaultState) {
		clientBlocks.put(blockPos, defaultState);
	}
	
	public void createDelayedServerBlock(BlockPos blockPos, IBlockState defaultState) {
		serverBlocks.put(blockPos, defaultState);
	}*/
}
