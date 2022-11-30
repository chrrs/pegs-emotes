package me.chrrrs.pegsemotes.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.chrrrs.pegsemotes.EmotesMod;
import me.chrrrs.pegsemotes.emotes.Emote;
import me.chrrrs.pegsemotes.emotes.EmoteRegistry;
import me.chrrrs.pegsemotes.text.TextReaderVisitor;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmoteRenderHelper {
    public static List<RenderEmote> extractEmotes(TextReaderVisitor textReaderVisitor, TextRenderer textRenderer, float renderX, float renderY) {
        List<RenderEmote> renderEmoteList = new ArrayList<>();

        String message = textReaderVisitor.getString();

        Pattern emotePattern = Pattern.compile("\u00a8(\\d+)");
        Matcher matcher = emotePattern.matcher(message);

        while (matcher.find()) {
            try {
                int id = Integer.parseInt(matcher.group(1));
                Emote emote = EmoteRegistry.getInstance().getEmoteById(id);

                int startPos = matcher.start(0);
                int endPos = matcher.end(0);

                if (emote != null) {
                    float beforeTextWidth = textRenderer.getWidth(message.substring(0, startPos));

                    renderEmoteList.add(new RenderEmote(emote, renderX + beforeTextWidth, renderY));
                    textReaderVisitor.replaceBetween(startPos, endPos, emote.getReplacement(), Style.EMPTY);

                    break;
                }
            } catch (NumberFormatException ignored) {}
        }

        return renderEmoteList;
    }

    public static void drawEmote(MatrixStack matrices, RenderEmote renderEmote, float width, float alpha) {
        Emote emote = renderEmote.getEmote();

        RenderSystem.setShaderTexture(0, emote.getTextureIdentifier());
        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);

        int frameNumber = 1;

        if (emote.isAnimated()) {
            frameNumber = (int) ((System.currentTimeMillis() / emote.getFrameTimeMs()) % (long) emote.getFrameCount());
        }

        DrawableHelper.drawTexture(
                matrices,
                (int) renderEmote.getX(),
                (int) renderEmote.getY(),
                (int) width,
                EmotesMod.EMOTE_HEIGHT,
                0,
                emote.getHeight() * frameNumber,
                emote.getWidth(),
                emote.getHeight(),
                emote.getSheetWidth(),
                emote.getSheetHeight()
        );
    }
}
