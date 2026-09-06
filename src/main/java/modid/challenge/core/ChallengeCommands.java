package modid.challenge.core;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import modid.challenge.challenges.ChallengeEight;
import modid.challenge.challenges.ChallengeFive;
import modid.challenge.challenges.ChallengeFour;
import modid.challenge.challenges.ChallengeNine;
import modid.challenge.challenges.ChallengeOne;
import modid.challenge.challenges.ChallengeSeven;
import modid.challenge.challenges.ChallengeSix;
import modid.challenge.challenges.ChallengeThree;
import modid.challenge.challenges.ChallengeTwo;
import modid.challenge.challenges.Challenges;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public final class ChallengeCommands {
	public static ScoreThread retryThread;

	private ChallengeCommands() {}

	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(Commands.literal("challenge")
				.then(Commands.argument("num", IntegerArgumentType.integer(1, 9))
					.then(Commands.argument("x", IntegerArgumentType.integer())
						.then(Commands.argument("y", IntegerArgumentType.integer())
							.then(Commands.argument("z", IntegerArgumentType.integer())
								.executes(ctx -> {
									if (ChallengeMod.eventHandler.challenge != null) {
										ctx.getSource().sendFailure(Component.literal("A challenge is already running"));
										return 0;
									}
									int num = IntegerArgumentType.getInteger(ctx, "num");
									int x = IntegerArgumentType.getInteger(ctx, "x");
									int y = IntegerArgumentType.getInteger(ctx, "y");
									int z = IntegerArgumentType.getInteger(ctx, "z");
									Challenges c = getChallenge(num, x, y, z);
									if (c == null) {
										ctx.getSource().sendFailure(Component.literal("Invalid challenge number"));
										return 0;
									}
									ChallengeMod.eventHandler.challenge = c;
									ctx.getSource().sendSuccess(() -> Component.literal("Started challenge " + num), false);
									return 1;
								}))))));

			dispatcher.register(Commands.literal("startchallenge")
				.redirect(dispatcher.getRoot().getChild("challenge")));
			dispatcher.register(Commands.literal("createchallenge")
				.redirect(dispatcher.getRoot().getChild("challenge")));

			dispatcher.register(Commands.literal("retry")
				.executes(ctx -> {
					if (!ScoreThread.ONLINE_LEADERBOARD_ENABLED) {
						ctx.getSource().sendFailure(Component.literal(ScoreThread.HOST_STATUS));
						return 0;
					}
					if (retryThread != null) {
						ScoreThread thread = new ScoreThread(retryThread.score, retryThread.challengenum, retryThread.player);
						retryThread = null;
						thread.start();
						ctx.getSource().sendSuccess(() -> Component.literal("Retrying score post..."), false);
						return 1;
					}
					ctx.getSource().sendFailure(Component.literal("There was no score that failed posting"));
					return 0;
				}));
		});
	}

	private static Challenges getChallenge(int challengenum, int x, int y, int z) {
		return switch (challengenum) {
			case 1 -> new ChallengeOne(x, y, z);
			case 2 -> new ChallengeTwo(x, y, z);
			case 3 -> new ChallengeThree(x, y, z);
			case 4 -> new ChallengeFour(x, y, z);
			case 5 -> new ChallengeFive(x, y, z);
			case 6 -> new ChallengeSix(x, y, z);
			case 7 -> new ChallengeSeven(x, y, z);
			case 8 -> new ChallengeEight(x, y, z);
			case 9 -> new ChallengeNine(x, y, z);
			default -> null;
		};
	}
}
