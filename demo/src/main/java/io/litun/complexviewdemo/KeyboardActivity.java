package io.litun.complexviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.litun.complexview.ComplexView;

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
    }
}
