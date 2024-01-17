package me.chrr.pegsemotes.font;

import me.chrr.pegsemotes.emote.ApiEmotes;
import me.chrr.pegsemotes.emote.Emote;
import me.chrr.pegsemotes.emote.EmoteFetcher;
import net.minecraft.client.font.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class EmoteFontStorage extends FontStorage {
//    /tellraw @a ["|",{"text":"\u0000","font":"pegs-emotes:font/emotes"},{"text":"|","font":"default"}]
//    /tellraw @a ["<chrrz> yeah ",{"text":"\u0000","font":"pegs-emotes:font/emotes","hoverEvent":{"action":"show_text","value":"OMEGALUL"}}]

    private final Map<Glyph, GlyphRenderer> glyphRendererCache = new HashMap<>();

    private Future<Emote> emote;

    public EmoteFontStorage(TextureManager textureManager, Identifier id) {
        super(textureManager, id);

        try {
            ApiEmotes apiEmotes = ApiEmotes.fetchFrom(new URL("file:///D:\\Dev\\Java\\FabricEmotes\\content\\v2\\emotes.json"));
            emote = new EmoteFetcher().fetchEmote(apiEmotes.emotes[0]);
        } catch (Exception e) {
            emote = CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public void setFonts(List<Font> fonts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Glyph getGlyph(int codePoint, boolean validateAdvance) {
        if (emote.isDone()) {
            try {
                return emote.get().getFrame(Instant.now());
            } catch (InterruptedException | ExecutionException e) {
                return BuiltinEmptyGlyph.MISSING;
            }
        } else {
            return BuiltinEmptyGlyph.MISSING;
        }
    }

    @Override
    public GlyphRenderer getGlyphRenderer(int codePoint) {
        Glyph glyph = getGlyph(codePoint, false);
        return glyphRendererCache.computeIfAbsent(glyph, g -> g.bake(this::getGlyphRenderer));
    }
}
