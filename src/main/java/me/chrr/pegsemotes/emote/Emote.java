package me.chrr.pegsemotes.emote;

import me.chrr.pegsemotes.font.ImageGlyph;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.texture.NativeImage;

import java.time.Instant;
import java.util.Arrays;

public abstract class Emote {
    public abstract Glyph getFrame(Instant instant);

    public static class Static extends Emote {
        private final ImageGlyph glyph;

        public Static(NativeImage image) {
            this.glyph = new ImageGlyph(image);
        }

        @Override
        public ImageGlyph getFrame(Instant instant) {
            return glyph;
        }
    }

    public static class Animated extends Emote {
        private final Frame[] frames;
        private final int loopTimeMs;

        public Animated(Frame[] frames) {
            this.frames = frames;
            this.loopTimeMs = Math.max(1, Arrays.stream(frames).mapToInt(frame -> frame.delayMs).sum());
        }

        @Override
        public Glyph getFrame(Instant instant) {
            int localTime = (int) (instant.toEpochMilli() % loopTimeMs);

            int cumulativeTime = 0;
            for (Frame frame : frames) {
                cumulativeTime += frame.delayMs;
                if (cumulativeTime > localTime) {
                    return frame.glyph;
                }
            }

            return frames[0].glyph;
        }

        public static class Frame {
            private final ImageGlyph glyph;
            private final int delayMs;

            public Frame(NativeImage image, int delayMs) {
                this.glyph = new ImageGlyph(image);
                this.delayMs = delayMs;
            }
        }
    }
}
