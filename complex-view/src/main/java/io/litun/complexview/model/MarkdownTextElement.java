package io.litun.complexview.model;

/**
 * Created by Litun on 29.03.2017.
 */

public class MarkdownTextElement extends MarkdownElement {
    private final String text;

    public MarkdownTextElement(MarkdownFrame frame, String text) {
        super(frame);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
