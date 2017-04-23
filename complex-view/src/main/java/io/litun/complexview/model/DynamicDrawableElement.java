package io.litun.complexview.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Litun on 23.04.2017.
 */

public class DynamicDrawableElement extends DynamicElement{
    private int leftBound;
    private int topBound;
    private int rightBound;
    private int bottomBound;
    private Drawable drawable;

    public int getLeftBound() {
        return leftBound;
    }

    public void setLeftBound(int leftBound) {
        this.leftBound = leftBound;
    }

    public int getTopBound() {
        return topBound;
    }

    public void setTopBound(int topBound) {
        this.topBound = topBound;
    }

    public int getRightBound() {
        return rightBound;
    }

    public void setRightBound(int rightBound) {
        this.rightBound = rightBound;
    }

    public int getBottomBound() {
        return bottomBound;
    }

    public void setBottomBound(int bottomBound) {
        this.bottomBound = bottomBound;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
