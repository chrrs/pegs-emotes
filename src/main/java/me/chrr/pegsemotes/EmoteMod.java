package me.chrr.pegsemotes;

import me.chrr.pegsemotes.config.Config;
import me.chrr.pegsemotes.config.ConfigManager;
import me.chrr.pegsemotes.emote.EmoteRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.File;

public class EmoteMod implements ClientModInitializer {
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    public static final char EMOTE_PREFIX = '\ua950';
    public static final int EMOTE_HEIGHT = 8;

    private Config config;
    private static KeyBinding reloadKeyBinding;

    @Override
    public void onInitializeClient() {
        config = ConfigManager.read(getConfigFile());
        EmoteRegistry.getInstance().loadRepositoriesFromConfig(config);

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return new Identifier("pegs-emotes", "emotes");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        EmoteRegistry.getInstance().loadRepositoriesFromConfig(config);
                        EmoteRegistry.getInstance().clearCache();
                        EmoteRegistry.getInstance().refetchEmotes();
                    }
                }
        );

        reloadKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.pegs-emotes.reload", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F4, "category.pegs-emotes"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (reloadKeyBinding.wasPressed()) {
                EmoteRegistry.getInstance().loadRepositoriesFromConfig(config);
                EmoteRegistry.getInstance().refetchEmotes();

                if (client.player != null) {
                    client.player.sendMessage(Text.literal("Emotes reloaded!").setStyle(Style.EMPTY.withFormatting(Formatting.GRAY)), false);
                }
            }
        });
    }

    public static File getConfigFile() {
        return new File(FabricLoader.getInstance().getConfigDir().toFile(), "pegs-emotes.json");
    }
}
