package io.litun.complexview.model;

/**
 * Created by Litun on 29.03.2017.
 */

public abstract class MarkupElement {
    private final MarkupFrame frame;

    public MarkupElement(MarkupFrame frame) {
        this.frame = frame;
    }

    public MarkupFrame getFrame() {
        return frame;
    }
}
