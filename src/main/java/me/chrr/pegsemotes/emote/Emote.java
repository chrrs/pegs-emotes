package me.chrr.pegsemotes.emote;

import me.chrr.pegsemotes.font.ImageGlyph;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.texture.NativeImage;

import java.time.Instant;

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
}
