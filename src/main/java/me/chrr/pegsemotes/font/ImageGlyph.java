package me.chrr.pegsemotes.font;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import me.chrr.pegsemotes.config.Config;
import net.minecraft.client.font.BakedGlyph;
import net.minecraft.client.font.Glyph;
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
    public BakedGlyph bake(Function<RenderableGlyph, BakedGlyph> glyphRendererGetter) {
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
    public void upload(int x, int y, GpuTexture texture) {
        RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, image, 0, x, y, image.getWidth(), image.getHeight(), 0, 0);
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
