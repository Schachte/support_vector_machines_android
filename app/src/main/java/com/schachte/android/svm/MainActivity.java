package com.schachte.android.svm;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Accelerometer accel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accel = new Accelerometer(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Button button = (Button) findViewById(R.id.begin_listener);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accel.registerSensorListener();
                getBatteryCapacity();

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

        Button accuracyResults = (Button) findViewById(R.id.accuracy_resultsBtn);
        accuracyResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccuracyResults.class);
                startActivity(intent);
            }
        });
    }

    /*
     * NOTE: This doesn't work.
     */
    public void getBatteryCapacity() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        long energy = batteryStatus.getIntExtra("voltage", -1);
        BatteryManager batteryManager = (BatteryManager)this.getSystemService(Context.BATTERY_SERVICE);
        //long energy = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
        long amps = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);

        Toast.makeText(MainActivity.this, energy + " nanowatts " + amps + " milliamps",
                Toast.LENGTH_LONG).show();
    }
}
