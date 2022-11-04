package uk.co.algid.fabricemotes.text;

import net.minecraft.text.Style;

public class TextPart {
    private Style style;
    private char chr;

    public TextPart(Style style, char chr) {
        this.style = style;
        this.chr = chr;
    }

    public Style getStyle() {
        return style;
    }

    public char getChr() {
        return chr;
    }
}
