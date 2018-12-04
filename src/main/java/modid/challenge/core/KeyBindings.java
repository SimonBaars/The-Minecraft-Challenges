package modid.challenge.core;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {

    // Declare two KeyBindings, ping and pong
    public static KeyBinding flap;

    public static void init() {
        // Define the "ping" binding, with (unlocalized) name "key.ping" and
        // the category with (unlocalized) name "key.categories.mymod" and
        // key code 24 ("O", LWJGL constant: Keyboard.KEY_O)
    	flap = new KeyBinding("Flap", Keyboard.KEY_G, "The Minecraft Challenges");

        // Register both KeyBindings to the ClientRegistry
        ClientRegistry.registerKeyBinding(flap);
    }

}
