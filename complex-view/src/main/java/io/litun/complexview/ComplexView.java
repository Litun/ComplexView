package io.litun.complexview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import io.litun.complexview.model.Markup2;
import io.litun.complexview.model.MarkupDrawableElement2;
import io.litun.complexview.model.MarkupElement2;
import io.litun.complexview.model.MarkupFrame;
import io.litun.complexview.model.MarkupFrame2;
import io.litun.complexview.model.MarkupTextElement2;
import io.litun.complexview.model.ScaleMode;

/**
 * Created by Litun on 17.03.17.
 */
public class ComplexView extends View {
    private static final float DEFAULT_RATIO = 768f / 160f;
    private static final int DEFAULT_WIDTH_PIXELS = 320;
    private static final int MAX_ALPHA = 255;

    private final TextPaint textPaint = new TextPaint();
    private final Rect textBounds = new Rect();
    private final Paint paint = new Paint();

    private OnFrameClickListener frameClickListener;
    private ComplexViewModel viewModel;
    private float deltaX = 1f;
    private float deltaY = 1f;
    private float widthScale = 1f;
    private float heightScale = 1f;
    private float fontRatio = 1f;

    public ComplexView(Context context) {
        super(context);
        init(context);
    }

    public ComplexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ComplexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setData(ComplexViewModel viewModel) {
        this.viewModel = viewModel;
        requestLayout();
    }

    public void setFrameClickListener(OnFrameClickListener frameClickListener) {
        this.frameClickListener = frameClickListener;
    }

    private MarkupFrame2 touchingFrame;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (frameClickListener != null) {
            float markupX = (event.getX() - deltaX) / widthScale;
            float markupY = (event.getY() - deltaY) / heightScale;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchingFrame = findSeatElement(markupX, markupY);
                    return touchingFrame != null;
                case MotionEvent.ACTION_CANCEL:
                    touchingFrame = null;
                    return false;
                case MotionEvent.ACTION_MOVE:
                    if (touchingFrame != null && !insideFrame(markupX, markupY, touchingFrame)) {
                        touchingFrame = null;
                    }
                    return touchingFrame != null;
                case MotionEvent.ACTION_UP:
                    if (touchingFrame != null &&
                            insideFrame(markupX, markupY, touchingFrame)) {
                        frameClickListener.onSeatClick(touchingFrame);
                    }
                    touchingFrame = null;
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        float ratio = viewModel != null ?
                viewModel.getMarkup().getHeight() / viewModel.getMarkup().getWidth() :
                DEFAULT_RATIO;

        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            width = widthSize;
        } else {
            width = DEFAULT_WIDTH_PIXELS;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = (int) (width * ratio);
            if (height > heightSize) {
                height = heightSize;
            }
        } else {
            height = (int) (width * ratio);
        }

        if ((height / width < ratio) && (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST)) {
            width = (int) (height / ratio);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (viewModel != null) {
            Markup2 markup = viewModel.getMarkup();
            float width = right - left;
            float height = bottom - top;
            ScaleMode scaleMode = markup.getScaleMode();
            switch (scaleMode) {
                case FILL:
                    deltaX = 0;
                    deltaY = 0;
                    widthScale = width / markup.getWidth();
                    heightScale = height / markup.getHeight();
                    break;
                case INSCRIBE:
                    if (markup.getHeight() / markup.getWidth() > height / width) {
                        widthScale = heightScale = height / markup.getHeight();
                        deltaX = (width - height / markup.getHeight() * markup.getWidth()) / 2;
                        deltaY = 0;
                    } else {
                        widthScale = heightScale = width / markup.getWidth();
                        deltaX = 0;
                        deltaY = (height - width / markup.getWidth() * markup.getHeight()) / 2;
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (viewModel != null) {
            drawFrames(canvas, viewModel.getMarkup().getFrames());
        }
    }

    private void drawFrames(Canvas canvas, List<MarkupFrame2> frames) {
        for (int j = 0; j < frames.size(); j++) {
            MarkupFrame2 frame = frames.get(j);
            drawFrame(canvas, frame);
        }
    }

    private void drawFrame(Canvas canvas, MarkupFrame2 frame) {
        float x = frame.getX() * widthScale + deltaX;
        float y = frame.getY() * heightScale + deltaY;
        float w = frame.getWidth() * widthScale;
        float h = frame.getHeight() * heightScale;
        for (int i = 0; i < frame.getElements().size(); i++) {
            drawElement(canvas, frame.getElements().get(i), x, y, w, h);
        }
    }

    private void drawElement(Canvas canvas, MarkupElement2 element, float x, float y, float w, float h) {
        x += element.getMarginLeft() * w;
        y += element.getMarginTop() * h;
        w *= 1 - element.getMarginLeft() - element.getMarginRight();
        h *= 1 - element.getMarginTop() - element.getMarginBottom();
        if (element instanceof MarkupDrawableElement2) {
            drawDrawableElement(canvas, (MarkupDrawableElement2) element, x, y, w, h, element.getScaleMode());
        } else if (element instanceof MarkupTextElement2) {
            drawTextElement(canvas, (MarkupTextElement2) element, x, y, w, h, element.getScaleMode());
        }
    }

    private void drawTextElement(Canvas canvas, MarkupTextElement2 element,
                                 float x, float y, float w, float h, ScaleMode scaleMode) {
        String text = element.getText();
        float alpha = 1f; // TODO: implement
        // inscribe text by height
        float textSize;
        if (fontRatio / text.length() > h / w) {
            textSize = h;
        } else { // inscribe text by width
            textSize = w / text.length() * fontRatio;
        }
        textPaint.setTextSize(textSize);
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        textPaint.setAlpha((int) (MAX_ALPHA * alpha));
        float x1 = (x + w / 2) - textBounds.width() / 2f - textBounds.left;
        float y1 = (y + h / 2) + textSize / 2f;
        canvas.drawText(text,
                x1,
                y1,
                textPaint);
    }

    private void drawDrawableElement(Canvas canvas, MarkupDrawableElement2 element,
                                     float x, float y, float w, float h, ScaleMode scaleMode) {
        Drawable drawable = element.getDefaultDrawable();
        float alpha = 1f; // TODO: implement
        float leftBound = x;
        float topBound = y;
        float rightBound = x + w;
        float bottomBound = y + h;
        if (scaleMode == ScaleMode.INSCRIBE) {
            int drawableHeight = drawable.getIntrinsicHeight();
            int drawableWidth = drawable.getIntrinsicWidth();
            if (drawableHeight != -1 && drawableWidth != -1) {
                if (h / w > (float) drawableHeight / drawableWidth) {
                    float newHeight = w / drawableWidth * drawableHeight;
                    topBound += (h - newHeight) / 2;
                    bottomBound = topBound + newHeight;
                } else {
                    float newWidth = h / drawableHeight * drawableWidth;
                    leftBound += (w - newWidth) / 2;
                    rightBound = leftBound + newWidth;
                }
            }
        }
        drawable.setBounds((int) leftBound,
                (int) topBound,
                (int) rightBound,
                (int) bottomBound);
        drawable.setAlpha((int) (MAX_ALPHA * alpha));
        drawable.draw(canvas);
    }

    private void drawInFrame(Canvas canvas, Drawable drawable, MarkupFrame frame, float alpha) {
        int drawableHeight = drawable.getIntrinsicHeight();
        int drawableWidth = drawable.getIntrinsicWidth();

        float x = frame.getX();
        float y = frame.getY();
        float w = frame.getWidth();
        float h = frame.getHeight();
        if (drawableHeight != -1 && drawableWidth != -1) {
            if (frame.getHeight() / frame.getWidth() > (float) drawableHeight / drawableWidth) {
                float newHeight = frame.getWidth() / drawableWidth * drawableHeight;
                y += (frame.getHeight() - newHeight) / 2;
                h = newHeight;
            } else {
                float newWidth = frame.getHeight() / drawableHeight * drawableWidth;
                x += (frame.getWidth() - newWidth) / 2;
                w = newWidth;
            }
        }

        float leftBound = x;
        float topBound = y;
        float rightBound = leftBound + w;
        float bottomBound = topBound + h;

        drawable.setBounds((int) (leftBound * widthScale),
                (int) (topBound * heightScale),
                (int) (rightBound * widthScale),
                (int) (bottomBound * heightScale));
        drawable.setAlpha((int) (MAX_ALPHA * alpha));
        drawable.draw(canvas);

    }

    private void drawInFrame(Canvas canvas, String text, MarkupFrame frame, float alpha) {
        // inscribe text by height
        float textSize;
        if (fontRatio / text.length() > frame.getHeight() / frame.getWidth()) {
            textSize = frame.getHeight() * heightScale;
        } else { // inscribe text by width
            textSize = frame.getWidth() * widthScale / text.length() * fontRatio;
        }
        textPaint.setTextSize(textSize);
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        textPaint.setAlpha((int) (MAX_ALPHA * alpha));
        float x = (frame.getX() + frame.getWidth() / 2) * widthScale - textBounds.width() / 2f - textBounds.left;
        float y = (frame.getY() + frame.getHeight() / 2) * heightScale + textSize / 2f;
//        canvas.drawRect(x + textBounds.left,
//                y - textBounds.height() + textBounds.bottom,
//                x + textBounds.width() + textBounds.left,
//                y + textBounds.bottom,
//                paint);
        canvas.drawText(text,
                x,
                y,
                textPaint);
    }

    private void init(Context context) {
        paint.setColor(Color.LTGRAY);
        textPaint.setAntiAlias(true);
        // TODO: refactor text color and font source
        textPaint.setColor(Color.BLACK);

        final float testTextHeight = 100f;
        textPaint.setTextSize(testTextHeight);
        float testTextWidth = textPaint.measureText("0");
        fontRatio = testTextHeight / testTextWidth;
    }

    @Nullable
    private MarkupFrame2 findSeatElement(float markupX, float markupY) {
        List<MarkupFrame2> frames = viewModel.getMarkup().getFrames();
        for (int i = 0; i < frames.size(); i++) {
            MarkupFrame2 frame = frames.get(i);
            if (insideFrame(markupX, markupY, frame)) {
                return frame;
            }
        }
        return null;
    }

    private boolean insideFrame(float markupX, float markupY, MarkupFrame2 frame) {
        return markupX >= frame.getX() &&
                markupX <= frame.getX() + frame.getWidth() &&
                markupY > frame.getY() &&
                markupY <= frame.getY() + frame.getHeight();
    }

    public void setScaleMode(ScaleMode scaleMode) {
        viewModel.getMarkup().setScaleMode(scaleMode);
    }

    public interface OnFrameClickListener {
        void onSeatClick(MarkupFrame2 frame);
    }
}
