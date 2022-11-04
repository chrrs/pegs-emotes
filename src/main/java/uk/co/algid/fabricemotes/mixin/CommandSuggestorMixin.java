package uk.co.algid.fabricemotes.mixin;

import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.network.ClientCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import uk.co.algid.fabricemotes.emotes.EmoteRegistry;

import java.util.Collection;

@Mixin(CommandSuggestor.class)
public class CommandSuggestorMixin {
    @Redirect(method = "refresh", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientCommandSource;getPlayerNames()Ljava/util/Collection;"))
    private Collection<String> breakRenderedChatMessageLines(ClientCommandSource clientCommandSource) {
        Collection<String> suggestions = EmoteRegistry.getInstance().getEmoteSuggestions();

        suggestions.addAll(clientCommandSource.getPlayerNames());

        return suggestions;
    }
}
