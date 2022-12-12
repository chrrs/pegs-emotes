package me.chrrrs.pegsemotes.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.chrrrs.pegsemotes.EmotesMod;
import me.chrrrs.pegsemotes.emotes.Emote;
import me.chrrrs.pegsemotes.emotes.EmoteRegistry;
import me.chrrrs.pegsemotes.emotes.FetchedEmote;
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
    public static List<PositionedEmote> extractEmotes(TextReaderVisitor textReaderVisitor, TextRenderer textRenderer, float renderX, float renderY) {
        List<PositionedEmote> positionedEmotes = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\u00a8(\\d+)");

        outer:
        while (true) {
            String message = textReaderVisitor.getString();
            Matcher matcher = pattern.matcher(message);

            while (true) {
                // TODO: This is kinda dirty.
                if (!matcher.find()) {
                    break outer;
                }

                try {
                    int id = Integer.parseInt(matcher.group(1));
                    Emote emote = EmoteRegistry.getInstance().getEmoteById(id);

                    int startPos = matcher.start(0);
                    int endPos = matcher.end(0);

                    if (emote != null) {
                        float beforeTextWidth = textRenderer.getWidth(message.substring(0, startPos));

                        if (emote instanceof FetchedEmote fetchedEmote) {
                            positionedEmotes.add(new PositionedEmote(fetchedEmote, renderX + beforeTextWidth, renderY));
                        } else {
                            EmoteRegistry.getInstance().upgradeEmote(emote.getId());
                        }

                        textReaderVisitor.replaceBetween(startPos, endPos, emote.getReplacementText(), Style.EMPTY);

                        break;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return positionedEmotes;
    }

    public static void drawEmote(MatrixStack matrices, PositionedEmote positionedEmote, float alpha) {
        FetchedEmote emote = positionedEmote.emote();

        RenderSystem.setShaderTexture(0, emote.getTextureIdentifier());
        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);

        int frameNumber = 1;

        if (emote.isAnimated()) {
            frameNumber = (int) ((System.currentTimeMillis() / emote.getFrameTime()) % (long) emote.getFrameCount());
        }

        DrawableHelper.drawTexture(
                matrices,
                (int) positionedEmote.x(),
                (int) positionedEmote.y(),
                emote.getRenderedWidth(),
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
