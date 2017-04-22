package io.litun.complexview.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Litun on 29.03.2017.
 */
public class MarkdownDrawableElement extends MarkdownElement {
    private final Drawable drawable;

    public MarkdownDrawableElement(MarkdownFrame frame, Drawable drawable) {
        super(frame);
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
    }
}
