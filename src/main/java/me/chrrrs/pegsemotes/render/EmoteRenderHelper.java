package me.chrrrs.pegsemotes.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.chrrrs.pegsemotes.emotes.Emote;
import me.chrrrs.pegsemotes.emotes.EmoteRegistry;
import me.chrrrs.pegsemotes.text.TextReaderVisitor;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import me.chrrrs.pegsemotes.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmoteRenderHelper {
    @SuppressWarnings("AssignmentUsedAsCondition")
    public static List<RenderEmote> extractEmotes(TextReaderVisitor textReaderVisitor, TextRenderer textRenderer, float renderX, float renderY) {
        List<RenderEmote> renderEmoteList = new ArrayList<>();

        final Pattern emotePattern = Pattern.compile(Constants.EMOTE_PATTERN_ID_STR);

        boolean emotesLeft = true;

        while(emotesLeft) {
            String textStr = textReaderVisitor.getString();
            final Matcher emoteMatch = emotePattern.matcher(textStr);

            while (emotesLeft = emoteMatch.find()) {
                String emoteIdStr = emoteMatch.group(2);

                try {
                    int emoteId = Integer.parseInt(emoteIdStr);
                    Emote emote = EmoteRegistry.getInstance().getEmoteById(emoteId);

                    int startPos = emoteMatch.start(1);
                    int endPos = emoteMatch.end(1);

                    if (emote != null) {
                        float beforeTextWidth = textRenderer.getWidth(textStr.substring(0, startPos));

                        renderEmoteList.add(new RenderEmote(emote, renderX + beforeTextWidth, renderY));
                        textReaderVisitor.replaceBetween(startPos, endPos, Constants.EMOTE_PLACEHOLDER, Style.EMPTY);

                        break;
                    }
                } catch(NumberFormatException e) {
                    // Invalid emote ID - continue as normal
                }
            }
        }

        return renderEmoteList;
    }

    public static void drawEmote(MatrixStack matrices, RenderEmote renderEmote, float size, float alpha, float sizeMult, float maxWidthMult) {
        Emote emote = renderEmote.getEmote();

        float scaleX = ((float) emote.getWidth() / (float) emote.getHeight()) * sizeMult;
        float scaleY = sizeMult;

        if (scaleX > maxWidthMult) {
            scaleX = maxWidthMult;
            scaleY = maxWidthMult * ((float) emote.getHeight() / (float) emote.getWidth());
        }

        scaleX = Math.round(size * scaleX) / size;
        scaleY = Math.round(size * scaleY) / size;

        int x = (int) (renderEmote.getX() + (size * (1f - scaleX)) / 2f);
        int y = (int) (renderEmote.getY() + (size * (1f - scaleY)) / 2f);

        RenderSystem.setShaderTexture(0, emote.getTextureIdentifier());
        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);

        int frameNumber = 1;

        if (emote.isAnimated()) {
            frameNumber = (int) ((System.currentTimeMillis() / emote.getFrameTimeMs()) % (long) emote.getFrameCount());
        }

        DrawableHelper.drawTexture(
                matrices,
                x,
                y,
                Math.round(size * scaleX),
                Math.round(size * scaleY),
                0,
                emote.getHeight() * frameNumber,
                emote.getWidth(),
                emote.getHeight(),
                emote.getSheetWidth(),
                emote.getSheetHeight()
        );
    }
}
