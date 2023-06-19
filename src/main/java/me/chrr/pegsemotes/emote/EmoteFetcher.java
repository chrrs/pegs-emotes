package me.chrr.pegsemotes.emote;

import me.chrr.pegsemotes.emote.source.LocalAnimatedEmoteSource;
import me.chrr.pegsemotes.emote.source.LocalStaticEmoteSource;
import me.chrr.pegsemotes.emote.source.RemoteEmote;
import me.chrr.pegsemotes.emote.source.RemoteEmoteSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EmoteFetcher {
    private static final Logger LOGGER = LogManager.getLogger("pegs-emotes.EmoteFetcher");
    private static int ITERATION = 0;

    private final Set<Identifier> identifiers = new HashSet<>();
    private final Map<Identifier, NativeImage> images = new HashMap<>();

    /**
     * Ideally, this should be called from the render thread.
     */
    public void clearCache() {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        for (Identifier identifier : identifiers) {
            textureManager.destroyTexture(identifier);
        }

        identifiers.clear();
        ITERATION += 1;
    }

    /**
     * This should only be called from the render thread!
     */
    public void tryFetch(Emote emote) {
        if (emote.getEmoteSource() instanceof RemoteEmoteSource remoteSource) {
            if (remoteSource.state == RemoteEmoteSource.State.UNFETCHED) {
                Identifier identifier = new Identifier("pegs-emotes", "emotes/" + emote.getId() + "/" + ITERATION);
                if (identifiers.contains(identifier)) {
                    remoteSource.identifier = identifier;
                    upgradeEmote(emote, remoteSource);
                    return;
                }

                remoteSource.state = RemoteEmoteSource.State.FETCHING;
                new Thread(() -> fetchEmoteImage(emote, identifier, remoteSource), "Emote fetcher").start();
            } else if (remoteSource.state == RemoteEmoteSource.State.FETCHED) {
                TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                textureManager.registerTexture(remoteSource.identifier, new NativeImageBackedTexture(images.get(remoteSource.identifier)));

                upgradeEmote(emote, remoteSource);
                LOGGER.debug("upgraded emote '" + emote.getName() + "'");
            }
        }
    }

    private void fetchEmoteImage(Emote emote, Identifier identifier, RemoteEmoteSource remoteSource) {
        try (InputStream stream = remoteSource.remoteEmote.url.openStream()) {
            NativeImage image = NativeImage.read(stream);
            identifiers.add(identifier);
            images.put(identifier, image);

            remoteSource.identifier = identifier;
            remoteSource.state = RemoteEmoteSource.State.FETCHED;

            LOGGER.debug("fetched image for emote '" + emote.getName() + "'");
        } catch (IOException e) {
            LOGGER.error("failed to fetch emote '" + emote.getName() + "'", e);
            remoteSource.state = RemoteEmoteSource.State.ERRORED;
        }
    }

    private void upgradeEmote(Emote emote, RemoteEmoteSource remoteSource) {
        if (remoteSource.remoteEmote instanceof RemoteEmote.Static) {
            emote.setEmoteSource(new LocalStaticEmoteSource(remoteSource.identifier, images.get(remoteSource.identifier)));
        } else if (remoteSource.remoteEmote instanceof RemoteEmote.Animated animatedEmote) {
            emote.setEmoteSource(new LocalAnimatedEmoteSource(remoteSource.identifier, images.get(remoteSource.identifier), animatedEmote.frameTime));
        } else {
            LOGGER.error("unknown emote source for emote '" + emote.getName() + "'");
            remoteSource.state = RemoteEmoteSource.State.ERRORED;
        }
    }
}
