package modid.challenge.structureloader;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPlacer
{
	private World world;
	private ArrayList<BlockPos> updatePos;
	private ArrayList<IBlockState> updateState;
	boolean isLive;

	public BlockPlacer(World world, boolean isLive)
	{
		this.world = world;
		this.isLive=isLive;
		this.updatePos = new ArrayList<BlockPos>();
		this.updateState = new ArrayList<IBlockState>();
	}

	public boolean add(IBlockState blockState, BlockPos blockPos)
	{
		/*Field field;
		try {
			field = World.class.getDeclaredField("processingLoadedTiles");
			field.setAccessible(true);
	        Object value = field.get(world);
	        while((Boolean)value){
	        	value = field.get(world);
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*world.processingLoadedTiles.setAccessible(true);
		while(world.processingLoadedTiles){
			
		}*/
		boolean blockAdded = DropFuncBlock.setBlock(this.world, blockState, blockPos, false, isLive);
		this.updatePos.add(blockPos);
		this.updateState.add(blockState);
		return blockAdded;
	}

	public void update()
	{
		for (int i = 0; i < this.updatePos.size(); i++)
		{
			this.world.markAndNotifyBlock(this.updatePos.get(i), this.world.getChunkFromBlockCoords(this.updatePos.get(i)), this.world.getBlockState(this.updatePos.get(i)), this.updateState.get(i), 3);
		}
	}
}
