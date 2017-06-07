package io.litun.complexviewdemo.keyboard;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.litun.complexview.ComplexView;
import io.litun.complexview.ComplexViewModel;
import io.litun.complexview.ResourceCache;
import io.litun.complexview.model.Markup;
import io.litun.complexview.model.Markup2;
import io.litun.complexview.model.MarkupDrawableElement;
import io.litun.complexview.model.MarkupDrawableElement2;
import io.litun.complexview.model.MarkupElement;
import io.litun.complexview.model.MarkupElement2;
import io.litun.complexview.model.MarkupFrame;
import io.litun.complexview.model.MarkupFrame2;
import io.litun.complexview.model.MarkupTextElement;
import io.litun.complexview.model.MarkupTextElement2;
import io.litun.complexview.model.ScaleMode;
import io.litun.complexviewdemo.R;

/**
 * Created by Litun on 21.04.2017.
 */

public class KeyboardActivity extends AppCompatActivity {

    public static final float QUARTER = 0.25f;

    @BindView(R.id.input)
    TextView input;
    @BindView(R.id.keyboard_layout)
    ViewGroup keyboardLayout;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.keyboard)
    ComplexView keyboard;
    @BindView(R.id.spinner)
    Spinner spinner;

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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.scale_modes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                keyboard.setScaleMode(ScaleMode.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        keyboard.setFrameClickListener(new ComplexView.OnFrameClickListener() {
            @Override
            public void onSeatClick(MarkupFrame2 frame) {
                Toast.makeText(KeyboardActivity.this, String.valueOf(frame.getMetadata()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static Markup2 makeMarkup(String object, ResourceCache resourceCache) {
        KeyboardMarkupModel keyboardMarkupModel = new Gson().fromJson(object, KeyboardMarkupModel.class);
        float x = keyboardMarkupModel.getX();
        float y = keyboardMarkupModel.getY();
        Markup2.Builder builder = new Markup2.Builder()
                .setSize(keyboardMarkupModel.getWidth(),
                        keyboardMarkupModel.getHeight());
        for (KeyboardMarkupItem markupItem : keyboardMarkupModel.getItems()) {
            MarkupFrame2.Builder frameBuilder = new MarkupFrame2.Builder(
                    markupItem.getX() - x,
                    markupItem.getY() - y,
                    markupItem.getWidth(),
                    markupItem.getHeight());
            if (markupItem.getLetter() != null) {
                frameBuilder.addElement(new MarkupDrawableElement2(0,
                        resourceCache.getDrawable(R.drawable.keyboard_letter_key),
                        null));
                frameBuilder.addElement(createLetterElement(1, markupItem.getLetter()));
                frameBuilder.setMetadata(markupItem.getLetter());
            } else if (markupItem.getAction() != null) {
                frameBuilder.addElement(new MarkupDrawableElement2(0,
                        resourceCache.getDrawable(getKeyBackgroundForAction(markupItem.getAction())),
                        null));
                frameBuilder.addElement(createElementForAction(markupItem.getAction(), 1, resourceCache));
            }
            builder.addFrame(frameBuilder.build());
        }
        return builder.build();
    }

    private static MarkupElement2 createLetterElement(int layer, String letter) {
        return new MarkupTextElement2(QUARTER, QUARTER, QUARTER, QUARTER, layer, ScaleMode.INSCRIBE, letter);
    }

    private static MarkupElement2 createElementForAction(String action, int layer,
                                                         ResourceCache resourceCache) {
        MarkupElement2 result = null;
        String s = null;
        Drawable d = null;
        switch (action) {
            case "dot":
                s = ".";
                break;
            case "coma":
                s = ",";
                break;
            case "123":
                s = "&123";
                break;

            case "shift":
                d = resourceCache.getDrawable(R.drawable.ic_keyboard_up);
                break;
            case "enter":
                d = resourceCache.getDrawable(R.drawable.ic_keyboard_enter);
                break;
            case "backspace":
                d = resourceCache.getDrawable(R.drawable.ic_keyboard_backspace);
                break;
            case "smile":
                d = resourceCache.getDrawable(R.drawable.ic_keyboard_smile);
                break;
            default:
                return null;
        }

        if (s != null) {
            result = new MarkupTextElement2(QUARTER, QUARTER, QUARTER, QUARTER,
                    layer, ScaleMode.INSCRIBE, s);
        } else if (d != null) {
            result = new MarkupDrawableElement2(QUARTER, QUARTER, QUARTER, QUARTER,
                    layer, ScaleMode.INSCRIBE, d, null);
        }

        return result;
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
