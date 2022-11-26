package me.chrrrs.pegsemotes.render;

import me.chrrrs.pegsemotes.emotes.Emote;

public class RenderEmote {
    private Emote emote;
    private float x;
    private float y;

    public RenderEmote(Emote emote, float x, float y) {
        this.emote = emote;
        this.x = x;
        this.y = y;
    }

    public Emote getEmote() {
        return emote;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
