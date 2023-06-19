package me.chrr.pegsemotes.emote;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public record EmoteRepository(URL baseUrl) {
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
                    Emote emote = new Emote(emoteObject.get("name").getAsString(), false);
                    emotes.add(emote);
                }
            }

            if (object.has("animated")) {
                JsonArray array = object.getAsJsonArray("animated");
                for (JsonElement emoteElement : array) {
                    JsonObject emoteObject = emoteElement.getAsJsonObject();
                    Emote emote = new Emote(emoteObject.get("name").getAsString(), false);
                    emotes.add(emote);
                }
            }
        }

        return emotes;
    }
}
