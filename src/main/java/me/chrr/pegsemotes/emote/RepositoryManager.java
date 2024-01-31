package me.chrr.pegsemotes.emote;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RepositoryManager {
    private static final Logger LOGGER = LogManager.getLogger("pegs-emotes.RepositoryManager");

    private static final RepositoryManager INSTANCE = new RepositoryManager();

    private String[] repositories = new String[]{};

    private final Map<Integer, Future<Emote>> emotes = new HashMap<>();
    private final Map<String, Integer> codePoints = new HashMap<>();
    private final Map<String, EmoteFetcher.RemoteEmote> remoteEmotes = new HashMap<>();

    private final EmoteFetcher emoteFetcher = new EmoteFetcher();
    private int nextCodePoint = 0;

    private RepositoryManager() {
    }

    public void reload() {
        remoteEmotes.clear();
        codePoints.clear();

        for (String repository : repositories) {
            try {
                URL base = new URL(repository);
                ApiEmotes res = ApiEmotes.fetchFrom(new URL(base, "emotes.json"));

                for (ApiEmotes.ApiEmote emote : res.emotes) {
                    remoteEmotes.put(emote.name, new EmoteFetcher.RemoteEmote(base, emote));
                }
            } catch (Exception e) {
                LOGGER.error("failed to load repository at " + repository, e);
            }
        }

        LOGGER.info("emotes fetched: " + String.join(" ", remoteEmotes.keySet()));
    }

    public Optional<Integer> tryGetCodePoint(String name) {
        Integer codePoint = codePoints.get(name);
        if (codePoint != null) {
            return Optional.of(codePoint);
        }

        EmoteFetcher.RemoteEmote remoteEmote = remoteEmotes.get(name);
        if (remoteEmote != null) {
            codePoint = nextCodePoint++;
            codePoints.put(name, codePoint);

            emotes.put(codePoint, emoteFetcher.fetchEmote(remoteEmote));

            return Optional.of(codePoint);
        }

        return Optional.empty();
    }

    public Optional<Emote> getEmote(int codePoint) {
        Future<Emote> emote = emotes.get(codePoint);

        if (emote.isDone()) {
            try {
                return Optional.of(emote.get());
            } catch (InterruptedException | ExecutionException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public Collection<String> getEmoteNames() {
        return remoteEmotes.keySet();
    }

    public void setRepositories(String[] repositories) {
        this.repositories = repositories;
    }

    public static RepositoryManager getInstance() {
        return INSTANCE;
    }
}
