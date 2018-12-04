package modid.challenge.structureloader;

import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LightUpdateThread extends Thread {
	private World[] worldIn=new World[2];
	public ArrayList<BlockPos> processes = new ArrayList<BlockPos>();
	
    public LightUpdateThread(World worldIn, World serverWorld){
    	this.worldIn[0]=worldIn;
    	this.worldIn[1]=serverWorld;
    }
    
	public void run() {
		while(true){
		while(processes.size()>0){
			for(int i = 0; i<2; i++){
			//worldIn[i].theProfiler.startSection("checkLight");
				if(processes.get(0)!=null){
			worldIn[i].checkLight(processes.get(0));
				//worldIn[i].setBlockState(processes.get(0), blocks.get(0));
			worldIn[i].getChunkFromBlockCoords(processes.get(0)).setChunkModified();
				}
            //worldIn[i].theProfiler.endSection();
			//worldIn[i].getChunkProvider().provideChunk(processes.get(0));
			//worldIn[i].scheduleUpdate(processes.get(0), worldIn[i].getBlockState(processes.get(0)).getBlock(), 2);
			}
			processes.remove(0);
		}
		try {
			this.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
	
	public int getProcessesSize(){
		return processes.size();
	}
}
