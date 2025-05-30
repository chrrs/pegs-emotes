package me.chrr.pegsemotes.font;

import me.chrr.pegsemotes.emote.RepositoryManager;
import net.minecraft.client.font.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class EmoteFontStorage extends FontStorage {
    private final Map<Glyph, BakedGlyph> bakedGlyphCache = new HashMap<>();

    public EmoteFontStorage(TextureManager textureManager, Identifier id) {
        super(textureManager, id);
    }

    @Override
    public Glyph getGlyph(int codePoint, boolean validateAdvance) {
        return RepositoryManager.getInstance()
                .getEmote(codePoint)
                .map((emote) -> emote.getFrame(Instant.now()))
                .orElse(BuiltinEmptyGlyph.MISSING);
    }

    @Override
    public BakedGlyph getBaked(int codePoint) {
        Glyph glyph = getGlyph(codePoint, false);
        return bakedGlyphCache.computeIfAbsent(glyph, g -> g.bake(this::bake));
    }
}
