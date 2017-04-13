package io.litun.complexview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.litun.complexview.model.MarkdownElement;

/**
 * Created by Litun on 22.03.17.
 */
public class ComplexViewModel {
    public static final int OVERVIEW_LAYER = 0;
    public static final int DETAILS_LAYER = 1;

    private final float width;
    private final float height;
    private final List<List<MarkdownElement>> layers;

    public ComplexViewModel(Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.layers = Collections.unmodifiableList(builder.layers);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public List<List<MarkdownElement>> getLayers() {
        return layers;
    }

    public static class Builder {
        private float width;
        private float height;
        private final List<List<MarkdownElement>> layers;

        public Builder() {
            layers = new ArrayList<>();
            layers.add(new ArrayList<MarkdownElement>());
        }

        public Builder setSize(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder addElement(int layer, MarkdownElement element) {
            while (layer >= layers.size()) {
                layers.add(new ArrayList<MarkdownElement>());
            }
            List<MarkdownElement> layerElements = layers.get(layer);
            layerElements.add(element);
            return this;
        }

        public ComplexViewModel build() {
            return new ComplexViewModel(this);
        }
    }
}
