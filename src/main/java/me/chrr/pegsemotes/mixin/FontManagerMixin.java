package me.chrr.pegsemotes.mixin;

import me.chrr.pegsemotes.font.EmoteFontStorage;
import net.minecraft.client.font.FontManager;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(FontManager.class)
public abstract class FontManagerMixin {
    @Shadow
    @Final
    private TextureManager textureManager;

    @Shadow
    @Final
    private Map<Identifier, FontStorage> fontStorages;

    @Inject(method = "reload(Lnet/minecraft/client/font/FontManager$ProviderIndex;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("TAIL"))
    public void injectEmoteFontStorage(CallbackInfo ci) {
        Identifier id = Identifier.of("pegs-emotes", "font/emotes");
        fontStorages.put(id, new EmoteFontStorage(textureManager, id));
    }
}
