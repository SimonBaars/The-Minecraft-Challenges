package modid.challenge.challenges;

import modid.challenge.core.Challenge;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockBlueRock extends Block {

	public BlockBlueRock() {
		super(Material.rock);
		setBlockUnbreakable();
		setResistance(6000000.0F);
		disableStats();
		// TODO Auto-generated constructor stub
	}
    
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
    {
    	if(Challenge.eventHandler.challenge!=null){
    		Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Don't break challenge related blocks while a challenge is running"));
    		Challenge.eventHandler.challenge.removeThisChallenge();
    	}
    }
	
	/*public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if(!(neighborBlock instanceof BlockBlackRock || neighborBlock instanceof BlockTouchable) && Challenge.eventHandler.challenge.withinGameRoom(pos.getX(), pos.getY(), pos.getZ())){
        	Minecraft.getMinecraft().thePlayer.sendChatMessage("Don't place any blocks while a challenge is running");
        	Challenge.eventHandler.challenge.removeThisChallenge();
        }
    }*/
}
