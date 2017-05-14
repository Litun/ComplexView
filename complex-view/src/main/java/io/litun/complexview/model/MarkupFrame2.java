package io.litun.complexview.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Litun on 14.05.2017.
 */

public class MarkupFrame2 {
    private float x;
    private float y;
    private float width;
    private float height;
    HorizontalPosition horizontalPosition = HorizontalPosition.CENTER;
    VerticalPosition verticalPosition = VerticalPosition.CENTER;
    private final List<MarkupElement2> elements = new ArrayList<>();

    public MarkupFrame2(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public HorizontalPosition getHorizontalPosition() {
        return horizontalPosition;
    }

    public void setHorizontalPosition(HorizontalPosition horizontalPosition) {
        this.horizontalPosition = horizontalPosition;
    }

    public VerticalPosition getVerticalPosition() {
        return verticalPosition;
    }

    public void setVerticalPosition(VerticalPosition verticalPosition) {
        this.verticalPosition = verticalPosition;
    }

    public List<MarkupElement2> getElements() {
        return elements;
    }

    public enum HorizontalPosition {
        SCALE,
        LEFT,
        RIGHT,
        CENTER,
    }

    public enum VerticalPosition {
        SCALE,
        TOP,
        BOTTOM,
        CENTER,
    }
}
