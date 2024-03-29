package me.chrr.pegsemotes.font;

import me.chrr.pegsemotes.config.Config;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.font.RenderableGlyph;
import net.minecraft.client.texture.NativeImage;

import java.util.function.Function;

public class ImageGlyph implements Glyph, RenderableGlyph {
    private final NativeImage image;

    public ImageGlyph(NativeImage image) {
        this.image = image;
    }

    @Override
    public float getAdvance() {
        return (float) getWidth() / ((float) getHeight() / 7f) + 1f;
    }

    @Override
    public GlyphRenderer bake(Function<RenderableGlyph, GlyphRenderer> glyphRendererGetter) {
        return glyphRendererGetter.apply(this);
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public void upload(int x, int y) {
        image.upload(0, x, y, 0, 0, image.getWidth(), image.getHeight(), true, true, true, false);
    }

    @Override
    public boolean hasColor() {
        return true;
    }

    @Override
    public float getOversample() {
        return (float) getHeight() / 7f;
    }

    @Override
    public float getBoldOffset() {
        return 0f;
    }

    @Override
    public float getShadowOffset() {
        return Config.getInstance().emoteShadow ? 1f : 0f;
    }
}
