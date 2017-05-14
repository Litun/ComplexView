package io.litun.complexview.model;

/**
 * Created by Litun on 15.05.2017.
 */

public class MarkupElement2 {
    private float marginLeft;
    private float marginTop;
    private float marginRight;
    private float marginBottom;
    private int layer;
    private HorizontalGravity horizontalGravity = HorizontalGravity.FILL;
    private VerticalGravity verticalGravity = VerticalGravity.FILL;

    public enum HorizontalGravity {
        FILL,
        CENTER,
        LEFT,
        RIGHT,
    }

    public enum VerticalGravity {
        FILL,
        CENTER,
        TOP,
        BOTTOM,
    }
}
