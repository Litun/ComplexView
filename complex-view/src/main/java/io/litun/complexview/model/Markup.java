package io.litun.complexview.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Litun on 22.04.2017.
 */

public class Markup {
    private final float width;
    private final float height;
    private final List<List<MarkupElement>> layers;

    private Markup(Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.layers = builder.layers;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public List<List<MarkupElement>> getLayers() {
        return layers;
    }

    public static class Builder {
        private float width;
        private float height;
        private final List<List<MarkupElement>> layers;

        public Builder() {
            layers = new ArrayList<>();
            layers.add(new ArrayList<MarkupElement>());
        }

        public Builder setSize(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder addElement(int layer, MarkupElement element) {
            while (layer >= layers.size()) {
                layers.add(new ArrayList<MarkupElement>());
            }
            List<MarkupElement> layerElements = layers.get(layer);
            layerElements.add(element);
            return this;
        }

        public Markup build() {
            return new Markup(this);
        }
    }
}
