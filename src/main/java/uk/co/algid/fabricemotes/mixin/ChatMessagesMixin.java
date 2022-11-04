package uk.co.algid.fabricemotes.mixin;

import net.minecraft.client.util.ChatMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import uk.co.algid.fabricemotes.Constants;
import uk.co.algid.fabricemotes.emotes.Emote;
import uk.co.algid.fabricemotes.emotes.EmoteRegistry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ChatMessages.class)
public class ChatMessagesMixin {
    private static final Logger MOD_LOGGER = LogManager.getLogger("fabricemotes.mixin.ChatMessagesMixin");

    @ModifyVariable(method = "getRenderedChatMessage", at = @At("HEAD"), ordinal = 0)
    private static String getRenderedChatMessageParam(String message) {
        final Pattern emotePattern = Pattern.compile(Constants.EMOTE_PATTERN_STR);

        boolean emotesLeft = true;

        while(emotesLeft) {
            final Matcher emoteMatch = emotePattern.matcher(message);

            while (emotesLeft = emoteMatch.find()) {
                String emoteName = emoteMatch.group(2);
                Emote emote = EmoteRegistry.getInstance().getEmoteByName(emoteName);

                int startPos = emoteMatch.start(1);
                int endPos = emoteMatch.end(1);

                if (emote != null) {
                    message = message.substring(0, startPos) + "▏" + emote.getId() + message.substring(endPos);
                    break;
                }
            }
        }

        return message;
    }
}
