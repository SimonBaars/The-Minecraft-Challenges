package modid.challenge.structureloader;

import java.io.DataInputStream;
import java.util.zip.GZIPInputStream;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SchematicStructure extends Structure
{
	
	public boolean isLive;
	public SchematicStructure(String fileName)
	{
		super(fileName);
		this.isLive=false;
	}

	// Blocks stored [y][z][x]
	private Block[][][] blocks;
	private int[][][] blockData;

	private NBTTagCompound[] entities;
	private NBTTagCompound[] tileEntities;
	private int blocksAdded;
	World serverWorld; World world; int posX,  posY,  posZ;
	BlockPlacer blockPlacer2;
	BlockPlacer blockPlacer;
	Vec3d harvestPos;


	@Override
	public void process(World serverWorld, World world, int posX, int posY, int posZ)
	{
		this.serverWorld=serverWorld;this.world=world;this.posX=posX;this.posY=posY;this.posZ=posZ;
		//Minecraft.getMinecraft().thePlayer.sendChatMessage("Please be patient, I'm just creating "+(height*width*length)+" blocks for the structure...");
		Block blk = Blocks.air;
		   // Make a position.
		   BlockPos pos0 = new BlockPos(posX,posY,posZ);
		   // Get the default state(basically metadata 0)
		   IBlockState state0=blk.getDefaultState();
		   // set the block
		   serverWorld.setBlockState(pos0, state0);
		   world.setBlockState(pos0, state0);
		blocksAdded=0;
		posX-=length/2-1;
		posZ-=width/2-1;
		Vec3d harvestPos = new Vec3d(posX + 0.5, posY, posZ + 0.5);
		BlockPlacer blockPlacer2 = new BlockPlacer(serverWorld,isLive);
		//BlockPlacer blockPlacer = new BlockPlacer(world,isLive);
		//this.blockPlacer=blockPlacer;this.blockPlacer2=blockPlacer2;
		this.harvestPos=harvestPos;
		
		//System.out.println("Blocks");
		for (int y = 0; y < this.height; y++)
		{
			for (int z = 0; z < this.width; z++)
			{
				for (int x = 0; x < this.length; x++)
				{
					//System.out.println("DATA=="+this.blocks[y][z][x]+blockPlacer+this.getCenterPos()+harvestPos);
					if (this.blockMode.equals("overlay") && this.blocks[y][z][x] == Blocks.air) continue;
					StructureUtils.setBlock(blockPlacer2, this.blocks[y][z][x].getStateFromMeta(this.blockData[y][z][x]), new BlockPos(x, y, z), this.getCenterPos(), harvestPos);
					//if(StructureUtils.setBlock(blockPlacer, this.blocks[y][z][x].getStateFromMeta(this.blockData[y][z][x]), new BlockPos(x, y, z), this.getCenterPos(), harvestPos)){
					//	blocksAdded++;
					//}
				}
			}
		}
	}
	
	public void initSingleBlockPlacer(World serverWorld, World world, int posX, int posY, int posZ){
		this.serverWorld=serverWorld;this.world=world;this.posX=posX;this.posY=posY;this.posZ=posZ;
		//Minecraft.getMinecraft().thePlayer.sendChatMessage("Please be patient, I'm just creating "+(height*width*length)+" blocks for the structure...");
		Block blk = Blocks.air;
		
		   // Make a position.
		   BlockPos pos0 = new BlockPos(this.posX,this.posY,this.posZ);
		   // Get the default state(basically metadata 0)
		   IBlockState state0=blk.getDefaultState();
		   // set the block
		   this.serverWorld.setBlockState(pos0, state0);
		   this.world.setBlockState(pos0, state0);
		blocksAdded=0;
		
		this.posX-=length/2-1;
		this.posZ-=width/2-1;
		
		
	this.harvestPos = new Vec3d(this.posX + 0.5, this.posY, this.posZ + 0.5);
		this.blockPlacer2 = new BlockPlacer(serverWorld,isLive);
		this.blockPlacer = new BlockPlacer(world,isLive);
	}
		
	public void postProcess(){
		//System.out.println("Structure Processed!");
		try{
		for (NBTTagCompound tileEntity : this.tileEntities)
			StructureUtils.setTileEntity(serverWorld, TileEntity.createTileEntity(Minecraft.getMinecraft().getIntegratedServer(),tileEntity), this.getCenterPos(), harvestPos);
		
		for (NBTTagCompound entity : this.entities)
			StructureUtils.setEntity(serverWorld, EntityList.createEntityFromNBT(entity, serverWorld), this.getCenterPos(), harvestPos);
		
		for (NBTTagCompound tileEntity : this.tileEntities)
			StructureUtils.setTileEntity(world, TileEntity.createTileEntity(Minecraft.getMinecraft().getIntegratedServer(), tileEntity), this.getCenterPos(), harvestPos);
		
		for (NBTTagCompound entity : this.entities)
			StructureUtils.setEntity(world, EntityList.createEntityFromNBT(entity, world), this.getCenterPos(), harvestPos);
		
		if (this.blockUpdate){
			//blockPlacer2.update();
			blockPlacer.update();
		}
		} catch(Exception e){
			
		}
		
		//Minecraft.getMinecraft().thePlayer.sendChatMessage("I just created "+blocksAdded+" out of "+(height*width*length)+" blocks in this structure!");
	}
	
	public void showOutline(int x, int modifierx, int y,int modifiery, int z, int modifierz, World worldIn){
		for(int i=0; i<width; i++){ for(int j = 0; j<height; j++){ for(int k = 0; k<length; k++){ 
			if(i==0||j==0||k==0){
				if(i==modifierx&&j==modifiery&&k==modifierz) continue;
			Block blk = Blocks.glass;
			   // Make a position.
			   BlockPos pos0 = new BlockPos(x-i+modifierx,y+j+modifiery,z-k+modifierz);
			   // Get the default state(basically metadata 0)
			   IBlockState state0=blk.getDefaultState();
			   // set the block
			   worldIn.setBlockState(pos0, state0);

			//worldIn.spawnEntityInWorld(new EntitySnowball(worldIn, x+i,y+j,z+k)); 
			}}} }
	}
	
	public void removeOutline(int x, int modifierx, int y,int modifiery, int z, int modifierz, World worldIn){
		for(int i=0; i<width; i++){ for(int j = 0; j<height; j++){ for(int k = 0; k<length; k++){ 
			if(i==0||j==0||k==0){
			if(i==modifierx&&j==modifiery&&k==modifierz) continue;
			Block blk = Blocks.air;
			   // Make a position.
			   BlockPos pos0 = new BlockPos(x-i+modifierx,y+j+modifiery,z-k+modifierz);
			   // Get the default state(basically metadata 0)
			   IBlockState state0=blk.getDefaultState();
			   // set the block
			   worldIn.setBlockState(pos0, state0);

			//worldIn.spawnEntityInWorld(new EntitySnowball(worldIn, x+i,y+j,z+k)); 
			}}} }
	}


	@Override
	public void readFromFile()
	{
		NBTTagCompound nbtTagCompound = null;
		DataInputStream dataInputStream;
		try
		{
			dataInputStream = new DataInputStream(new GZIPInputStream(this.fileStream));
			nbtTagCompound = CompressedStreamTools .read(dataInputStream);
			dataInputStream.close();
		}
		catch (Exception e)
		{
			System.err.println("Instant Massive Structures Mod: Error loading structure '" + this.fileName + "'");
			return;
		}

		// In schematics, length is z and width is x. Here it is reversed.
		this.length = nbtTagCompound.getShort("Width");
		this.width = nbtTagCompound.getShort("Length");
		this.height = nbtTagCompound.getShort("Height");

		int size = this.length * this.width * this.height;
		/*if (size > STRUCTURE_BLOCK_LIMIT)
		{
			System.err.println("Instant Massive Structures Mod: Error loading structure. The structure '" + this.fileName + "' (" + size + " blocks) exceeds the " + STRUCTURE_BLOCK_LIMIT + " block limit");
			return;
		}*/

		this.blocks = new Block[this.height][this.width][this.length];
		this.blockData = new int[this.height][this.width][this.length];

		byte[] blockIdsByte = nbtTagCompound.getByteArray("Blocks");
		byte[] blockDataByte = nbtTagCompound.getByteArray("Data");
		int x = 1, y = 1, z = 1;
		for (int i = 0; i < blockIdsByte.length; i++)
		{
			int blockId = (short) (blockIdsByte[i] & 0xFF);
			this.blocks[y - 1][z - 1][x - 1] = Block.getBlockById(blockId);
			this.blockData[y - 1][z - 1][x - 1] = blockDataByte[i];
			x++;
			if (x > this.length)
			{
				x = 1;
				z++;
			}
			if (z > this.width)
			{
				z = 1;
				y++;
			}
		}

		NBTTagList entityList = nbtTagCompound.getTagList("Entities", 10);
		this.entities = new NBTTagCompound[entityList.tagCount()];
		for (int i = 0; i < entityList.tagCount(); i++)
			this.entities[i] = entityList.getCompoundTagAt(i);

		NBTTagList tileEntityList = nbtTagCompound.getTagList("TileEntities", 10);
		this.tileEntities = new NBTTagCompound[tileEntityList.tagCount()];
		for (int i = 0; i < tileEntityList.tagCount(); i++)
			this.tileEntities[i] = tileEntityList.getCompoundTagAt(i);

		this.initCenterPos();
	}
}
