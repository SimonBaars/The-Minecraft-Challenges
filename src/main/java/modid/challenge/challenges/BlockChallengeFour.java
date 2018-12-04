package modid.challenge.challenges;

import modid.challenge.core.Challenge;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockChallengeFour extends Block
{
  public BlockChallengeFour(int i)
    {
        super(Material.rock);
    }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
  {
	  if(!worldIn.isRemote && Challenge.eventHandler.challenge==null){
	  new ChallengeFour(pos.getX(), pos.getY(), pos.getZ());
	  }
	  return true;
  }

}
