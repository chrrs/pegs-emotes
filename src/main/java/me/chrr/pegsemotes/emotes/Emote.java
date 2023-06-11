package me.chrr.pegsemotes.emotes;

import java.util.HashMap;
import java.util.Map;

public abstract class Emote {
    private static final Map<String, Integer> ID_MAP = new HashMap<>();
    private static int NEXT_EMOTE_ID = 1;

    public abstract int getId();

    public abstract String getName();

    public abstract String getReplacementText();

    protected static int createId(String name) {
        if (ID_MAP.containsKey(name)) {
            return ID_MAP.get(name);
        } else {
            int id = NEXT_EMOTE_ID++;
            ID_MAP.put(name, id);
            return id;
        }
    }
}
