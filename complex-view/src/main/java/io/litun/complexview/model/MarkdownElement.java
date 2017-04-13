package io.litun.complexview.model;

/**
 * Created by Litun on 29.03.2017.
 */

public abstract class MarkdownElement {
    private final MarkdownFrame frame;

    public MarkdownElement(MarkdownFrame frame) {
        this.frame = frame;
    }

    public MarkdownFrame getFrame() {
        return frame;
    }
}
