package me.chrr.pegsemotes;

import me.chrr.pegsemotes.emotes.EmoteRegistry;
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
import org.lwjgl.glfw.GLFW;

public class EmotesMod implements ClientModInitializer {
    public static final int EMOTE_HEIGHT = 8;

    private static KeyBinding reloadKeyBinding;

    @Override
    public void onInitializeClient() {
        EmoteRegistry.getInstance().repositoryLinks = Config.loadConfigOrCreateDefault().repositories;

        reloadKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.pegs-emotes.reload", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F4, "category.pegs-emotes"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (reloadKeyBinding.wasPressed()) {
                EmoteRegistry.getInstance().reload();
                client.player.sendMessage(Text.literal("Emotes reloaded!").setStyle(Style.EMPTY.withFormatting(Formatting.GRAY)), false);
            }
        });

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return new Identifier("pegs-emotes", "emotes");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        EmoteRegistry.getInstance().disposeEmotes();
                        EmoteRegistry.getInstance().reload();
                    }
                }
        );
    }
}
