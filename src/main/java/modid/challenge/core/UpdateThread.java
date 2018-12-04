package modid.challenge.core;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class UpdateThread extends Thread {
public void run(){
	String updateMessage = readFile("http://minecraftcreations.com/challenge7.txt");
	if(updateMessage.equals("1")){
		if(Minecraft.getMinecraft().thePlayer!=null){
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("There's a new update for The Minecraft Challenges available. To be able to keep playing, you must download it."));
		} else {
			Challenge.updateChecked=false;
		}
		}
	
	String[] highscores = readFile("http://minecraftcreations.com/scorepostc/highscore.php?player="+Minecraft.getMinecraft().thePlayer.getName()).split(",");
	if(highscores.length==9){
		for(int i = 0; i<highscores.length; i++){
			Challenge.highscores[i]=Integer.parseInt(highscores[i]);
			//System.out.println(Challenge.highscores[i]+" loaded!");
		}
	}
}
public static String readFile(String path) {
	try {
		String webPage = path;
		URL url = new URL(webPage);
		URLConnection urlConnection = url.openConnection();
		InputStream is = urlConnection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);

		int numCharsRead;
		char[] charArray = new char[1024];
		StringBuffer sb = new StringBuffer();
		while ((numCharsRead = isr.read(charArray)) > 0) {
			sb.append(charArray, 0, numCharsRead);
		}
		String result = sb.toString();

		return result;
	} catch (Exception e) {
		
	}
		return "0";
			}



}
