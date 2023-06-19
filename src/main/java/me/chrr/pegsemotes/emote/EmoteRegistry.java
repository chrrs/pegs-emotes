package me.chrr.pegsemotes.emote;

import me.chrr.pegsemotes.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class EmoteRegistry {
    private static final Logger LOGGER = LogManager.getLogger("pegs-emotes.EmoteRegistry");
    private static final EmoteRegistry INSTANCE = new EmoteRegistry();

    private final EmoteFetcher emoteFetcher = new EmoteFetcher();
    private List<EmoteRepository> repositories = List.of();

    private Map<String, Emote> emotesByName = Map.of();
    private Map<Integer, Emote> emotesById = Map.of();

    public static EmoteRegistry getInstance() {
        return INSTANCE;
    }

    public Emote getEmoteByName(String name) {
        return emotesByName.get(name);
    }

    public Emote getEmoteById(int id) {
        return emotesById.get(id);
    }

    public Collection<Emote> getEmotes() {
        return emotesById.values();
    }

    public void loadRepositoriesFromConfig(Config config) {
        repositories = new ArrayList<>();

        for (String url : config.repositories) {
            try {
                repositories.add(new EmoteRepository(new URL(url)));
            } catch (MalformedURLException e) {
                LOGGER.error("failed to initialize repository at '" + url + "'", e);
            }
        }
    }

    /**
     * Ideally, this should be called from the render thread.
     */
    public void clearCache() {
        emoteFetcher.clearCache();
    }

    public void refetchEmotes() {
        emotesByName = new HashMap<>();
        emotesById = new HashMap<>();

        for (EmoteRepository repository : repositories) {
            try {
                for (Emote emote : repository.fetchEmotes()) {
                    emotesByName.putIfAbsent(emote.getName(), emote);
                    emotesById.putIfAbsent(emote.getId(), emote);
                }
            } catch (IOException e) {
                LOGGER.error("failed to fetch emotes from '" + repository.baseUrl() + "'", e);
            }
        }

        LOGGER.info("emotes: " + getEmotes().stream().map(Emote::getName).collect(Collectors.joining(", ")));
        LOGGER.info("refetched all emotes");
    }

    /**
     * This should only be called from the render thread!
     */
    public void ensureEmote(Emote emote) {
        emoteFetcher.tryFetch(emote);
    }
}
