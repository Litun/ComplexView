package io.litun.complexview.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Litun on 14.05.2017.
 */
public class MarkupFrame2 {
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final List<MarkupElement2> elements;

    public MarkupFrame2(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
        this.elements = builder.elements;
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

    public List<MarkupElement2> getElements() {
        return elements;
    }

    public static class Builder {
        private float x;
        private float y;
        private float width;
        private float height;
        private final List<MarkupElement2> elements = new ArrayList<>();

        public Builder(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public Builder addElement(MarkupElement2 element) {
            elements.add(element);
            return this;
        }

        public MarkupFrame2 build() {
            return new MarkupFrame2(this);
        }
    }
}
