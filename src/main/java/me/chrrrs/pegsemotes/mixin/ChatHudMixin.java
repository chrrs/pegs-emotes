package me.chrrrs.pegsemotes.mixin;

import me.chrrrs.pegsemotes.Constants;
import me.chrrrs.pegsemotes.render.EmoteRenderHelper;
import me.chrrrs.pegsemotes.render.RenderEmote;
import me.chrrrs.pegsemotes.text.TextReaderVisitor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.gui.hud.ChatHud;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    private final Logger MOD_LOGGER = LogManager.getLogger("pegs-emotes.mixin.ChatHudMixin");

    @Shadow @Final
    private MinecraftClient client;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;FFI)I"))
    private int drawWithShadow(TextRenderer textRenderer, MatrixStack matrices, OrderedText text, float x, float y, int color) {
        TextReaderVisitor textReaderVisitor = new TextReaderVisitor();
        text.accept(textReaderVisitor);

        float emoteSize = textRenderer.getWidth(Constants.EMOTE_PLACEHOLDER);
        float emoteAlpha = (float) (color >> 24 & 255) / 255.0f;

        List<RenderEmote> renderEmoteList = EmoteRenderHelper.extractEmotes(textReaderVisitor, textRenderer, x, y);

        matrices.translate(0, -0.5f, 0);
        for(RenderEmote renderEmote : renderEmoteList) {
            EmoteRenderHelper.drawEmote(matrices, renderEmote, emoteSize, emoteAlpha, 1.05f, 1.5f);
        }
        matrices.translate(0, 0.5, 0);

        return textRenderer.draw(matrices, textReaderVisitor.getOrderedText(), x, y, color);
    }
}
