package modid.challenge.structureloader;

import java.io.InputStream;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Structure
{
	public static final int STRUCTURE_BLOCK_LIMIT = 100000;

	public int length;
	public int height;
	public int width;

	protected String fileName;
	public InputStream fileStream;
	protected String blockMode;
	protected boolean blockUpdate;

	public Float centerX;
	public Float centerY;
	public Float centerZ;
	protected Vec3d centerPos;

	public Structure(String fileName) 
	{
		this.blockMode = "replace";
		this.blockUpdate = true;
		this.fileName = "/assets/challenge/structs/"+fileName+".structure";
		this.fileStream = Structure.class.getResourceAsStream(this.fileName);
	}

	public void readFromFile()
	{

	}
	
	public void doNotReplaceAir(){
		blockMode="overlay";
	}

	public void process(World serverWorld, World world, int posX, int posY, int posZ)
	{

	}

	public Vec3d getCenterPos()
	{
		return this.centerPos;
	}

	protected void initCenterPos()
	{
		int defaultCenterX = (int) (this.length / 2.0F);
		int defaultCenterZ = (int) (this.width / 2.0F);
		if (this.centerX == null) this.centerX = defaultCenterX + 0.5F;
		if (this.centerY == null) this.centerY = 0.0F;
		if (this.centerZ == null) this.centerZ = defaultCenterZ + 0.5F;
		this.centerPos = new Vec3d(this.centerX, this.centerY, this.centerZ);
	}
}
