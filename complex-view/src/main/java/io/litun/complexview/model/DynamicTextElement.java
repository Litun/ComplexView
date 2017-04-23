package io.litun.complexview.model;

/**
 * Created by Litun on 23.04.2017.
 */

public class DynamicTextElement extends DynamicElement {
    private float textSize;
    private float x;
    private float y;
    private String text;

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
