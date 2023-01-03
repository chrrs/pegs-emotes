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

public class EmoteRenderHelper {
    public static List<PositionedEmote> extractEmotes(TextReaderVisitor textReaderVisitor, TextRenderer textRenderer, float renderX, float renderY) {
        List<PositionedEmote> positionedEmotes = new ArrayList<>();

        String message = textReaderVisitor.getString();
        int offset = 0;
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == '\ua950') {
                int id = 0;
                int n = 1;
                while (i + 1 < message.length()) {
                    char next = message.charAt(i + 1);
                    if (Character.isDigit(next)) {
                        id = id * 10 + (next - '0');

                        i++;
                        n++;
                    } else {
                        break;
                    }
                }

                Emote emote = EmoteRegistry.getInstance().getEmoteById(id);

                if (emote != null) {
                    int start = i - n + 1 - offset;

                    float x = textRenderer.getWidth(textReaderVisitor.getString().substring(0, start));

                    if (emote instanceof FetchedEmote fetchedEmote) {
                        positionedEmotes.add(new PositionedEmote(fetchedEmote, renderX + x, renderY));
                    } else {
                        EmoteRegistry.getInstance().upgradeEmote(emote.getId());
                    }

                    String replacement = emote.getReplacementText();
                    textReaderVisitor.replaceBetween(start, start + n, replacement, Style.EMPTY);

                    offset += n - replacement.length();
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
