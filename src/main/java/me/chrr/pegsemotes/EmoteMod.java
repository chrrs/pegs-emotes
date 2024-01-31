package me.chrr.pegsemotes;

import me.chrr.pegsemotes.emote.RepositoryManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class EmoteMod implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger("pegs-emotes");

    public static final String USER_AGENT = "PegsEmotes (github.com/chrrs)";
    public static final Identifier EMOTE_FONT = new Identifier("pegs-emotes", "font/emotes");

    private Config config = new Config();

    @Override
    public void onInitializeClient() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("pegs-emotes.json");

        try {
            config = Config.load(configPath);
        } catch (IOException e) {
            LOGGER.error("failed to load config", e);
        }

        RepositoryManager.getInstance().setRepositories(config.repositories);

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
