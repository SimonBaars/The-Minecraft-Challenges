package modid.challenge.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class RetryCommand implements ICommand
{
  private List aliases;
  public static ScoreThread retryThread;
  public RetryCommand()
  {
    this.aliases = new ArrayList();
  }

  @Override
  public String getCommandName()
  {
    return "retry";
  }

  @Override
  public String getCommandUsage(ICommandSender icommandsender)
  {
    return "retry";
  }
  
  public String getCommandUsage()
  {
    return "retry";
  }

  @Override
  public List getCommandAliases()
  {
    return this.aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) 
  {
	  if(retryThread!=null){
		  ScoreThread thread = new ScoreThread(retryThread.score, retryThread.challengenum, retryThread.player);
		  retryThread = null;
		  thread.start();
	  } else {
		  Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("There was no score that failed posting"));
	  }
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
