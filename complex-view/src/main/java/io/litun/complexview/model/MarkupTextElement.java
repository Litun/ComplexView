package io.litun.complexview.model;

/**
 * Created by Litun on 29.03.2017.
 */

public class MarkupTextElement extends MarkupElement {
    private final String text;

    public MarkupTextElement(MarkupFrame frame, String text) {
        super(frame);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
