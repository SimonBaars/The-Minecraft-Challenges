package modid.challenge.challenges;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import modid.challenge.core.Challenge;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings.GameType;

public class ChallengeOne extends Challenges {
	int jumpRow=0;
	int runwaySize=20;
	int runwayWidth=4;
	final int maxHeight=9;
	int rowSize = 1;
	int currentHeight=1;
	boolean randomlyPlaced = false;
	boolean runwaySmaller=false;
	//Block[][][] blockMapping = new Block[runwayWidth+2][maxHeight+1][runwaySize+4];
	
	public ChallengeOne(int x, int y, int z) {
		super(x,y,z,GameType.ADVENTURE,EnumDifficulty.PEACEFUL);
		showScore();
		for(EntityPlayerMP player : players){
		player.setPosition(x-(runwayWidth/2)-1,y+3	,z-(runwaySize/2));
		//((EntityPlayerMP)MinecraftServer.getServer().worldServerForDimension(0).getPlayerEntityByName(Minecraft.getMinecraft().thePlayer.getName())).setPosition(x-(runwayWidth/2)-2,y+4	,z-(runwaySize/4)+1);
		}
		resetPlayer();
		firstRow();
	}
	
	void firstRow(){
		placeBlocks(Blocks.air, x-(runwayWidth/2), y-2, z-(runwaySize/4), runwayWidth, maxHeight, runwaySize); //Zet alles naar air
		//System.out.println("Airdone");
		placeBlocks(Challenge.BlockTouchable, x-(runwayWidth/2), y-2, z-(runwaySize/4), runwayWidth, 1, runwaySize); //onder renbaan
		placeBlocks(Challenge.BlockTouchable, x-(runwayWidth/2), y-2, z-(runwaySize/4)+1, runwayWidth, maxHeight, 1);//rand achter
		placeBlocks(Challenge.BlockBlueRock, x-(runwayWidth/2)+1, y-1, z-(runwaySize/4), 1, maxHeight-1, runwaySize); //rand links
		placeBlocks(Challenge.BlockBlueRock, x-((int)(3.00*(runwayWidth/2.00))), y-1, z-(runwaySize/4), 1, maxHeight-1, runwaySize);//rand rechts
		placeBlocks(Challenge.BlockTouchable, x-(runwayWidth/2)+1, y-2, z-((int)(5.00*(runwaySize/4.00))), runwayWidth+2, maxHeight, 1);//rand voor
		placeBlocks(Challenge.BlockBlackRock, x-(runwayWidth/2), y-1, z-(runwaySize/4), runwayWidth, 1, runwaySize); //renbaan
	}
	
	void placeRow(boolean isJumpRow){
		int score = getScore();
		int rowZ=z-((int)(5.00*(runwaySize/4.00)))-score;
		placeBlocks(Blocks.air, x-(runwayWidth/2), y-1, rowZ, runwayWidth, maxHeight, 1); //oude rand voor naar air
		placeBlocks(Blocks.air, x-(runwayWidth/2)+1, y-2, z-(runwaySize/4)+2-score, runwayWidth+2, maxHeight, 1);//rand achter naar air
		//placeBlocks(Challenge.BlockTouchable, x-(runwayWidth/2), y-2, z-(runwaySize/4)-score, runwayWidth, maxHeight, 1);//niewe rand achter
		placeBlocks(Challenge.BlockTouchable, x-(runwayWidth/2), y-2, z-(runwaySize/4)-score+1, runwayWidth, maxHeight, 2);//niewe rand achter
		placeBlocks(Challenge.BlockBlueRock, x-(runwayWidth/2)+1, y-1, rowZ, 1, maxHeight-1, 1); //rand links
		placeBlocks(Challenge.BlockBlueRock, x-((int)(3.00*(runwayWidth/2.00))), y-1, rowZ, 1, maxHeight-1, 1);//rand rechts
		//placeBlocks(Challenge.BlockTouchable, x-(runwayWidth/2)+1, y-1, rowZ, 1, maxHeight, 1); //rand links
		//placeBlocks(Challenge.BlockTouchable, x-((int)(3.00*(runwayWidth/2.00))), y-1, rowZ, 1, maxHeight, 1);//rand rechts
		placeBlocks(Challenge.BlockTouchable, x-(runwayWidth/2)+1, y-2, rowZ-1, runwayWidth+2, maxHeight, 1);//nieuwe rand voor
		if(isJumpRow){
			//System.out.println("Jumprow at "+currentHeight);
			if(randomlyPlaced){
				placeBlocks(Challenge.BlockBlackRock, x-(runwayWidth/2)-((int)(Math.random()*runwayWidth)), y-2+currentHeight, rowZ, 1, 1, 1);//De springbaan
			} else {
				placeBlocks(Challenge.BlockBlackRock, x-(runwayWidth/2), y-2+currentHeight, rowZ, runwayWidth, 1, 1);//De springbaan
			}
		}
	}
	
	void destroy(){
		placeBlocks(Blocks.air, x-(runwayWidth/2)+1, y-2, z-(runwaySize/4)-getScore()+2, runwayWidth+2, maxHeight+1, runwaySize+4);
	}
	
	/*boolean addToMap(IBlockState state, int x, int y, int z){
		x = x-this.x+(runwaySize/4)+1;
		y = y-this.y+2;
		z = z-this.z+((int)(5.00*(runwaySize/4.00)))+1+score;
		if(x>=0 && x<blockMapping.length && y>=0 && y<blockMapping[0].length && z>=0 && z<blockMapping[0][0].length){
		blockMapping[x][y][z]=state.getBlock();
		return true;
		} else {
			System.out.println(score+", "+x+", "+y+", "+z);
		}
		return false;
	}*/
	
	/*boolean checkPreviousRow(){
		
		for(int x = 0; x<blockMapping.length; x++){
			for(int y =0; y<blockMapping[0].length; y++){
				for(int z =0; z<blockMapping[0][0].length; z++){
					if(blockMapping[x][y][z]!=null && blockMapping[x][y][z]!=worldIn.getBlockState(new BlockPos( this.x-(runwayWidth/2)+1+x, this.y-2+y, z-(runwaySize/4)-score+2+z))){
						//Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent) new ChatComponentText("You may not place any blocks in the challenge area."));
						System.out.println(score+", "+x+", "+y+", "+z+", "+blockMapping[x][y][z]+", "+worldIn.getBlockState(new BlockPos( this.x-(runwayWidth/2)+1+x, this.y-2+y, z-(runwaySize/4)-score+2+z)));
						//removeThisChallenge();
						//return true;
					}
				}
			}
		}
		return false;
	}*/
	
	boolean closeToGameRoom(int howClose, int x, int y, int z){
		x = x-this.x+(runwaySize/4)+1-howClose;
		y = y-this.y+2-howClose;
		z = z-this.z+((int)(5.00*(runwaySize/4.00)))+1+getScore()-howClose;
		//System.out.println(x+", "+y+", "+z+", "+(x>=0)+", "+(x<=runwayWidth+2)+", "+(y>=0)+", "+(y<=maxHeight+2)+", "+(z>=0)+", "+(z<=runwaySize+2)+", "+(x>=0 && x<=runwayWidth+2 && y>=0 && y<=maxHeight+2 && z>=0 && z<=runwaySize+2));
		return (x>=0 && x<=runwayWidth+2+(2*howClose) && y>=0 && y<=maxHeight+2+(2*howClose) && z>=0 && z<=runwaySize+1+(2*howClose));
	}
	
	public boolean run(){
		if(waitTime==2000){
			waitTime=700;
		}
		if(Minecraft.getMinecraft().thePlayer!=null){
		if(resetPlayer()){
			return true;
		}
		if(jumpRow==0){
		currentHeight+=calculateRandomHeight();
		}
		/*if(checkPreviousRow()){
			return true;
		}*/
		placeRow(jumpRow==0);
		increaseScore();
		showScore();
		waitTime--;
		
		
		int score = getScore();
		switch(getScore()){
		case 50: case 10: case 90: case 300: randomlyPlaced=true; break;
		case 30: case 70: rowSize++;randomlyPlaced=false;break;
		case 120: randomlyPlaced=true;rowSize=2;break;
		}
		if(score>=100 && score<120){
			randomlyPlaced=((int)(Math.random()*2))==0;
			rowSize = ((int)(Math.random()*3))+1;
		} else if (score>=130 && score<=250 && score%10==0){
			/*runwaySmaller=true;*/
			randomlyPlaced=((int)(Math.random()*2))==0;
			rowSize = ((int)(Math.random()*3))+1;
		} else if(score>=260 && score<300){
			rowSize=3;
			randomlyPlaced=((int)(Math.random()*2))==0;
		} 
		if(runwaySmaller && jumpRow!=0){
			runwaySize--;
			runwaySmaller=false;
		}
		jumpRow++;
		if(jumpRow>=rowSize){
			jumpRow=0;
		}
		register();
		}
		return false;
	}
	
	void register(){
		//System.out.println("Registered "+at);
		PrintWriter writer;
		try {
			(new File("saves/"+Minecraft.getMinecraft().getIntegratedServer().getFolderName())).mkdirs();
			writer = new PrintWriter("saves/"+Minecraft.getMinecraft().getIntegratedServer().getFolderName()+"/challenge.txt", "UTF-8");
			writer.println(x-(runwayWidth/2)+1);
			writer.println(y-2);
			writer.println(z-(runwaySize/4)-getScore()+2);
			writer.println(runwayWidth+2);
			writer.println(maxHeight+1);
			writer.println(runwaySize+4);
		writer.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int calculateRandomHeight() {
		if(currentHeight==1){
			return (int)(Math.random()*2);
		} else if(currentHeight==maxHeight-3){
			return -1+((int)(Math.random()*2));
		}
		return -1+((int)(Math.random()*3));
	}
}
