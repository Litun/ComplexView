package io.litun.complexviewdemo.keyboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.litun.complexview.ComplexView;
import io.litun.complexview.ComplexViewModel;
import io.litun.complexview.ResourceCache;
import io.litun.complexview.model.Markup;
import io.litun.complexview.model.MarkupDrawableElement;
import io.litun.complexview.model.MarkupElement;
import io.litun.complexview.model.MarkupFrame;
import io.litun.complexview.model.MarkupTextElement;
import io.litun.complexviewdemo.R;

/**
 * Created by Litun on 21.04.2017.
 */

public class KeyboardActivity extends AppCompatActivity {

    @BindView(R.id.input)
    TextView input;
    @BindView(R.id.keyboard_layout)
    ViewGroup keyboardLayout;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.keyboard)
    ComplexView keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        ButterKnife.bind(this);
        divider.setOnTouchListener(new View.OnTouchListener() {
            float downY = 0;
            int downHeight = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = event.getRawY();
                        downHeight = keyboardLayout.getHeight();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        ViewGroup.LayoutParams layoutParams = keyboardLayout.getLayoutParams();
                        layoutParams.height = (int) (downHeight + downY - event.getRawY());
                        keyboardLayout.setLayoutParams(layoutParams);
                        return true;
                    default:
                        return false;
                }
            }
        });
        new AsyncTask<Void, Void, ComplexViewModel>() {

            @Override
            protected ComplexViewModel doInBackground(Void... params) {
                return new ComplexViewModel.Builder(KeyboardActivity.this)
                        .setSourceFileName("keyboard_markup.json")
                        .setMarkupProcessor(KeyboardActivity::makeMarkup)
                        .build();
            }

            @Override
            protected void onPostExecute(ComplexViewModel complexViewModel) {
                keyboard.setData(complexViewModel);
            }
        }.execute();
    }

    private static Markup makeMarkup(String object, ResourceCache resourceCache) {
        KeyboardMarkupModel keyboardMarkupModel = new Gson().fromJson(object, KeyboardMarkupModel.class);
        float x = keyboardMarkupModel.getX();
        float y = keyboardMarkupModel.getY();
        Markup.Builder builder = new Markup.Builder()
                .setSize(keyboardMarkupModel.getWidth(),
                        keyboardMarkupModel.getHeight());
        for (KeyboardMarkupItem markupItem : keyboardMarkupModel.getItems()) {
            MarkupFrame frame = new MarkupFrame(
                    markupItem.getX() - x,
                    markupItem.getY() - y,
                    markupItem.getWidth(),
                    markupItem.getHeight());
            if (markupItem.getLetter() != null) {
                builder.addElement(0, new MarkupDrawableElement(frame,
                        resourceCache.getDrawable(R.drawable.keyboard_letter_key)));
                builder.addElement(1, new MarkupTextElement(createSmallFrame(frame),
                        markupItem.getLetter()));
            } else if (markupItem.getAction() != null) {
                builder.addElement(0, new MarkupDrawableElement(frame,
                        resourceCache.getDrawable(getKeyBackgroundForAction(markupItem.getAction()))));
                builder.addElement(1, createElementForAction(markupItem.getAction(), frame, resourceCache));
            }
        }
        return builder.build();
    }

    private static MarkupElement createElementForAction(String action, MarkupFrame frame,
                                                        ResourceCache resourceCache) {
        switch (action) {
            case "dot":
                return new MarkupTextElement(createSmallFrame(frame), ".");
            case "coma":
                return new MarkupTextElement(createSmallFrame(frame), ",");
            case "123":
                return new MarkupTextElement(createSmallFrame(frame), "&123");

            case "shift":
                return new MarkupDrawableElement(createSmallFrame(frame),
                        resourceCache.getDrawable(R.drawable.ic_keyboard_up));
            case "enter":
                return new MarkupDrawableElement(createSmallFrame(frame),
                        resourceCache.getDrawable(R.drawable.ic_keyboard_enter));
            case "backspace":
                return new MarkupDrawableElement(createSmallFrame(frame),
                        resourceCache.getDrawable(R.drawable.ic_keyboard_backspace));
            case "smile":
                return new MarkupDrawableElement(createSmallFrame(frame),
                        resourceCache.getDrawable(R.drawable.ic_keyboard_smile));
            default:
                return null;
        }
    }

    private static MarkupFrame createSmallFrame(MarkupFrame frame) {
        return new MarkupFrame(frame.getX() + frame.getWidth() / 4,
                frame.getY() + frame.getHeight() / 4,
                frame.getWidth() / 2,
                frame.getHeight() / 2);

    }

    private static int getKeyBackgroundForAction(String action) {
        switch (action) {
            case "shift":
            case "enter":
            case "123":
            case "backspace":
                return R.drawable.keyboard_action_key;
            case "dot":
            case "coma":
            case "smile":
                return R.drawable.keyboard_extra_key;
            default:
                return R.drawable.keyboard_letter_key;
        }
    }
}
