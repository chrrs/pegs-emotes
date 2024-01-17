package me.chrr.pegsemotes.emote;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import me.chrr.pegsemotes.EmoteMod;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ApiEmotes {
    private static final Gson GSON = new Gson();

    public String lastUpdate;
    public ApiEmote[] emotes;

    public static class ApiEmote {
        public String name;
        public String format;
        public String url;
        public String sha1;
    }

    public static ApiEmotes fetchFrom(URL endpoint) throws IOException, JsonParseException {
        URLConnection connection = endpoint.openConnection();
        connection.setRequestProperty("User-Agent", EmoteMod.USER_AGENT);
        return GSON.fromJson(new InputStreamReader(connection.getInputStream()), ApiEmotes.class);
    }
}
