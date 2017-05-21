package io.litun.complexview.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * Created by Litun on 21.05.2017.
 */

public class MarkupDrawableElement2 extends MarkupElement2 {
    private final Drawable defaultDrawable;
    @Nullable
    private final Drawable selectedDrawable;

    public MarkupDrawableElement2(int layer,
                                  Drawable defaultDrawable,
                                  @Nullable Drawable selectedDrawable) {
        super(layer);
        this.defaultDrawable = defaultDrawable;
        this.selectedDrawable = selectedDrawable;
    }

    public MarkupDrawableElement2(float marginLeft,
                                  float marginTop,
                                  float marginRight,
                                  float marginBottom,
                                  int layer,
                                  ScaleMode scaleMode,
                                  Drawable defaultDrawable,
                                  @Nullable Drawable selectedDrawable) {
        super(marginLeft, marginTop, marginRight, marginBottom, layer, scaleMode);
        this.defaultDrawable = defaultDrawable;
        this.selectedDrawable = selectedDrawable;
    }

    public Drawable getDefaultDrawable() {
        return defaultDrawable;
    }

    @Nullable
    public Drawable getSelectedDrawable() {
        return selectedDrawable;
    }
}
