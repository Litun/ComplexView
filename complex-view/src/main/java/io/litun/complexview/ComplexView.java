package io.litun.complexview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import io.litun.complexview.model.MarkdownDrawableElement;
import io.litun.complexview.model.MarkdownElement;
import io.litun.complexview.model.MarkdownFrame;
import io.litun.complexview.model.MarkdownClickableElement;
import io.litun.complexview.model.MarkdownTextElement;

/**
 * Created by Litun on 17.03.17.
 */
public class ComplexView extends View {
    private static final float DEFAULT_RATIO = 768f / 160f;
    private static final int DEFAULT_WIDTH_PIXELS = 320;
    private static final int MAX_ALPHA = 255;

    private final TextPaint textPaint = new TextPaint();
    private final Rect textBounds = new Rect();

    private OnSeatClickListener seatClickListener;
    private ComplexViewModel viewModel;
    private float widthScale = 1f;
    private float heightScale = 1f;
    private State state = State.OVERVIEW;
    private float transitionProgress = 0f;
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

    public void setSeatClickListener(OnSeatClickListener seatClickListener) {
        this.seatClickListener = seatClickListener;
    }

    public void updateTransitionProgress(float transitionProgress) {
        this.transitionProgress = transitionProgress;
    }

    public void startTransition() {
        this.transitionProgress = 0f;
        state = State.TRANSITION;
    }

    public void finishTransition() {
        this.transitionProgress = 1f;
        state = State.DETAILED;
    }

    private MarkdownClickableElement touchingSeat;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (seatClickListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchingSeat = findSeatElement(event.getX(), event.getY());
                    return touchingSeat != null;
                case MotionEvent.ACTION_CANCEL:
                    touchingSeat = null;
                    return false;
                case MotionEvent.ACTION_UP:
                    if (touchingSeat != null &&
                            insideFrame(event.getX() / widthScale, event.getY() / heightScale,
                                    touchingSeat.getFrame())) {
                        seatClickListener.onSeatClick(touchingSeat);
                    }
                    touchingSeat = null;
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

        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            width = widthSize;
        } else {
            width = DEFAULT_WIDTH_PIXELS;
        }

        float ratio = viewModel != null ?
                viewModel.getHeight() / viewModel.getWidth() :
                DEFAULT_RATIO;

        height = (int) (width * ratio);

        if (heightMode == MeasureSpec.AT_MOST && height > heightSize ||
                heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;

        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (viewModel != null) {
            widthScale = getWidth() / viewModel.getWidth();
            heightScale = getHeight() / viewModel.getHeight();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (viewModel != null) {
            List<List<MarkdownElement>> layers = viewModel.getLayers();
            drawLayer(canvas, layers.get(State.OVERVIEW.getLayerNumber()), 1f);
            if (state == State.DETAILED) {
                drawLayer(canvas, layers.get(State.DETAILED.getLayerNumber()), 1f);
            } else if (state == State.TRANSITION) {
                drawLayer(canvas, layers.get(State.DETAILED.getLayerNumber()), transitionProgress);
            }
        }
    }

    private void drawLayer(Canvas canvas, List<MarkdownElement> layerElements, float alpha) {
        for (int j = 0; j < layerElements.size(); j++) {
            MarkdownElement element = layerElements.get(j);
            drawElement(canvas, element, alpha);
        }
    }

    private void drawElement(Canvas canvas, MarkdownElement element, float alpha) {
        if (element instanceof MarkdownDrawableElement) {
            drawInFrame(canvas, ((MarkdownDrawableElement) element).getDrawable(),
                    element.getFrame(), alpha);
        } else if (element instanceof MarkdownTextElement) {
            drawInFrame(canvas, ((MarkdownTextElement) element).getText(), element.getFrame(),
                    alpha);
        }
    }

    private void drawInFrame(Canvas canvas, Drawable drawable, MarkdownFrame frame, float alpha) {
        float leftBound = frame.getX();
        float topBound = frame.getY();
        float rightBound = leftBound + frame.getWidth();
        float bottomBound = topBound + frame.getHeight();

        drawable.setBounds((int) (leftBound * widthScale),
                (int) (topBound * heightScale),
                (int) (rightBound * widthScale),
                (int) (bottomBound * heightScale));
        drawable.setAlpha((int) (MAX_ALPHA * alpha));
        drawable.draw(canvas);
    }

    private void drawInFrame(Canvas canvas, String text, MarkdownFrame frame, float alpha) {
        // inscribe text by height
        if (fontRatio / text.length() > frame.getHeight() / frame.getWidth()) {
            textPaint.setTextSize(frame.getHeight() * heightScale);
        } else { // inscribe text by width
            textPaint.setTextSize(frame.getWidth() * widthScale / text.length() * fontRatio);
        }
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        textPaint.setAlpha((int) (MAX_ALPHA * alpha));
        canvas.drawText(text,
                (frame.getX() + frame.getWidth() / 2) * widthScale - (textBounds.right + textBounds.left) / 2f,
                (frame.getY() + frame.getHeight() / 2) * heightScale + textBounds.height() / 2f + textBounds.bottom,
                textPaint);
    }

    private void init(Context context) {
        textPaint.setAntiAlias(true);
        // TODO: refactor text color and font source
        textPaint.setColor(Color.BLACK);

        final float testTextHeight = 100f;
        textPaint.setTextSize(testTextHeight);
        float testTextWidth = textPaint.measureText("0");
        fontRatio = testTextHeight / testTextWidth;
    }

    private MarkdownClickableElement findSeatElement(float x, float y) {
        float markdownX = x / widthScale;
        float markdownY = y / heightScale;
        List<MarkdownElement> overviewElements = viewModel.getLayers().get(ComplexViewModel.OVERVIEW_LAYER);
        for (MarkdownElement element : overviewElements) {
            if (element instanceof MarkdownClickableElement &&
                    insideFrame(markdownX, markdownY, element.getFrame())) {
                return (MarkdownClickableElement) element;
            }
        }
        return null;
    }

    private boolean insideFrame(float markdownX, float markdownY, MarkdownFrame frame) {
        return markdownX >= frame.getX() &&
                markdownX <= frame.getX() + frame.getWidth() &&
                markdownY > frame.getY() &&
                markdownY <= frame.getY() + frame.getHeight();
    }

    public enum State {
        OVERVIEW(0),
        TRANSITION(1),
        DETAILED(1);

        private final int layerNumber;

        State(int layerNumber) {
            this.layerNumber = layerNumber;
        }

        public int getLayerNumber() {
            return layerNumber;
        }
    }

    public interface OnSeatClickListener {
        void onSeatClick(MarkdownClickableElement seatElement);
    }
}
