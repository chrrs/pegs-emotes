package me.chrrrs.pegsemotes.mixin;

import me.chrrrs.pegsemotes.emotes.Emote;
import me.chrrrs.pegsemotes.emotes.EmoteRegistry;
import net.minecraft.client.util.ChatMessages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatMessages.class)
public class ChatMessagesMixin {
    @ModifyVariable(method = "getRenderedChatMessage", at = @At("HEAD"), ordinal = 0)
    private static String getRenderedChatMessageParam(String message) {
        for (Emote emote : EmoteRegistry.getInstance().emoteMap.values()) {
            message = message.replaceAll("\\b" + emote.getName() + "\\b", "\u00a8" + emote.getId());
        }

        return message;
    }

//    @Redirect(method = "refresh", at = @At(value = "INVOKE", target = "Lnet/minecraft/command/CommandSource;getPlayerNames()Ljava/util/Collection;"))
//    private Collection<String> breakRenderedChatMessageLines(CommandSource commandSource) {
//        Collection<String> suggestions = EmoteRegistry.getInstance().getEmoteSuggestions();
//
//        suggestions.addAll(commandSource.getPlayerNames());
//
//        return suggestions;
//    }
}
