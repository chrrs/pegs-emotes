package me.chrr.pegsemotes.util;

import net.minecraft.client.texture.NativeImage;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class ImageUtil {
    private ImageUtil() {
    }

    public static NativeImage fromBufferedImage(BufferedImage image) {
        NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, image.getWidth(), image.getHeight(), false);
        ColorModel colorModel = image.getColorModel();

        // FIXME: I feel like there is a better way to do this..
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                nativeImage.setColor(x, y, colorModel.getRed(rgb)
                        | (colorModel.getGreen(rgb) << 8)
                        | (colorModel.getBlue(rgb) << 16)
                        | (colorModel.getAlpha(rgb) << 24));
            }
        }

        return nativeImage;
    }
}
