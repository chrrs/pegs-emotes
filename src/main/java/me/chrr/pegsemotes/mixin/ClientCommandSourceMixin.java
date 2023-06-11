package me.chrr.pegsemotes.mixin;

import me.chrr.pegsemotes.emotes.Emote;
import me.chrr.pegsemotes.emotes.EmoteRegistry;
import net.minecraft.client.network.ClientCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;

@Mixin(ClientCommandSource.class)
public class ClientCommandSourceMixin {
    @Inject(method = "getChatSuggestions", at = @At("RETURN"), cancellable = true)
    private void addSuggestions(CallbackInfoReturnable<Collection<String>> cir) {
        ArrayList<String> suggestions = new ArrayList<>(cir.getReturnValue());

        for (Emote emote : EmoteRegistry.getInstance().getEmotes()) {
            suggestions.add(emote.getName());
        }

        cir.setReturnValue(suggestions);
    }
}
