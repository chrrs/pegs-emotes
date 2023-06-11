package me.chrr.pegsemotes.emotes;

import me.chrr.pegsemotes.EmotesMod;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;

public class FetchedEmote extends Emote {
    private final int id;
    private final String name;
    private final Identifier textureIdentifier;

    private final int width;
    private final int height;

    private final int frameCount;
    private final int frameTime;

    public FetchedEmote(String name, Identifier textureIdentifier, int frameTime) {
        this.name = name;
        this.id = createId(name);

        this.textureIdentifier = textureIdentifier;
        this.frameTime = frameTime;

        NativeImage image = EmoteRegistry.getInstance().getEmoteImage(textureIdentifier);

        width = image.getWidth();

        if (frameTime > 0) {
            frameCount = image.getHeight() / width;

            //noinspection SuspiciousNameCombination
            height = width;
        } else {
            frameCount = 1;
            height = image.getHeight();
        }
    }

    public boolean isAnimated() {
        return frameTime > 0;
    }

    public int getFrameTime() {
        return frameTime;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public Identifier getTextureIdentifier() {
        return textureIdentifier;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSheetWidth() {
        return width;
    }

    public int getSheetHeight() {
        return height * frameCount;
    }

    public int getRenderedWidth() {
        return (int) ((double) width / ((double) height / (float) EmotesMod.EMOTE_HEIGHT));
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getReplacementText() {
        return " ".repeat((int) Math.ceil((double) getRenderedWidth() / 4.0f));
    }
}
