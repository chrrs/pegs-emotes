package me.chrrrs.pegsemotes.mixin;

import me.chrrrs.pegsemotes.emotes.Emote;
import me.chrrrs.pegsemotes.emotes.EmoteRegistry;
import net.minecraft.client.util.ChatMessages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatMessages.class)
public class ChatMessagesMixin {
    @ModifyVariable(method = "getRenderedChatMessage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static String getRenderedChatMessageParam(String message) {
        for (Emote emote : EmoteRegistry.getInstance().getEmotes()) {
            String regex = emote.getName();

            if (Character.isAlphabetic(regex.charAt(0))) {
                regex = "\\b" + regex;
            } else {
                regex = "\\B" + regex;
            }

            if (Character.isAlphabetic(regex.charAt(regex.length() - 1))) {
                regex = regex + "\\b";
            } else {
                regex = regex + "\\B";
            }

            message = message.replaceAll(regex, "\u00a8" + emote.getId());
        }

        return message;
    }
}
