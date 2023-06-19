package me.chrr.pegsemotes.emote;

public class Emote {
    private final int id;
    private final String name;
    private final boolean animated;

    public Emote(String name, boolean animated) {
        this.name = name;
        this.animated = animated;
        this.id = name.hashCode();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAnimated() {
        return animated;
    }
}
