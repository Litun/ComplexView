package io.litun.complexview.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Litun on 21.05.2017.
 */
public class Markup2 {
    private final float width;
    private final float height;
    private final List<MarkupFrame2> frames;
    private ScaleMode scaleMode;

    public Markup2(Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.frames = builder.frames;
        this.scaleMode = builder.scaleMode;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public List<MarkupFrame2> getFrames() {
        return frames;
    }

    public ScaleMode getScaleMode() {
        return scaleMode;
    }

    public void setScaleMode(ScaleMode scaleMode) {
        this.scaleMode = scaleMode;
    }

    public static class Builder {
        private float width;
        private float height;
        private ScaleMode scaleMode = ScaleMode.INSCRIBE;
        private final List<MarkupFrame2> frames;

        public Builder() {
            frames = new ArrayList<>();
        }

        public Builder setSize(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setScaleMode(ScaleMode scaleMode) {
            this.scaleMode = scaleMode;
            return this;
        }

        public Builder addFrame(MarkupFrame2 frame) {
            frames.add(frame);
            return this;
        }

        public Markup2 build() {
            return new Markup2(this);
        }
    }
}
