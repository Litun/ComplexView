package io.litun.complexview.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Litun on 23.04.2017.
 */

public class DynamicLayout {
    private final List<DynamicDrawableElement> drawableElements = new ArrayList<>();
    private final List<DynamicTextElement> textElements = new ArrayList<>();
    private int drawableCount = 0;
    private int textCount = 0;

    public int getDrawableCount() {
        return drawableCount;
    }

    public void setDrawableCount(int drawableCount) {
        this.drawableCount = drawableCount;
    }

    public int getTextCount() {
        return textCount;
    }

    public void setTextCount(int textCount) {
        this.textCount = textCount;
    }

    public List<DynamicDrawableElement> getDrawableElements() {
        return drawableElements;
    }

    public List<DynamicTextElement> getTextElements() {
        return textElements;
    }
}
