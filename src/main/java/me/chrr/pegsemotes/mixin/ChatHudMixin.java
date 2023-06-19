package me.chrr.pegsemotes.mixin;

import me.chrr.pegsemotes.emote.EmoteRegistry;
import me.chrr.pegsemotes.util.TextEmoteReplacer;
import me.chrr.pegsemotes.util.TextReaderVisitor;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;FFI)I"))
    private int drawWithShadow(TextRenderer textRenderer, MatrixStack matrices, OrderedText text, float x, float y, int color) {
        TextReaderVisitor textReaderVisitor = new TextReaderVisitor();
        text.accept(textReaderVisitor);

        float emoteAlpha = (float) (color >> 24 & 0xff) / 255.0f;

        List<TextEmoteReplacer.PositionedEmote> emotes = TextEmoteReplacer.replaceEmotes(textReaderVisitor, textRenderer, x, y);

        matrices.translate(0, -0.5f, 0);
        for (TextEmoteReplacer.PositionedEmote positionedEmote : emotes) {
            EmoteRegistry.getInstance().ensureEmote(positionedEmote.emote());
            positionedEmote.emote().getEmoteSource().draw(matrices, positionedEmote.x(), positionedEmote.y(), emoteAlpha);
        }
        matrices.translate(0, 0.5, 0);

        return textRenderer.draw(matrices, textReaderVisitor.getOrderedText(), x, y, color);
    }
}
