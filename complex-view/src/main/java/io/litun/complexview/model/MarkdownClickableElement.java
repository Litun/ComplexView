package io.litun.complexview.model;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;


/**
 * Created by Litun on 29.03.2017.
 */

public class MarkdownClickableElement extends MarkdownDrawableElement {
    @ColorInt
    private final int defaultColor;
    @ColorInt
    private final int selectColor;
    private final int number;
    private boolean selected;

    public MarkdownClickableElement(MarkdownFrame frame, Drawable drawable,
                                    @ColorInt int seatColor, @ColorInt int selectColor, int number) {
        super(frame, drawable, drawableRes);
        this.defaultColor = seatColor;
        this.selectColor = selectColor;
        this.number = number;
        this.selected = false;
    }

    @Override
    public Drawable getDrawable() {
        Drawable drawable = super.getDrawable();
        if (selected) {
            drawable.setColorFilter(selectColor, PorterDuff.Mode.SRC_IN);
        } else {
            drawable.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN);
        }
        return drawable;
    }

    public int getNumber() {
        return number;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
