package modid.challenge.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class ScoreThread extends Thread {
	public int score;
	public int challengenum;
	public EntityPlayer player;
	private boolean retry = false;
	public ScoreThread(int distance, int challengenum, EntityPlayer entityIn) {
		// TODO Auto-generated constructor stub
		this.score=distance;
		this.challengenum=challengenum;
		this.player=entityIn;
	}

	public void run(){
		int i;
		for(i = 0; i<5 && !tryToPostHighscore(); i++){

		};
		if(i==5){
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Failed to post your score."));	
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("There was an error somewhere. Please contact SimJoo about this."));
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("http://minecraftcreations.com/contact.php"));
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Please tell him this: "+encrypt(player.getName()+challengenum+score, "VpzWvUXEPapsh9bx")+"."));	
			retry=true;
		}
		if(retry){
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Do \"/retry\" to retry posting your score."));	
			RetryCommand.retryThread=this;
		}
	}

	private boolean tryToPostHighscore() {
		int highScore;
		try{
			if(challengenum==3 || challengenum==4 || challengenum==5 || challengenum==6 || challengenum==7){
				if(Challenge.eventHandler.challenge.numberOfPlayers!=1){
					score=score/Challenge.eventHandler.challenge.numberOfPlayers;
					Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("This score is divided by "+Challenge.eventHandler.challenge.numberOfPlayers+" as you play with "+Challenge.eventHandler.challenge.numberOfPlayers+" players."));
				}
			}
			if(hasImpossibleScore()){
				Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("You got a score of "+score+" on challenge "+challengenum+"."));	
				Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("This score is in the range of impossible scores for this challenge."));
				Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("It's probably due to a bug. Please report it to SimJoo."));
				Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("http://minecraftcreations.com/contact.php"));
				return true;
			}
			String challengenumString = ""+challengenum;
			if(challengenum<10){
				challengenumString=" "+challengenum;
			}
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(readFile("http://minecraftcreations.com/scorepostc/","username",/*URLEncoder.encode(*/encrypt(player.getName(),"1ZcNZFIvQkbBrs"+challengenumString)/*, "UTF-8")*/,"score",/*URLEncoder.encode(*/encrypt(""+score,"13FRxiEjtS6Cir"+challengenumString)/*, "UTF-8")*/,"id",/*URLEncoder.encode(*/encrypt(""+challengenum,"1Q58jgSh3jLUUQ4V")/*, "UTF-8")*/)));
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Check the leaderboards online at: "));
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("minecraftcreations.com/c"+challengenum));
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Here you can see your ranking vs the rest of the world!"));
			return true;
		} catch (Exception e){
			return false;
		}
	}

	private boolean hasImpossibleScore() {
		// TODO Auto-generated method stub
		switch(challengenum){
		case 1: if(score>900){ return true; }break;
		case 2: if(score>1200){ return true; }break;
		case 3: if(score>2000){ return true; }break;
		case 4: if(score>2000){ return true; }break;
		case 5: if(score>200){ return true; }break;
		case 6: if(score>240){ return true; }break;
		case 7: if(score>2000){ return true; }break;
		case 8: if(score>2600){ return true; }break;
		}
		return false;
	}

	public String encrypt(String input, String key){
		byte[] crypted = null;
		try{
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(input.getBytes());
		}catch(Exception e){
			e.printStackTrace();
		}
		return new String(Base64.encodeBase64(crypted));
	}

	/*public String decrypt(String input, String key){
	    byte[] output = null;
	    try{
	      SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
	      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	      cipher.init(Cipher.DECRYPT_MODE, skey);
	      output = cipher.doFinal(Base64.decodeBase64(input));
	    }catch(Exception e){
	      System.out.println(e.toString());
	    }
	    return new String(output);
	}*/


	private String readFile(String path, String string0, String string1, String string2, String string3, String string4, String string5) {
		try {
			String url = path;

			HttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);

			// add header
			post.setHeader("User-Agent", "Java");

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair(string0, string1));
			urlParameters.add(new BasicNameValuePair(string2, string3));
			urlParameters.add(new BasicNameValuePair(string4, string5));

			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			HttpResponse response = client.execute(post);

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			return result.toString();



		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("Could not post your score online... It's probably due to the fact that you have no internet connection.");
		}
		retry=true;
		return "Could not post your score online... I think you have no internet connection.";
	}

}
