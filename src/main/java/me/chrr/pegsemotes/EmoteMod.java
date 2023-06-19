package me.chrr.pegsemotes;

import me.chrr.pegsemotes.config.Config;
import me.chrr.pegsemotes.config.ConfigManager;
import me.chrr.pegsemotes.emote.EmoteRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class EmoteMod implements ClientModInitializer {
    private Config config;

    @Override
    public void onInitializeClient() {
        config = ConfigManager.read(getConfigFile());
        EmoteRegistry.getInstance().loadRepositoriesFromConfig(config);
        EmoteRegistry.getInstance().refetchEmotes();
    }

    public static File getConfigFile() {
        return new File(FabricLoader.getInstance().getConfigDir().toFile(), "pegs-emotes.json");
    }
}
