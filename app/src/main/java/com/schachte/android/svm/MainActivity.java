package com.schachte.android.svm;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Accelerometer accel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.accel = new Accelerometer(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Button button = (Button) findViewById(R.id.begin_listener);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accel.registerSensorListener();
            }
        });

        final Button comparisonGraph = (Button) findViewById(R.id.goto_comparison_graph);
        comparisonGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ComparisonGraph.class);
                startActivity(intent);
            }
        });

        final Button accuracyResults = (Button) findViewById(R.id.accuracy_resultsBtn);
        accuracyResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccuracyResults.class);
                startActivity(intent);
            }
        });

        final Button threed_graph = (Button) findViewById(R.id.threed_graph_button);
        threed_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThreeDGraph.class);
                startActivity(intent);
            }
        });
    }



}
