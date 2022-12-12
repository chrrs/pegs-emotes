package me.chrrrs.pegsemotes.emotes.remote;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class EmoteRepository {
    private final URL baseUrl;
    private final List<StaticEmote> staticEmotes;
    private final List<AnimatedEmote> animatedEmotes;

    public EmoteRepository(URL baseUrl) throws IOException {
        this.baseUrl = baseUrl;

        URL url = new URL(baseUrl, "emotes.json?_=" + System.currentTimeMillis());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            Emotes emotes = gson.fromJson(reader, Emotes.class);

            staticEmotes = emotes.static_;
            animatedEmotes = emotes.animated;
        }
    }

    public List<StaticEmote> getStaticEmotes() {
        return Objects.requireNonNullElseGet(staticEmotes, List::of);
    }

    public List<AnimatedEmote> getAnimatedEmotes() {
        return Objects.requireNonNullElseGet(animatedEmotes, List::of);
    }

    public URL getBaseUrl() {
        return baseUrl;
    }

    private static class Emotes {
        @SerializedName("static")
        List<StaticEmote> static_;
        List<AnimatedEmote> animated;
    }

    public static class StaticEmote {
        public String name;
        public String emoteUrl;
    }

    public static class AnimatedEmote {
        public String name;
        public String emoteUrl;
        public int frameTime;
    }
}
