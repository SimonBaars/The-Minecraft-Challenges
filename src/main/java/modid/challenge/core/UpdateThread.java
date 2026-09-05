package modid.challenge.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class UpdateThread extends Thread {
	@Override
	public void run() {
		String updateMessage = readFile("http://minecraftcreations.com/challenge7.txt");
		if ("1".equals(updateMessage)) {
			if (ClientHooks.localPlayer() != null) {
				ClientHooks.chat("There's a new update for The Minecraft Challenges available. To be able to keep playing, you must download it.");
			} else {
				ChallengeMod.updateChecked = false;
			}
		}
		if (ClientHooks.localPlayer() == null) return;
		String[] highscores = readFile("http://minecraftcreations.com/scorepostc/highscore.php?player="
			+ ClientHooks.localPlayer().getName().getString()).split(",");
		if (highscores.length == 9) {
			for (int i = 0; i < highscores.length; i++) {
				try {
					ChallengeMod.highscores[i] = Integer.parseInt(highscores[i].trim());
				} catch (NumberFormatException ignored) {
				}
			}
		}
	}

	public static String readFile(String path) {
		try {
			var conn = URI.create(path).toURL().openConnection();
			conn.setRequestProperty("User-Agent", "Java");
			try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
				StringBuilder result = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) result.append(line);
				return result.toString();
			}
		} catch (Exception e) {
			return "";
		}
	}
}
