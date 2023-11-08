package me.chrr.pegsemotes.emote.source;

import com.mojang.blaze3d.systems.RenderSystem;
import me.chrr.pegsemotes.EmoteMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;

public class LocalAnimatedEmoteSource implements EmoteSource {
    private final Identifier identifier;

    private final int width;
    private final int height;

    private final int frameCount;
    private final int frameTime;

    public LocalAnimatedEmoteSource(Identifier identifier, NativeImage image, int frameTime) {
        this.identifier = identifier;
        this.frameTime = frameTime;

        this.width = image.getWidth();
        // We assume square emotes.
        this.height = image.getWidth();

        this.frameCount = image.getHeight() / height;
    }

    @Override
    public int getRenderedWidth() {
        return (int) ((double) width / ((double) height / (float) EmoteMod.EMOTE_HEIGHT));
    }

    @Override
    public void draw(DrawContext context, float x, float y, float alpha) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);

        int frameNumber = (int) ((System.currentTimeMillis() / frameTime) % (long) frameCount);

        context.drawTexture(
                identifier,
                (int) x,
                (int) y,
                getRenderedWidth(),
                EmoteMod.EMOTE_HEIGHT,
                0,
                height * frameNumber,
                width,
                height,
                width,
                height * frameCount
        );

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }
}
