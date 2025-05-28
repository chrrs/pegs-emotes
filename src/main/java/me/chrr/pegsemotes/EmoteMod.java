package me.chrr.pegsemotes;

import me.chrr.pegsemotes.config.Config;
import me.chrr.pegsemotes.emote.RepositoryManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

public class EmoteMod implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger("pegs-emotes");

    public static final String USER_AGENT = "PegsEmotes (github.com/chrrs)";
    public static final Identifier EMOTE_FONT = Identifier.of("pegs-emotes", "font/emotes");

    @Override
    public void onInitializeClient() {
        try {
            Config.loadInstance();
        } catch (IOException e) {
            LOGGER.error("failed to load config", e);
        }

        // Reload emotes when client resources get reloaded
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return Identifier.of("pegs-emotes", "emote-reloader");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        RepositoryManager.getInstance().reload();
                    }
                });

        // Reload emotes when key bind is pressed (F4 by default)
        KeyBinding reloadKeyBinding = KeyBindingHelper.registerKeyBinding(
                new KeyBinding("key.pegs-emotes.reload", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F4, "category.pegs-emotes"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (reloadKeyBinding.wasPressed()) {
                RepositoryManager.getInstance().reload();

                if (client.player != null) {
                    client.player.sendMessage(Text.translatable("text.pegs-emotes.reload-complete", RepositoryManager.getInstance().getEmoteNames().size())
                            .setStyle(Style.EMPTY.withFormatting(Formatting.GRAY)), false);
                }
            }
        });
    }
}
