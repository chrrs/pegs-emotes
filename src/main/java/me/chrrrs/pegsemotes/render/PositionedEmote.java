package me.chrrrs.pegsemotes.render;

import me.chrrrs.pegsemotes.emotes.FetchedEmote;

public record PositionedEmote(FetchedEmote emote, float x, float y) {
}
