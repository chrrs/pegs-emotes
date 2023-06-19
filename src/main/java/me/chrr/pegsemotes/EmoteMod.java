package me.chrr.pegsemotes;

import me.chrr.pegsemotes.config.Config;
import me.chrr.pegsemotes.config.ConfigManager;
import me.chrr.pegsemotes.emote.EmoteRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.File;

public class EmoteMod implements ClientModInitializer {
    private Config config;

    @Override
    public void onInitializeClient() {
        config = ConfigManager.read(getConfigFile());
        EmoteRegistry.getInstance().loadRepositoriesFromConfig(config);

        ClientPlayConnectionEvents.INIT.register((handler, client) ->
                EmoteRegistry.getInstance().refetchEmotes());

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return new Identifier("pegs-emotes", "emotes");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        EmoteRegistry.getInstance().loadRepositoriesFromConfig(config);
                        EmoteRegistry.getInstance().disposeCache();
                        EmoteRegistry.getInstance().refetchEmotes();
                    }
                }
        );
    }

    public static File getConfigFile() {
        return new File(FabricLoader.getInstance().getConfigDir().toFile(), "pegs-emotes.json");
    }
}
