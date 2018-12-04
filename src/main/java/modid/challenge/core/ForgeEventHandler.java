package modid.challenge.core;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandler {
	@SubscribeEvent
	public void livingSpawnEvent(WorldEvent.Load event)
	{
		Challenge.updateChecked=false;
	}
	
	@SubscribeEvent
	public void deadPlayer(LivingDeathEvent event){
		if(event.getEntityLiving() instanceof EntityPlayer && Challenge.eventHandler.challenge!=null){
			Challenge.eventHandler.challenge.endChallenge((EntityPlayer)event.getEntityLiving());
		}
	}
	
	@SubscribeEvent
	public void commandEvent(CommandEvent event){
		if(Challenge.eventHandler.challenge!=null){
			Challenge.eventHandler.challenge.removeThisChallenge();
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("No commands may be issued while a challenge is running."));
		}
	}
	
	@SubscribeEvent
	public void placeEvent(PlaceEvent event){
		if(Challenge.eventHandler.challenge!=null){
			Challenge.eventHandler.challenge.removeThisChallenge();
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("No blocks may be placed while a challenge is running."));
		}
	}
	
	
}
