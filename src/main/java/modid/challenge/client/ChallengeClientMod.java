package modid.challenge.client;

import com.mojang.blaze3d.platform.InputConstants;
import modid.challenge.core.ChallengeMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class ChallengeClientMod implements ClientModInitializer {
	public static KeyMapping flap;

	@Override
	public void onInitializeClient() {
		flap = KeyMappingHelper.registerKeyMapping(new KeyMapping(
			"key.challenge.flap",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_G,
			KeyMapping.Category.register(Identifier.fromNamespaceAndPath(ChallengeMod.MOD_ID, "challenges"))
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (ChallengeMod.eventHandler != null) {
				ChallengeMod.eventHandler.onClientTick();
				while (flap.consumeClick()) {
					ChallengeMod.eventHandler.onFlap();
				}
			}
		});
	}
}
