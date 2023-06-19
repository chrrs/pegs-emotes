package me.chrr.pegsemotes.emote.source;

import java.net.URL;

public abstract class RemoteEmote {
    public URL url;

    public static class Static extends RemoteEmote {
        public Static(URL url) {
            this.url = url;
        }
    }

    public static class Animated extends RemoteEmote {
        public int frameTime;

        public Animated(URL url, int frameTime) {
            this.url = url;
            this.frameTime = frameTime;
        }
    }
}
