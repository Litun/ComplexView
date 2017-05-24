package io.litun.complexview.model;

/**
 * Created by Litun on 15.05.2017.
 */

public class MarkupElement2 {
    private final float marginLeft;
    private final float marginTop;
    private final float marginRight;
    private final float marginBottom;
    private final int layer;
    private final ScaleMode scaleMode;

    public MarkupElement2(int layer) {
        this(0f, 0f, 0f, 0f, layer, ScaleMode.FILL);
    }

    public MarkupElement2(float marginLeft,
                          float marginTop,
                          float marginRight,
                          float marginBottom,
                          int layer,
                          ScaleMode scaleMode) {
        this.marginLeft = marginLeft;
        this.marginTop = marginTop;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
        this.layer = layer;
        this.scaleMode = scaleMode;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    public float getMarginTop() {
        return marginTop;
    }

    public float getMarginRight() {
        return marginRight;
    }

    public float getMarginBottom() {
        return marginBottom;
    }

    public int getLayer() {
        return layer;
    }

    public ScaleMode getScaleMode() {
        return scaleMode;
    }
}
