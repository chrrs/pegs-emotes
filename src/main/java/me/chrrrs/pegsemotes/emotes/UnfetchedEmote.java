package me.chrrrs.pegsemotes.emotes;

import net.minecraft.util.Identifier;

import java.net.URL;

public class UnfetchedEmote extends Emote {
    private final int id;
    private final String name;
    private final URL url;
    private final int frameTime;

    public boolean fetching = false;
    public Identifier textureIdentifier = null;
    public boolean textureRegistered = false;

    public UnfetchedEmote(String name, URL url, int frameTime) {
        this.name = name;
        this.url = url;
        this.frameTime = frameTime;
        this.id = createId(name);
    }

    public URL getUrl() {
        return url;
    }

    public int getFrameTime() {
        return frameTime;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getReplacementText() {
        return "  ";
    }
}
