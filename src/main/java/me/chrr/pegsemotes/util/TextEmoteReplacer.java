package me.chrr.pegsemotes.util;

import me.chrr.pegsemotes.EmoteMod;
import me.chrr.pegsemotes.emote.Emote;
import me.chrr.pegsemotes.emote.EmoteRegistry;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Style;

import java.util.ArrayList;
import java.util.List;

public class TextEmoteReplacer {
    public static List<PositionedEmote> replaceEmotes(TextReaderVisitor visitor, TextRenderer textRenderer, float renderX, float renderY) {
        List<PositionedEmote> positionedEmotes = new ArrayList<>();

        String message = visitor.getString();
        int offset = 0;
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == EmoteMod.EMOTE_PREFIX) {
                String id = "";

                int n = 1;
                while (i + 1 < message.length()) {
                    char next = message.charAt(i + 1);
                    if (Character.isDigit(next) || (n == 1 && next == '-')) {
                        id += next;

                        i++;
                        n++;
                    } else {
                        break;
                    }
                }

                Emote emote = EmoteRegistry.getInstance().getEmoteById(Integer.parseInt(id));

                if (emote != null) {
                    int start = i - n + 1 - offset;

                    float x = textRenderer.getWidth(visitor.getString().substring(0, start));

                    positionedEmotes.add(new PositionedEmote(emote, renderX + x, renderY));

                    String replacement = getReplacementForWidth(emote.getEmoteSource().getRenderedWidth());
                    visitor.replaceBetween(start, start + n, replacement, Style.EMPTY);

                    offset += n - replacement.length();
                }
            }
        }

        return positionedEmotes;
    }

    private static String getReplacementForWidth(int width) {
        return " ".repeat((int) Math.ceil((double) width / 4.0f));
    }

    public record PositionedEmote(Emote emote, float x, float y) {
    }
}
