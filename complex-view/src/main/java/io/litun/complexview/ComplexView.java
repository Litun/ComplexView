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

import io.litun.complexview.model.MarkupDrawableElement;
import io.litun.complexview.model.MarkupElement;
import io.litun.complexview.model.MarkupFrame;
import io.litun.complexview.model.MarkupClickableElement;
import io.litun.complexview.model.MarkupTextElement;

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

    private OnSeatClickListener seatClickListener;
    private ComplexViewModel viewModel;
    private float widthScale = 1f;
    private float heightScale = 1f;
    private State state = State.DETAILED;
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

    private MarkupClickableElement touchingSeat;

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
            widthScale = getWidth() / viewModel.getMarkup().getWidth();
            heightScale = getHeight() / viewModel.getMarkup().getHeight();
//            viewModel.onLayout(getWidth(), getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (viewModel != null) {
            List<List<MarkupElement>> layers = viewModel.getMarkup().getLayers();
            drawLayer(canvas, layers.get(State.OVERVIEW.getLayerNumber()), 1f);
            if (state == State.DETAILED) {
                drawLayer(canvas, layers.get(State.DETAILED.getLayerNumber()), 1f);
            } else if (state == State.TRANSITION) {
                drawLayer(canvas, layers.get(State.DETAILED.getLayerNumber()), transitionProgress);
            }
        }
    }

    private void drawLayer(Canvas canvas, List<MarkupElement> layerElements, float alpha) {
        for (int j = 0; j < layerElements.size(); j++) {
            MarkupElement element = layerElements.get(j);
            drawElement(canvas, element, alpha);
        }
    }

    private void drawElement(Canvas canvas, MarkupElement element, float alpha) {
        if (element instanceof MarkupDrawableElement) {
            drawInFrame(canvas, ((MarkupDrawableElement) element).getDrawable(),
                    element.getFrame(), alpha);
        } else if (element instanceof MarkupTextElement) {
            drawInFrame(canvas, ((MarkupTextElement) element).getText(), element.getFrame(),
                    alpha);
        }
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

    private MarkupClickableElement findSeatElement(float x, float y) {
        float markupX = x / widthScale;
        float markupY = y / heightScale;
        List<MarkupElement> overviewElements = viewModel.getMarkup().getLayers().get(0);
        for (MarkupElement element : overviewElements) {
            if (element instanceof MarkupClickableElement &&
                    insideFrame(markupX, markupY, element.getFrame())) {
                return (MarkupClickableElement) element;
            }
        }
        return null;
    }

    private boolean insideFrame(float markupX, float markupY, MarkupFrame frame) {
        return markupX >= frame.getX() &&
                markupX <= frame.getX() + frame.getWidth() &&
                markupY > frame.getY() &&
                markupY <= frame.getY() + frame.getHeight();
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
        void onSeatClick(MarkupClickableElement seatElement);
    }
}
