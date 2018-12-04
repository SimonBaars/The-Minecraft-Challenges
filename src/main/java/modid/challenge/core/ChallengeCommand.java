package modid.challenge.core;

import java.util.ArrayList;
import java.util.List;

import modid.challenge.challenges.ChallengeEight;
import modid.challenge.challenges.ChallengeFive;
import modid.challenge.challenges.ChallengeFour;
import modid.challenge.challenges.ChallengeNine;
import modid.challenge.challenges.ChallengeOne;
import modid.challenge.challenges.ChallengeSeven;
import modid.challenge.challenges.ChallengeSix;
import modid.challenge.challenges.ChallengeThree;
import modid.challenge.challenges.ChallengeTwo;
import modid.challenge.challenges.Challenges;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class ChallengeCommand implements ICommand
{
  private List aliases;
  public ChallengeCommand()
  {
    this.aliases = new ArrayList();
    this.aliases.add("startchallenge");
    this.aliases.add("createchallenge");
  }

  @Override
  public String getCommandName()
  {
    return "challenge";
  }

  @Override
  public String getCommandUsage(ICommandSender icommandsender)
  {
    return "challenge <challenge number> <x> <y> <z>";
  }
  
  public String getCommandUsage()
  {
    return "challenge <challenge number> <x> <y> <z>";
  }

  @Override
  public List getCommandAliases()
  {
    return this.aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) 
  {
    if(args.length <4 || Challenge.eventHandler.challenge!=null)
    {
    	Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Invalid arguments"));
    	Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(getCommandUsage()));
      return;
    }
    int x,y,z,challengenum;
    try{
    	challengenum=Integer.parseInt(args[0]);
    	x=Integer.parseInt(args[1]);
    	y=Integer.parseInt(args[2]);
    	z=Integer.parseInt(args[3]);
    	Challenge.eventHandler.challenge = getChallenge(challengenum, x,y,z);   
    } catch (Exception e){
    	Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Invalid arguments"));
    	Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(getCommandUsage()));
        return;
    }
    
  }

  private Challenges getChallenge(int challengenum, int x, int y, int z) {
	switch(challengenum){
	case 1: return new ChallengeOne(x,y,z);
	case 2: return new ChallengeTwo(x,y,z);
	case 3: return new ChallengeThree(x,y,z);
	case 4: return new ChallengeFour(x,y,z);
	case 5: return new ChallengeFive(x,y,z);
	case 6: return new ChallengeSix(x,y,z);
	case 7: return new ChallengeSeven(x,y,z);
	case 8: return new ChallengeEight(x,y,z);
	case 9: return new ChallengeNine(x,y,z);
		}
	return null;
}

@Override
  public List getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
  {
    return null;
  }

  @Override
  public boolean isUsernameIndex(String[] astring, int i)
  {
    return false;
  }

  @Override
  public int compareTo(ICommand o)
  {
    return 0;
  }

@Override
public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
	// TODO Auto-generated method stub
	return true;
}
}
