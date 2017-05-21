package io.litun.complexview.model;

/**
 * Created by Litun on 21.05.2017.
 */

public class MarkupTextElement2 extends MarkupElement2 {
    private final String text;

    public MarkupTextElement2(float marginLeft,
                              float marginTop,
                              float marginRight,
                              float marginBottom,
                              int layer,
                              ScaleMode scaleMode,
                              String text) {
        super(marginLeft, marginTop, marginRight, marginBottom, layer, scaleMode);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
