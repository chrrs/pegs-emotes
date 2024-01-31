package me.chrr.pegsemotes.mixin;

import me.chrr.pegsemotes.emote.RepositoryManager;
import net.minecraft.client.network.ClientCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Mixin(ClientCommandSource.class)
public abstract class ClientCommandSourceMixin {
    @Inject(method = "getChatSuggestions", at = @At("RETURN"), cancellable = true)
    public void addSuggestions(CallbackInfoReturnable<Collection<String>> cir) {
        Set<String> suggestions = new HashSet<>(cir.getReturnValue());
        suggestions.addAll(RepositoryManager.getInstance().getEmoteNames());
        cir.setReturnValue(suggestions);
    }
}
