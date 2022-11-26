package me.chrrrs.pegsemotes.emotes;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Emote {
    private static int currEmoteId = 0;

    private final int id;

    private final String name;
    private final String filename;

    private final int width;
    private int height;

    private final int frameCount;
    private final int frameTimeMs;

    public Emote(String name, String filename) throws IOException {
        this(name, filename, 0);
    }

    public Emote(String name, String filename, int frameTimeMs) throws IOException {
        this.id = currEmoteId++;

        this.name = name;
        this.filename = filename;

        this.frameTimeMs = frameTimeMs;

        Resource resource = MinecraftClient.getInstance().getResourceManager().getResource(getTextureIdentifier()).get();
        BufferedImage bufferedImage = ImageIO.read(resource.getInputStream());

        if (bufferedImage == null) {
            throw new IOException("Failed to load image: " + filename);
        }

        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();

        if (isAnimated()) {
            frameCount = (int) (height / width);
            height = width;
        } else {
            frameCount = 1;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getFrameTimeMs() {
        return frameTimeMs;
    }

    public boolean isAnimated() {
        return frameTimeMs > 0;
    }

    public int getSheetWidth() {
        return width;
    }

    public int getSheetHeight() {
        return height * frameCount;
    }

    public Identifier getTextureIdentifier() {
        return new Identifier("pegs-emotes:emotes/" + filename);
    }
}
