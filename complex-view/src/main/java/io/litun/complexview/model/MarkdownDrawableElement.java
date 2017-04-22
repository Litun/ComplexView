package io.litun.complexview.model;

import android.support.annotation.DrawableRes;

/**
 * Created by Litun on 29.03.2017.
 */
public class MarkdownDrawableElement extends MarkdownElement {
    @DrawableRes
    private final int drawableRes;

    public MarkdownDrawableElement(MarkdownFrame frame, int drawableRes) {
        super(frame);
        this.drawableRes = drawableRes;
    }

    @DrawableRes
    public int getDrawableRes() {
        return drawableRes;
    }
}
