package io.litun.complexview.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Litun on 29.03.2017.
 */
public class MarkupDrawableElement extends MarkupElement {
    private final Drawable drawable;

    public MarkupDrawableElement(MarkupFrame frame, Drawable drawable) {
        super(frame);
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
    }
}
