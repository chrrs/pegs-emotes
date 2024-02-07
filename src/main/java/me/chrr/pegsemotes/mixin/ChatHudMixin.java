package me.chrr.pegsemotes.mixin;

import me.chrr.pegsemotes.EmoteMod;
import me.chrr.pegsemotes.emote.RepositoryManager;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Unique
    private static final String SPLIT_CHARS = " \t\n,.!?<>[]'\"|{}();=";

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Text injectEmotes(Text message) {
        MutableText out = MutableText.of(TextContent.EMPTY);
        message.visit((style, text) -> {
            int lastIndex = -1;
            int lastEmote = 0;
            for (int i = 0; i <= text.length(); i++) {
                if (i == text.length() || SPLIT_CHARS.indexOf(text.charAt(i)) != -1) {
                    String word = text.substring(lastIndex + 1, i);

                    Optional<Integer> emoteCodePoint = RepositoryManager.getInstance().tryGetCodePoint(word);
                    if (emoteCodePoint.isPresent()) {
                        String emote = String.valueOf((char) (int) emoteCodePoint.get());
                        HoverEvent hoverEvent = style.getHoverEvent() != null
                                ? style.getHoverEvent()
                                : new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(word));

                        Style emoteStyle = Style.EMPTY
                                .withFont(EmoteMod.EMOTE_FONT)
                                .withClickEvent(style.getClickEvent())
                                .withHoverEvent(hoverEvent);

                        out.append(Text.literal(text.substring(lastEmote, lastIndex + 1)).setStyle(style));
                        out.append(Text.literal(emote).setStyle(emoteStyle));
                        lastEmote = i;
                    }

                    lastIndex = i;
                }
            }

            if (lastEmote != text.length()) {
                out.append(Text.literal(text.substring(lastEmote)).setStyle(style));
            }

            return Optional.empty();
        }, Style.EMPTY);
        return out;
    }
}
