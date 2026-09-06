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

/**
 * Legacy online score post to {@code minecraftcreations.com/scorepostc/}.
 * <p>
 * That host is <b>N/A / deferred</b>: the domain is parked / for-sale (AboveDomains)
 * and every legacy endpoint returns the same parking HTML (verified 2026-09-05 PT).
 * Global world rankings cannot be ported without that backend. In-session personal
 * highs still update {@link ChallengeMod#highscores}; no remote post is attempted
 * unless {@link #ONLINE_LEADERBOARD_ENABLED} is flipped (opt-in for a revived host).
 */
public class ScoreThread extends Thread {
	/**
	 * Opt-in only. Default false — host is parked for-sale, not temporarily down.
	 * Set true (or {@code -Dchallenge.onlineLeaderboard=true}) if the legacy API returns.
	 */
	public static final boolean ONLINE_LEADERBOARD_ENABLED =
		Boolean.parseBoolean(System.getProperty("challenge.onlineLeaderboard", "false"));

	public static final String HOST_STATUS =
		"Online leaderboard N/A/deferred — minecraftcreations.com is parked/for-sale (no scorepost API).";

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
		if (!ONLINE_LEADERBOARD_ENABLED) {
			announceLocalOnly();
			return;
		}
		int i;
		for (i = 0; i < 5 && !tryToPostHighscore(); i++) {
		}
		if (i == 5) {
			ClientHooks.chat("Failed to post your score.");
			ClientHooks.chat(HOST_STATUS);
			retry = true;
		}
		if (retry) {
			ClientHooks.chat("Do \"/retry\" to retry posting your score (only useful if the host returns).");
			ChallengeCommands.retryThread = this;
		}
	}

	private void announceLocalOnly() {
		// Score already announced by Challenges.endChallenge; only note host status.
		ClientHooks.chat(HOST_STATUS);
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
				ClientHooks.chat("It's probably due to a bug. Please report it.");
				return true;
			}
			String challengenumString = challengenum < 10 ? (" " + challengenum) : ("" + challengenum);
			String postResult = postScore(
				"http://minecraftcreations.com/scorepostc/",
				"username", encrypt(player.getName().getString(), "1ZcNZFIvQkbBrs" + challengenumString),
				"score", encrypt("" + score, "13FRxiEjtS6Cir" + challengenumString),
				"id", encrypt("" + challengenum, "1Q58jgSh3jLUUQ4V"));
			ClientHooks.chat(sanitizeChat(postResult));
			if (looksLikeParkingPage(postResult)) {
				ClientHooks.chat(HOST_STATUS);
				retry = false;
				return true;
			}
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

	private static boolean looksLikeParkingPage(String msg) {
		if (msg == null) return false;
		String t = msg.trim();
		return t.startsWith("<!DOCTYPE") || t.startsWith("<html") || t.contains("abovedomains")
			|| t.contains("may be for sale");
	}

	private static String sanitizeChat(String msg) {
		if (msg == null || msg.isBlank()) {
			return "Could not post your score online.";
		}
		String trimmed = msg.trim();
		if (looksLikeParkingPage(trimmed) || trimmed.contains("<body")) {
			return "Could not post your score online (leaderboard host returned a web page).";
		}
		if (trimmed.length() > 200) {
			return trimmed.substring(0, 200) + "...";
		}
		return trimmed;
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
