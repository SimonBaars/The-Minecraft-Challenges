package modid.challenge.challenges;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import modid.challenge.core.Challenge;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings.GameType;

public class ChallengeNine extends Challenges {
	int jumpRow=0;
	int runwaySize=80;
	int runwayWidth=20;
	int maxHeight=20;
	int rowSize = 25;
	//int currentHeight=1;
	boolean randomlyPlaced = false;
	byte runwaySmaller=0;
	int alterMap=0;
	//Block[][][] blockMapping = new Block[runwayWidth+2][maxHeight+1][runwaySize+4];
	private int openingSize=8;
	public ChallengeNine(int x, int y, int z) {
		super(x,y,z, GameType.ADVENTURE,EnumDifficulty.PEACEFUL);
		showScore();
		for(EntityPlayerMP player : players){
		player.setPosition(x,y+3	,z-(runwaySize/2));
		//((EntityPlayerMP)MinecraftServer.getServer().worldServerForDimension(0).getPlayerEntityByName(Minecraft.getMinecraft().thePlayer.getName())).setPosition(x-(runwayWidth/2)-2,y+4	,z-(runwaySize/4)+1);
		}
		Minecraft.getMinecraft().thePlayer.setPosition(x,y+3	,z-(runwaySize/2));
		waitTime=50;
		resetPlayer();
		firstRow();
		serverWorld.setWorldTime(0);
		Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Press G to flap your wings"));
	}
	
	void firstRow(){
		placeBlocks(Blocks.air, x+(runwayWidth/2), y-2, z-(runwaySize/4), runwayWidth, maxHeight, runwaySize); //Zet alles naar air
		//System.out.println("Airdone");
		placeBlocks(Challenge.BlockTouchable, x+(runwayWidth/2), y-2, z-(runwaySize/4), runwayWidth, 1, runwaySize); //onder renbaan
		placeBlocks(Challenge.BlockTouchable, x+(runwayWidth/2), y-2, z-(runwaySize/4)+1, runwayWidth, maxHeight, 1);//rand achter
		placeBlocks(Challenge.BlockTouchable, x+(runwayWidth/2)+1, y-1, z-(runwaySize/4), 1, maxHeight-1, runwaySize); //rand links
		placeBlocks(Challenge.BlockTouchable, x-(runwayWidth/2)+1, y-1, z-(runwaySize/4), 1, maxHeight-1, runwaySize);//rand rechts
		placeBlocks(Challenge.BlockTouchable, x+(runwayWidth/2)+1, y-2, z-((int)(5.00*(runwaySize/4.00))), runwayWidth+2, maxHeight, 1);//rand voor
		placeBlocks(Challenge.BlockBlackRock, x+(runwayWidth/2), y-1, z-(runwaySize/4), runwayWidth, 1, runwaySize/2); //renbaan
	}
	
	void placeRow(boolean isJumpRow){
		int score = getScore();
		int rowZ=z-((int)(5.00*(runwaySize/4.00)))-score;
		placeBlocks(Blocks.air, x+(runwayWidth/2), y-1, rowZ, runwayWidth, maxHeight+1, 1); //oude rand voor naar air
		placeBlocks(Blocks.air, x+(runwayWidth/2)+1, y-2, z-(runwaySize/4)+2-score, runwayWidth+2, maxHeight+1, 1);//rand achter naar air
		//placeBlocks(Challenge.BlockTouchable, x-(runwayWidth/2), y-2, z-(runwaySize/4)-score, runwayWidth, maxHeight, 1);//niewe rand achter
		placeBlocks(Challenge.BlockTouchable, x+(runwayWidth/2), y-2, z-(runwaySize/4)-score+1, runwayWidth+1, maxHeight, 2);//niewe rand achter
		placeBlocks(Challenge.BlockTouchable, x+(runwayWidth/2)+1, y-1, rowZ, 1, maxHeight, 1); //rand links
		placeBlocks(Challenge.BlockTouchable, x-(runwayWidth/2)+1, y-1, rowZ, 1, maxHeight, 1);//rand rechts
		//placeBlocks(Challenge.BlockTouchable, x-(runwayWidth/2)+1, y-1, rowZ, 1, maxHeight, 1); //rand links
		//placeBlocks(Challenge.BlockTouchable, x-((int)(3.00*(runwayWidth/2.00))), y-1, rowZ, 1, maxHeight, 1);//rand rechts
		placeBlocks(Challenge.BlockTouchable, x+(runwayWidth/2)+1, y-2, rowZ-1, runwayWidth+2, 1, 1);//nieuwe rand voor
		//placeBlocks(Blocks.glass, x+(runwayWidth/2)+1, y-2+maxHeight, rowZ, runwayWidth+2, 1, 1);//glazen wand boven
		//placeBlocks(Challenge.BlockBlackRock, x+(runwayWidth/2), y-1, rowZ, runwayWidth, 1, 1);//De springbaan
		if(isJumpRow){
			//System.out.println("Jumprow at "+currentHeight);
			placeBlocks(Challenge.BlockTouchable, x+(runwayWidth/2), y-1, rowZ, runwayWidth, maxHeight, 1);//Obstakel
			int holex = ((int)(Math.random()*(runwayWidth-openingSize)));
			int holey = ((int)(Math.random()*(maxHeight-openingSize-6)))+5;
			placeBlocks(Blocks.glowstone, x+(runwayWidth/2)-holex+1, y+holey-1, rowZ, 1, 1, 1);//Glowstone om het gat waar je doorheen moet
			placeBlocks(Blocks.glowstone, x+(runwayWidth/2)-holex-openingSize, y+holey-1, rowZ, 1, 1, 1);//Glowstone om het gat waar je doorheen moet
			placeBlocks(Blocks.glowstone, x+(runwayWidth/2)-holex+1, y+holey+openingSize, rowZ, 1, 1, 1);//Glowstone om het gat waar je doorheen moet
			placeBlocks(Blocks.glowstone, x+(runwayWidth/2)-holex-openingSize, y+holey+openingSize, rowZ, 1, 1, 1);//Glowstone om het gat waar je doorheen moet
			placeBlocks(Blocks.air, x+(runwayWidth/2)-holex, y+holey, rowZ, openingSize, openingSize, 1);//Gat waar je doorheen moet
			//System.out.print((x-(runwayWidth/2)+((int)(Math.random()*(runwayWidth-openingSize)))));
		}
	}
	
	private int getPositive(int i) {
		// TODO Auto-generated method stub
		if(i>0){
			return i;
		}
		return 0;
	}

	private int booleanToInt(boolean bool){
		return bool ? 1:0;
	}
	
	void destroy(){
		placeBlocks(Blocks.air, x+(runwayWidth/2)+1, y-2, z-(runwaySize/4)-getScore()+2, runwayWidth+2, maxHeight+1, runwaySize+4);
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
						//Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("You may not place any blocks in the challenge area."));
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
		x = x-this.x+(runwayWidth/2)-1-howClose;
		y = y-this.y+2-howClose;
		z = z-this.z+((int)(5.00*(runwaySize/4.00)))+1+getScore()-howClose;
		//System.out.println(x+", "+y+", "+z+", "+(x>=0)+", "+(x<=runwayWidth+2)+", "+(y>=0)+", "+(y<=maxHeight+2)+", "+(z>=0)+", "+(z<=runwaySize+2)+", "+(x>=0 && x<=runwayWidth+2 && y>=0 && y<=maxHeight+2 && z>=0 && z<=runwaySize+2));
		return (x>=0 && x<=runwayWidth+(2*howClose) && y>=0 && y<=maxHeight+(2*howClose) && z>=0 && z<=runwaySize+2+(2*howClose));
	}
	
	public boolean run(){
		if(Minecraft.getMinecraft().thePlayer!=null){
		if(resetPlayer()){
			return true;
		}
		int increase = 0;
		while(doIncreaseDistance()){
			increase++;
			if(increase>runwaySize){
				System.out.println("Couldn't keep up! Please make sure your pc can handle this challenge!");
				for(int i  = 0; i<alivePlayers.size(); i++){
					endChallenge(alivePlayers.get(i));
					i--;
					}
					return true;
			}
			//Minecraft.getMinecraft().thePlayer.moveEntityWithHeading(0, 1);
			//Minecraft.getMinecraft().thePlayer.setSprinting(true);
			//Minecraft.getMinecraft().thePlayer.sprintingTicksLeft=9999999;
			//Minecraft.getMinecraft().thePlayer.setSprinting(false);
			//if(doHelpSprinting){
				Minecraft.getMinecraft().thePlayer.setSprinting(false);
			//}
		/*if(jumpRow==0){
		currentHeight+=calculateRandomHeight();
		}*/
		/*if(checkPreviousRow()){
			return true;
		}*/
		placeRow(jumpRow==0);
		
		increaseScore();
		
		showScore();
		waitTime--;
		int score = getScore();
		if(score==150 || score==250 ||score==350 ||score==450||score==550){
			openingSize--;
		} 
		if (score%98==0 && rowSize>15){
			rowSize--;
		}/*else if (score%45==0 && maxHeight<100){
		}
			maxHeight++;
		}*/
		jumpRow++;
		if(jumpRow>=rowSize){
			jumpRow=0;
		}
		register();
		}}
		return false;
	}
	
	private boolean doIncreaseDistance() {
		//System.out.println(((int)Minecraft.getMinecraft().thePlayer.posZ)+", "+(z-score-(runwaySize/2))+", "+(((int)Minecraft.getMinecraft().thePlayer.posZ)>z-score-(runwaySize/2)));
		if(((int)Minecraft.getMinecraft().thePlayer.posZ)<z-getScore()-(runwaySize/2)){
			return true;
		}
		return false;
	}

	void register(){
		//System.out.println("Registered "+at);
		PrintWriter writer;
		try {
			(new File("saves/"+Minecraft.getMinecraft().getIntegratedServer().getFolderName())).mkdirs();
			writer = new PrintWriter("saves/"+Minecraft.getMinecraft().getIntegratedServer().getFolderName()+"/challenge.txt", "UTF-8");
			writer.println(x+(runwayWidth/2)+1);
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
}
