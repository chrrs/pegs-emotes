package me.chrrrs.pegsemotes.emotes;

import me.chrrrs.pegsemotes.emotes.remote.EmoteRepository;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class EmoteRegistry {
    private final Logger LOGGER = LogManager.getLogger("pegs-emotes.emotes.EmoteRegistry");
    private static int NEXT_ID = 0;

    public List<String> repositoryLinks = new ArrayList<>();

    private static final EmoteRegistry instance = new EmoteRegistry();

    private final Set<Identifier> identifiers = new HashSet<>();

    private final List<EmoteRepository> repositories = new ArrayList<>();
    private final Map<Identifier, NativeImage> images = new HashMap<>();

    private final Map<Integer, Emote> emotesById = new HashMap<>();
    private final Map<String, Emote> emotesByName = new HashMap<>();

    private EmoteRegistry() {
    }

    public static EmoteRegistry getInstance() {
        return instance;
    }

    public Collection<Emote> getEmotes() {
        return emotesById.values();
    }

    public Emote getEmoteById(int id) {
        return emotesById.get(id);
    }

    public Emote getEmoteByName(String name) {
        return emotesByName.get(name);
    }

    public NativeImage getEmoteImage(Identifier textureIdentifier) {
        return images.get(textureIdentifier);
    }

    public void disposeEmotes() {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        for (Identifier identifier : identifiers) {
            textureManager.destroyTexture(identifier);
        }

        identifiers.clear();
        images.clear();
        emotesById.clear();
        emotesByName.clear();

        LOGGER.info("disposed all emotes!");
    }

    public void reload() {
        emotesById.clear();
        emotesByName.clear();
        repositories.clear();

        for (String url : repositoryLinks) {
            try {
                repositories.add(new EmoteRepository(new URL(url)));
            } catch (IOException e) {
                LOGGER.error("failed to fetch from emote repository at '" + url + "'", e);
            }
        }

        for (EmoteRepository repository : repositories) {
            for (EmoteRepository.StaticEmote staticEmote : repository.getStaticEmotes()) {
                try {
                    addEmote(new UnfetchedEmote(staticEmote.name, new URL(repository.getBaseUrl(), staticEmote.emoteUrl), 0));
                } catch (MalformedURLException e) {
                    LOGGER.error("failed to add static emote '" + staticEmote.name + "' from '" + repository.getBaseUrl() + "'", e);
                }
            }

            for (EmoteRepository.AnimatedEmote animatedEmote : repository.getAnimatedEmotes()) {
                try {
                    addEmote(new UnfetchedEmote(animatedEmote.name, new URL(repository.getBaseUrl(), animatedEmote.emoteUrl), animatedEmote.frameTime));
                } catch (MalformedURLException e) {
                    LOGGER.error("failed to add animated emote '" + animatedEmote.name + "' from '" + repository.getBaseUrl() + "'", e);
                }
            }
        }

        LOGGER.info("emotes: " + getEmotes().stream().map(Emote::getName).collect(Collectors.joining(", ")));
        LOGGER.info("emotes reloaded!");
    }

    public synchronized void upgradeEmote(int id) {
        Emote emote = getEmoteById(id);
        if (emote instanceof UnfetchedEmote unfetchedEmote && !unfetchedEmote.fetching) {
            unfetchedEmote.fetching = true;

            if (unfetchedEmote.textureIdentifier == null) {
                Identifier identifier = new Identifier("pegs-emotes", "emotes/" + (NEXT_ID++));
                if (!identifiers.contains(identifier)) {
                    new Thread(() -> {
                        try (InputStream stream = unfetchedEmote.getUrl().openStream()) {
                            NativeImage image = NativeImage.read(stream);
                            identifiers.add(identifier);
                            images.put(identifier, image);


                            unfetchedEmote.textureIdentifier = identifier;
                            unfetchedEmote.fetching = false;
                        } catch (IOException e) {
                            LOGGER.error("failed to fetch emote '" + emote.getName() + "'", e);
                        }
                    }, "Emote Fetcher").start();
                } else {
                    unfetchedEmote.textureRegistered = true;
                    unfetchedEmote.textureIdentifier = identifier;
                    unfetchedEmote.fetching = false;
                }
            } else {
                if (!unfetchedEmote.textureRegistered) {
                    TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                    textureManager.registerTexture(unfetchedEmote.textureIdentifier, new NativeImageBackedTexture(images.get(unfetchedEmote.textureIdentifier)));
                }

                try {
                    FetchedEmote fetchedEmote = new FetchedEmote(emote.getName(), unfetchedEmote.textureIdentifier, unfetchedEmote.getFrameTime());
                    addEmote(fetchedEmote);

                    LOGGER.debug("upgraded emote '" + emote.getName() + "'");
                } catch (Exception e) {
                    LOGGER.error("failed to initialize fetched emote '" + emote.getName() + "'", e);
                    identifiers.remove(unfetchedEmote.textureIdentifier);
                }
            }
        }
    }

    public void addEmote(Emote emote) {
        emotesById.put(emote.getId(), emote);
        emotesByName.put(emote.getName(), emote);
    }
}
