package modid.challenge.challenges;

import modid.challenge.core.Challenge;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTouchable extends Block {

	protected static final AxisAlignedBB CACTUS_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);
    protected static final AxisAlignedBB CACTUS_COLLISION_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

    
	public BlockTouchable() {
		super(Material.rock);
		setBlockUnbreakable();
		setResistance(6000000.0F);
		disableStats();
		// TODO Auto-generated constructor stub
	}
	
	public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return CACTUS_AABB;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getCollisionBoundingBox(IBlockState worldIn, World pos, BlockPos state)
    {
        return CACTUS_COLLISION_AABB.offset(state);
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
    	//System.out.println("Poke! "+(Challenge.eventHandler.challenge!=null)+", "+(Challenge.eventHandler.challenge.getScore()>1 || Challenge.eventHandler.challenge instanceof ChallengeSeven)+", "+(entityIn instanceof EntityPlayer));
    	if(Challenge.eventHandler.challenge!=null && (Challenge.eventHandler.challenge.getScore()>1 || Challenge.eventHandler.challenge instanceof ChallengeSeven) && !worldIn.isRemote){
        if(entityIn instanceof EntityPlayer){
        	Challenge.eventHandler.challenge.endChallenge((EntityPlayer)entityIn);
        } else {
        	entityIn.setDead();
        	/*if(Challenge.eventHandler.challenge instanceof ChallengeSeven && ((ChallengeSeven)(Challenge.eventHandler.challenge)).activeMonsters.contains){
        		
        	}*/
        }
    	}
        
    }
    
    /*public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if(!(neighborBlock instanceof BlockBlackRock || neighborBlock instanceof BlockTouchable) && Challenge.eventHandler.challenge.withinGameRoom(pos.getX(), pos.getY(), pos.getZ()-2)&& Challenge.eventHandler.challenge!=null){
        	Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Don't place any blocks while a challenge is running"));
        	Challenge.eventHandler.challenge.removeThisChallenge();
        }
    }*/
    
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
    {
    	if(Challenge.eventHandler.challenge!=null){
    		Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Don't break challenge related blocks while a challenge is running"));
    		Challenge.eventHandler.challenge.removeThisChallenge();
    	}
    }
	
}
