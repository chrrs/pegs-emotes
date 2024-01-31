package me.chrr.pegsemotes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.chrr.pegsemotes.font.ImageGlyph;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TextRenderer.Drawer.class)
public abstract class TextRendererDrawerMixin {
    @Redirect(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawGlyph(Lnet/minecraft/client/font/GlyphRenderer;ZZFFFLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumer;FFFFI)V"))
    public void drawGlyph(TextRenderer textRenderer, GlyphRenderer glyphRenderer, boolean bold, boolean italic, float weight, float x, float y, Matrix4f matrix, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int light, @Local Glyph glyph) {
        if (glyph instanceof ImageGlyph imageGlyph) {
            float scale = (float) imageGlyph.getHeight() / 8f;
            matrix.scale(1f / scale);
            textRenderer.drawGlyph(glyphRenderer, bold, italic, weight, x * scale, y * scale, matrix, vertexConsumer, red, green, blue, alpha, light);
            matrix.scale(scale);
        } else {
            textRenderer.drawGlyph(glyphRenderer, bold, italic, weight, x, y, matrix, vertexConsumer, red, green, blue, alpha, light);
        }
    }
}
