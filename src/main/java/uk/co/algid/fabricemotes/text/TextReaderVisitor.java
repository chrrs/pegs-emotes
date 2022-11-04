package uk.co.algid.fabricemotes.text;

import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;

import java.util.ArrayList;
import java.util.List;

public class TextReaderVisitor implements CharacterVisitor {
    private List<TextPart> textParts = new ArrayList<>();

    public boolean accept(int val, Style style, int currCharInt) {
        textParts.add(new TextPart(style, (char) currCharInt));
        return true;
    }

    /**
     * Delete chars between two indexes and insert next text in it's place
     *
     * @param beginIndex – the beginning index, inclusive
     * @param endIndex – the ending index, exclusive
     * @param text - the text to insert
     * @param style - the style to use for the inserted text
     */
    public void replaceBetween(int beginIndex, int endIndex, String text, Style style) {
        deleteBetween(beginIndex, endIndex);
        insertAt(beginIndex, text, style);
    }

    /**
     * Delete chars between two indexes
     *
     * @param beginIndex – the beginning index, inclusive
     * @param endIndex – the ending index, exclusive
     */
    public void deleteBetween(int beginIndex, int endIndex) {
        for(int i = endIndex - 1; i >= beginIndex; i--) {
            textParts.remove(i);
        }
    }

    /**
     * Insert text at a specified index
     *
     * @param index - index to insert after
     * @param text - text to insert
     * @param style - style to apply to the text
     */
    public void insertAt(int index, String text, Style style) {
        for(int i = 0; i < text.length(); i++) {
            textParts.add(index + i, new TextPart(style, text.charAt(i)));
        }
    }

    public OrderedText getOrderedText() {
        LiteralText literalText = new LiteralText("");

        for(TextPart textPart : textParts) {
            literalText.append(new LiteralText(Character.toString((textPart.getChr()))).setStyle(textPart.getStyle()));
        }

        return literalText.asOrderedText();
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();

        for(TextPart textPart : textParts) {
            sb.append(textPart.getChr());
        }

        return sb.toString();
    }
}
