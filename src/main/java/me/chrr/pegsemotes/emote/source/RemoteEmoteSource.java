package me.chrr.pegsemotes.emote.source;

import me.chrr.pegsemotes.EmoteMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class RemoteEmoteSource implements EmoteSource {
    public final RemoteEmote remoteEmote;

    public Identifier textureIdentifier;
    public State state = State.UNFETCHED;

    public RemoteEmoteSource(RemoteEmote remoteEmote) {
        this.remoteEmote = remoteEmote;
    }

    @Override
    public int getRenderedWidth() {
        // As most emotes are square, we predict the width being the same as height.
        //noinspection SuspiciousNameCombination
        return EmoteMod.EMOTE_HEIGHT;
    }

    @Override
    public void draw(DrawContext context, float x, float y, float alpha) {
        // We don't draw anything for remote emotes.
    }

    public enum State {
        UNFETCHED, FETCHING, ERRORED, FETCHED
    }
}
