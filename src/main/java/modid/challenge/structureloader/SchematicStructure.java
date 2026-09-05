package modid.challenge.structureloader;

import java.io.DataInputStream;
import java.util.zip.GZIPInputStream;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Loads legacy .structure schematics (numeric block ids from 1.8/1.9 era).
 * Metadata is ignored; ids are mapped through a best-effort legacy table.
 */
public class SchematicStructure extends Structure {
	public boolean isLive;
	private Block[][][] blocks;
	private int[][][] blockData;
	private CompoundTag[] entities;
	private CompoundTag[] tileEntities;

	public SchematicStructure(String fileName) {
		super(fileName);
		this.isLive = false;
	}

	@Override
	public void process(Level serverWorld, Level world, int posX, int posY, int posZ) {
		BlockPos pos0 = new BlockPos(posX, posY, posZ);
		serverWorld.setBlock(pos0, Blocks.AIR.defaultBlockState(), 3);
		posX -= length / 2 - 1;
		posZ -= width / 2 - 1;
		Vec3 harvestPos = new Vec3(posX + 0.5, posY, posZ + 0.5);
		BlockPlacer blockPlacer2 = new BlockPlacer(serverWorld, isLive);
		for (int y = 0; y < this.height; y++) {
			for (int z = 0; z < this.width; z++) {
				for (int x = 0; x < this.length; x++) {
					Block block = this.blocks[y][z][x];
					if (this.blockMode.equals("overlay") && block == Blocks.AIR) continue;
					BlockState state = block.defaultBlockState();
					StructureUtils.setBlock(blockPlacer2, state, new BlockPos(x, y, z), this.getCenterPos(), harvestPos);
				}
			}
		}
	}

	@Override
	public void readFromFile() {
		CompoundTag nbtTagCompound;
		try {
			DataInputStream dataInputStream = new DataInputStream(new GZIPInputStream(this.fileStream));
			nbtTagCompound = NbtIo.read(dataInputStream, NbtAccounter.unlimitedHeap());
			dataInputStream.close();
		} catch (Exception e) {
			System.err.println("Challenge Mod: Error loading structure '" + this.fileName + "'");
			this.length = this.width = this.height = 1;
			this.blocks = new Block[1][1][1];
			this.blocks[0][0][0] = Blocks.STONE;
			this.blockData = new int[1][1][1];
			this.entities = new CompoundTag[0];
			this.tileEntities = new CompoundTag[0];
			this.initCenterPos();
			return;
		}

		this.length = nbtTagCompound.getShortOr("Width", (short) 1);
		this.width = nbtTagCompound.getShortOr("Length", (short) 1);
		this.height = nbtTagCompound.getShortOr("Height", (short) 1);

		this.blocks = new Block[this.height][this.width][this.length];
		this.blockData = new int[this.height][this.width][this.length];

		byte[] blockIdsByte = nbtTagCompound.getByteArray("Blocks").orElse(new byte[0]);
		byte[] blockDataByte = nbtTagCompound.getByteArray("Data").orElse(new byte[blockIdsByte.length]);
		int x = 1, y = 1, z = 1;
		for (int i = 0; i < blockIdsByte.length; i++) {
			int blockId = blockIdsByte[i] & 0xFF;
			this.blocks[y - 1][z - 1][x - 1] = LegacyBlocks.fromId(blockId);
			this.blockData[y - 1][z - 1][x - 1] = i < blockDataByte.length ? blockDataByte[i] : 0;
			x++;
			if (x > this.length) {
				x = 1;
				z++;
			}
			if (z > this.width) {
				z = 1;
				y++;
			}
		}

		ListTag entityList = nbtTagCompound.getListOrEmpty("Entities");
		this.entities = new CompoundTag[entityList.size()];
		for (int i = 0; i < entityList.size(); i++) {
			this.entities[i] = entityList.getCompoundOrEmpty(i);
		}

		ListTag tileEntityList = nbtTagCompound.getListOrEmpty("TileEntities");
		this.tileEntities = new CompoundTag[tileEntityList.size()];
		for (int i = 0; i < tileEntityList.size(); i++) {
			this.tileEntities[i] = tileEntityList.getCompoundOrEmpty(i);
		}

		this.initCenterPos();
	}

	/** Best-effort 1.8/1.9 numeric id → modern block. Unknown ids become stone. */
	public static final class LegacyBlocks {
		private LegacyBlocks() {}

		public static Block fromId(int id) {
			return switch (id) {
				case 0 -> Blocks.AIR;
				case 1 -> Blocks.STONE;
				case 2 -> Blocks.GRASS_BLOCK;
				case 3 -> Blocks.DIRT;
				case 4 -> Blocks.COBBLESTONE;
				case 5 -> Blocks.OAK_PLANKS;
				case 7 -> Blocks.BEDROCK;
				case 8, 9 -> Blocks.WATER;
				case 10, 11 -> Blocks.LAVA;
				case 12 -> Blocks.SAND;
				case 13 -> Blocks.GRAVEL;
				case 17 -> Blocks.OAK_LOG;
				case 18 -> Blocks.OAK_LEAVES;
				case 20 -> Blocks.GLASS;
				case 24 -> Blocks.SANDSTONE;
				case 35 -> Blocks.WOOL.white();
				case 41 -> Blocks.GOLD_BLOCK;
				case 42 -> Blocks.IRON_BLOCK;
				case 44 -> Blocks.SMOOTH_STONE_SLAB;
				case 45 -> Blocks.BRICKS;
				case 48 -> Blocks.MOSSY_COBBLESTONE;
				case 49 -> Blocks.OBSIDIAN;
				case 50 -> Blocks.TORCH;
				case 53 -> Blocks.OAK_STAIRS;
				case 64 -> Blocks.OAK_DOOR;
				case 65 -> Blocks.LADDER;
				case 67 -> Blocks.COBBLESTONE_STAIRS;
				case 85 -> Blocks.OAK_FENCE;
				case 89 -> Blocks.GLOWSTONE;
				case 98 -> Blocks.STONE_BRICKS;
				case 112 -> Blocks.NETHER_BRICKS;
				case 133 -> Blocks.EMERALD_BLOCK;
				case 159 -> Blocks.TERRACOTTA;
				default -> {
					// Prefer registry id if it happens to align; otherwise stone
					Block byRaw = BuiltInRegistries.BLOCK.byId(id);
					yield byRaw != null && byRaw != Blocks.AIR ? byRaw : Blocks.STONE;
				}
			};
		}
	}
}
