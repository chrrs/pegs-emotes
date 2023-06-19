package me.chrr.pegsemotes.emote.source;

import net.minecraft.client.util.math.MatrixStack;

public interface EmoteSource {
    boolean isAnimated();
    int getRenderedWidth();
    void draw(MatrixStack matrices, float x, float y, float alpha);
}
