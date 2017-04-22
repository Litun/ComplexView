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
        this.layers = null;
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
        private List<MarkdownElement> elements;
        private String fileName;

        public Builder() {
        }

        public Builder setSourceFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setSize(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public ComplexViewModel build() {
            return new ComplexViewModel(this);
        }
    }
}
