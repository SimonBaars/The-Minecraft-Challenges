package modid.challenge.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import net.minecraft.world.entity.player.Player;

public class ScoreThread extends Thread {
	public int score;
	public int challengenum;
	public Player player;
	private boolean retry = false;

	public ScoreThread(int distance, int challengenum, Player entityIn) {
		this.score = distance;
		this.challengenum = challengenum;
		this.player = entityIn;
	}

	@Override
	public void run() {
		int i;
		for (i = 0; i < 5 && !tryToPostHighscore(); i++) {
		}
		if (i == 5) {
			ClientHooks.chat("Failed to post your score.");
			ClientHooks.chat("There was an error somewhere. Please contact SimJoo about this.");
			ClientHooks.chat("http://minecraftcreations.com/contact.php");
			ClientHooks.chat("Please tell him this: " + encrypt(player.getName().getString() + challengenum + score, "VpzWvUXEPapsh9bx") + ".");
			retry = true;
		}
		if (retry) {
			ClientHooks.chat("Do \"/retry\" to retry posting your score.");
			ChallengeCommands.retryThread = this;
		}
	}

	private boolean tryToPostHighscore() {
		try {
			if (challengenum == 3 || challengenum == 4 || challengenum == 5 || challengenum == 6 || challengenum == 7) {
				if (ChallengeMod.eventHandler.challenge != null && ChallengeMod.eventHandler.challenge.numberOfPlayers != 1) {
					score = score / ChallengeMod.eventHandler.challenge.numberOfPlayers;
					ClientHooks.chat("This score is divided by " + ChallengeMod.eventHandler.challenge.numberOfPlayers
						+ " as you play with " + ChallengeMod.eventHandler.challenge.numberOfPlayers + " players.");
				}
			}
			if (hasImpossibleScore()) {
				ClientHooks.chat("You got a score of " + score + " on challenge " + challengenum + ".");
				ClientHooks.chat("This score is in the range of impossible scores for this challenge.");
				ClientHooks.chat("It's probably due to a bug. Please report it to SimJoo.");
				return true;
			}
			String challengenumString = challengenum < 10 ? (" " + challengenum) : ("" + challengenum);
			ClientHooks.chat(postScore(
				"http://minecraftcreations.com/scorepostc/",
				"username", encrypt(player.getName().getString(), "1ZcNZFIvQkbBrs" + challengenumString),
				"score", encrypt("" + score, "13FRxiEjtS6Cir" + challengenumString),
				"id", encrypt("" + challengenum, "1Q58jgSh3jLUUQ4V")));
			ClientHooks.chat("Check the leaderboards online at: ");
			ClientHooks.chat("minecraftcreations.com/c" + challengenum);
			ClientHooks.chat("Here you can see your ranking vs the rest of the world!");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean hasImpossibleScore() {
		return switch (challengenum) {
			case 1 -> score > 900;
			case 2 -> score > 1200;
			case 3, 4, 7 -> score > 2000;
			case 5 -> score > 200;
			case 6 -> score > 240;
			case 8 -> score > 2600;
			default -> false;
		};
	}

	public String encrypt(String input, String key) {
		byte[] crypted = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Base64.getEncoder().encodeToString(crypted);
	}

	private String postScore(String path, String k0, String v0, String k1, String v1, String k2, String v2) {
		try {
			HttpURLConnection conn = (HttpURLConnection) URI.create(path).toURL().openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("User-Agent", "Java");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			String body = k0 + "=" + URLEncoder.encode(v0, StandardCharsets.UTF_8)
				+ "&" + k1 + "=" + URLEncoder.encode(v1, StandardCharsets.UTF_8)
				+ "&" + k2 + "=" + URLEncoder.encode(v2, StandardCharsets.UTF_8);
			try (OutputStream os = conn.getOutputStream()) {
				os.write(body.getBytes(StandardCharsets.UTF_8));
			}
			try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
				StringBuilder result = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) result.append(line);
				return result.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		retry = true;
		return "Could not post your score online... I think you have no internet connection.";
	}
}
