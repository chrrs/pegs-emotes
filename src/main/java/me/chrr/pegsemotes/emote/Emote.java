package me.chrr.pegsemotes.emote;

import me.chrr.pegsemotes.emote.source.EmoteSource;

public class Emote {
    private final int id;
    private final String name;

    private EmoteSource emoteSource;

    public Emote(String name, EmoteSource emoteSource) {
        this.name = name;
        this.id = name.hashCode();
        this.emoteSource = emoteSource;
    }

    public EmoteSource getEmoteSource() {
        return emoteSource;
    }

    public void setEmoteSource(EmoteSource emoteSource) {
        this.emoteSource = emoteSource;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
