package me.chrrrs.pegsemotes.mixin;

import me.chrrrs.pegsemotes.emotes.Emote;
import me.chrrrs.pegsemotes.emotes.EmoteRegistry;
import net.minecraft.client.util.ChatMessages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatMessages.class)
public class ChatMessagesMixin {
    private static final String SPLIT_CHARS = " \t\n,.!?<>[]'\"|{}();=";

    @ModifyVariable(method = "getRenderedChatMessage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static String getRenderedChatMessageParam(String message) {
        StringBuilder out = new StringBuilder();

        StringBuilder word = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (SPLIT_CHARS.indexOf(message.charAt(i)) != -1) {
                if (!word.isEmpty()) {
                    Emote emote = EmoteRegistry.getInstance().getEmoteByName(word.toString());
                    if (emote != null) {
                        out.append("\ua950").append(emote.getId());
                    } else {
                        out.append(word);
                    }
                }

                word = new StringBuilder();
                out.append(c);
            } else {
                word.append(c);
            }
        }

        if (!word.isEmpty()) {
            Emote emote = EmoteRegistry.getInstance().getEmoteByName(word.toString());
            if (emote != null) {
                out.append("\ua950").append(emote.getId());
            } else {
                out.append(word);
            }
        }

        return out.toString().replaceAll("\uD83E\uDDAD", ":seal:");
    }
}
