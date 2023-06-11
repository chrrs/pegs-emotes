package me.chrr.pegsemotes.render;

import me.chrr.pegsemotes.emotes.FetchedEmote;

public record PositionedEmote(FetchedEmote emote, float x, float y) {
}
