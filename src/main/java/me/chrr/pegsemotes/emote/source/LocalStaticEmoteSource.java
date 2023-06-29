package me.chrr.pegsemotes.emote.source;

import com.mojang.blaze3d.systems.RenderSystem;
import me.chrr.pegsemotes.EmoteMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;

public class LocalStaticEmoteSource implements EmoteSource {
    private final Identifier identifier;

    private final int width;
    private final int height;

    public LocalStaticEmoteSource(Identifier identifier, NativeImage image) {
        this.identifier = identifier;

        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public int getRenderedWidth() {
        return (int) ((double) width / ((double) height / (float) EmoteMod.EMOTE_HEIGHT));
    }

    @Override
    public void draw(DrawContext context, float x, float y, float alpha) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);

        context.drawTexture(
                identifier,
                (int) x,
                (int) y,
                getRenderedWidth(),
                EmoteMod.EMOTE_HEIGHT,
                0,
                0,
                width,
                height,
                width,
                height
        );

        RenderSystem.disableBlend();
    }
}
