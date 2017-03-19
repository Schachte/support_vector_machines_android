package com.schachte.android.svm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Accelerometer accel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accel = new Accelerometer(this);

        final Button button = (Button) findViewById(R.id.begin_listener);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accel.registerSensorListener();
            }
        });
    }
}
