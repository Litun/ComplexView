package io.litun.complexviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.train_schemes)
    void openTrainSchemes() {
        Intent intent = new Intent(this, TrainSchemesActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.keyboard)
    void openKeyboard() {
        Intent intent = new Intent(this, KeyboardActivity.class);
        startActivity(intent);
    }
}
