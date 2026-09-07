package modid.challenge.structureloader;

import java.io.DataInputStream;
import java.util.zip.GZIPInputStream;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Loads legacy .structure schematics (numeric block ids from 1.8/1.9 era).
 * Block+meta are resolved at place-time via {@link LegacyBlockStates}.
 */
public class SchematicStructure extends Structure {
	public boolean isLive;
	/** Pre-flattening block ids; -1 = unset / unmapped. */
	private int[][][] legacyIds;
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
					int legacyId = this.legacyIds[y][z][x];
					if (legacyId < 0) continue;
					BlockState state = LegacyBlockStates.fromLegacy(legacyId, this.blockData[y][z][x]);
					if (state == null) continue;
					if (this.blockMode.equals("overlay") && state.isAir()) continue;
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
			this.legacyIds = new int[1][1][1];
			this.legacyIds[0][0][0] = 1; // stone
			this.blockData = new int[1][1][1];
			this.entities = new CompoundTag[0];
			this.tileEntities = new CompoundTag[0];
			this.initCenterPos();
			return;
		}

		this.length = nbtTagCompound.getShortOr("Width", (short) 1);
		this.width = nbtTagCompound.getShortOr("Length", (short) 1);
		this.height = nbtTagCompound.getShortOr("Height", (short) 1);

		this.legacyIds = new int[this.height][this.width][this.length];
		this.blockData = new int[this.height][this.width][this.length];
		for (int y0 = 0; y0 < this.height; y0++) {
			for (int z0 = 0; z0 < this.width; z0++) {
				for (int x0 = 0; x0 < this.length; x0++) {
					this.legacyIds[y0][z0][x0] = -1;
				}
			}
		}

		byte[] blockIdsByte = nbtTagCompound.getByteArray("Blocks").orElse(new byte[0]);
		byte[] blockDataByte = nbtTagCompound.getByteArray("Data").orElse(new byte[blockIdsByte.length]);
		int x = 1, y = 1, z = 1;
		for (int i = 0; i < blockIdsByte.length; i++) {
			int blockId = blockIdsByte[i] & 0xFF;
			int meta = i < blockDataByte.length ? (blockDataByte[i] & 0xFF) : 0;
			// Keep raw id+meta; BlockState resolved at process-time via LegacyBlockStates
			this.legacyIds[y - 1][z - 1][x - 1] = blockId;
			this.blockData[y - 1][z - 1][x - 1] = meta;
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
}
