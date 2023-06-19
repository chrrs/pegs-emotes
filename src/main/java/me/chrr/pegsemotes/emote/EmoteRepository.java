package me.chrr.pegsemotes.emote;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.chrr.pegsemotes.emote.source.EmoteSource;
import me.chrr.pegsemotes.emote.source.RemoteEmote;
import me.chrr.pegsemotes.emote.source.RemoteEmoteSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public record EmoteRepository(URL baseUrl) {
    private static final Logger LOGGER = LogManager.getLogger("pegs-emotes.EmoteRepository");

    public List<Emote> fetchEmotes() throws IOException {
        String cacheBuster = "";
        if (baseUrl.getProtocol().startsWith("http")) {
            cacheBuster = "?_=" + System.currentTimeMillis();
        }

        List<Emote> emotes = new ArrayList<>();

        URL url = new URL(baseUrl, "emotes.json" + cacheBuster);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            JsonElement element = JsonParser.parseReader(reader);
            JsonObject object = element.getAsJsonObject();

            if (object.has("static")) {
                JsonArray array = object.getAsJsonArray("static");
                for (JsonElement emoteElement : array) {
                    JsonObject emoteObject = emoteElement.getAsJsonObject();
                    Emote emote = parseEmote(emoteObject, false);
                    if (emote != null) {
                        emotes.add(emote);
                    }
                }
            }

            if (object.has("animated")) {
                JsonArray array = object.getAsJsonArray("animated");
                for (JsonElement emoteElement : array) {
                    JsonObject emoteObject = emoteElement.getAsJsonObject();
                    Emote emote = parseEmote(emoteObject, true);
                    if (emote != null) {
                        emotes.add(emote);
                    }
                }
            }
        }

        return emotes;
    }

    public Emote parseEmote(JsonObject emote, boolean animated) {
        try {
            URL url = new URL(baseUrl, emote.get("emote_url").getAsString());

            RemoteEmote remoteEmote = animated ?
                    new RemoteEmote.Animated(url, emote.get("frame_time").getAsInt()) :
                    new RemoteEmote.Static(url);
            EmoteSource source = new RemoteEmoteSource(remoteEmote);
            
            return new Emote(emote.get("name").getAsString(), source);
        } catch (MalformedURLException e) {
            LOGGER.error("invalid URL for emote '" + emote.get("name") + "' in repository '" + baseUrl + "'");
            return null;
        } catch (NullPointerException e) {
            LOGGER.error("malformed JSON for emote '" + emote.get("name") + "' in repository '" + baseUrl + "'");
            return null;
        }
    }
}
