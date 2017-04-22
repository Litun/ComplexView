package io.litun.complexviewdemo.keyboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.litun.complexview.ComplexView;
import io.litun.complexview.ComplexViewModel;
import io.litun.complexview.ResourceCache;
import io.litun.complexview.model.Markdown;
import io.litun.complexview.model.MarkdownElement;
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
                return new ComplexViewModel.Builder()
                        .setSourceFileName("keyboard_markdown.json")
                        .setMarkdownProcessor(KeyboardActivity::makeMarkdown)
                        .build();
            }
        }.execute();
    }

    private static Markdown makeMarkdown(String object, ResourceCache resourceCache) {
        KeyboardMarkdownModel keyboardMarkdownModel = new Gson().fromJson(object, KeyboardMarkdownModel.class);
        return null;
    }
}
