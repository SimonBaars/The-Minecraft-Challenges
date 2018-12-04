package modid.challenge.core;

import modid.challenge.structureloader.DropFuncBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class BlockPlaceHandler {
	public static void placeBlocks(World worldIn, World serverWorld, Block block, int posx, int posy, int posz, int sizex, int sizey, int sizez){
		//EventHandler.lockProcesses=true;
		for(int x = 0; x<sizex; x++){
			for(int y =0; y<sizey; y++){
				for(int z =0; z<sizez; z++){
					BlockPos pos = new BlockPos(posx-x,posy+y,posz-z);
					//Challenge.eventHandler.createDelayedClientBlock(pos, block.getDefaultState());
					//Challenge.eventHandler.createDelayedServerBlock(pos, block.getDefaultState());
					DropFuncBlock.setBlock(serverWorld, block.getDefaultState(), new BlockPos(posx-x,posy+y,posz-z), true, false);
					//setBlock(worldIn, new BlockPos(posx-x,posy+y,posz-z), block.getDefaultState());
					//setBlock(serverWorld, new BlockPos(posx-x,posy+y,posz-z), block.getDefaultState());
				}
			}
		}
		
		//EventHandler.lockProcesses=false;
	}
	
	/*public static void setBlock(World world, BlockPos pos, IBlockState state){
		//if(addToMap(state, pos.getX(),pos.getY(),pos.getZ())){
		try{
			Chunk chunk = world.getChunkFromBlockCoords(pos);
			ExtendedBlockStorage storageArray = chunk.getBlockStorageArray()[pos.getY() >> 4];
			if (storageArray == null) storageArray = chunk.getBlockStorageArray()[pos.getY() >> 4] = new ExtendedBlockStorage(pos.getY() >> 4 << 4, !world.provider.getHasNoSky());

			if (storageArray.get(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15) != state.getBlock())
			{
				//IBlockState oldState = world.getBlockState(pos);
				storageArray.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, state);
				world.markBlockRangeForRenderUpdate(pos,pos);
				if(world.isRemote){
				Challenge.eventHandler.lightUpdate.processes.add(pos);
				}
			}
		} catch (Exception e){
			//e.printStackTrace();
		}
	}*/
}
