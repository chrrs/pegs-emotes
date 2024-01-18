package me.chrr.pegsemotes;

import me.chrr.pegsemotes.emote.RepositoryManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class EmoteMod implements ClientModInitializer {
    public static final String USER_AGENT = "PegsEmotes (github.com/chrrs)";
    public static final Identifier EMOTE_FONT = new Identifier("pegs-emotes", "font/emotes");

    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return new Identifier("pegs-emotes", "emote-reloader");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        RepositoryManager.getInstance().reload();
                    }
                });
    }
}
