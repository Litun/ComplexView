package io.litun.complexview.model;

/**
 * Created by Litun on 13.04.2017.
 */
public class MarkdownFrame {
    private float x;
    private float y;
    private float width;
    private float height;

    public MarkdownFrame(float x, float y, float width, float height) {
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
}
