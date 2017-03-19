package com.schachte.android.svm;


/**
 * Created by schachte on 2/19/17.
 */

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import java.lang.Math;;

import static android.content.Context.SENSOR_SERVICE;

public class Accelerometer implements SensorEventListener {

    private final int ACCE_FILTER_DATA_MINTIME = 100;
    private static final String TAG = "MainActivity";
    private int totalRecords;
    public float x,y,z;

    float min_x = Float.POSITIVE_INFINITY;
    float max_x = Float.NEGATIVE_INFINITY;

    float min_y = Float.POSITIVE_INFINITY;
    float max_y = Float.NEGATIVE_INFINITY;

    float min_z = Float.POSITIVE_INFINITY;
    float max_z = Float.NEGATIVE_INFINITY;

    float x_sum = 0;
    float y_sum = 0;
    float z_sum = 0;


    private Sensor phoneSensor;
    private SensorManager SM;
    long lastSaved = System.currentTimeMillis();
    Activity mainAct;
    SVM supportVectorClassification;
    EditText et1;

    public Accelerometer(Activity mainAct) {

        //Reference to the main activity
        this.mainAct = mainAct;
        et1 = (EditText) this.mainAct.findViewById(R.id.log_data);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if((System.currentTimeMillis() - lastSaved) > ACCE_FILTER_DATA_MINTIME ) {

            lastSaved = System.currentTimeMillis();


            totalRecords += 1;

            max_x = Math.max(max_x, event.values[0]);
            min_x = Math.min(min_x, event.values[0]);
            x_sum += event.values[0];

            max_y = Math.max(max_y, event.values[1]);
            min_y = Math.min(min_y, event.values[1]);
            y_sum += event.values[1];


            max_z = Math.max(max_z, event.values[2]);
            min_z = Math.min(min_z, event.values[2]);
            z_sum += event.values[2];


            int recordCount = 50;
            if (totalRecords == recordCount) {
                Toast.makeText(mainAct, "Completed all  " + Integer.toString(recordCount), Toast.LENGTH_LONG).show();

                float[] dataVals = new float[6];

                dataVals[0] = 10 * (max_x - min_x);
                dataVals[1] = 10 * (max_y - min_y);
                dataVals[2] = 10 * (max_z - min_z);
                dataVals[3] = (x_sum/recordCount);
                dataVals[4] = (y_sum/recordCount);
                dataVals[5] = (z_sum/recordCount);

                supportVectorClassification = new SVM(mainAct, dataVals);

                unregisterSensorListener();
                totalRecords = 0;
                min_x = Float.POSITIVE_INFINITY;
                max_x = Float.NEGATIVE_INFINITY;

                min_y = Float.POSITIVE_INFINITY;
                max_y = Float.NEGATIVE_INFINITY;

                min_z = Float.POSITIVE_INFINITY;
                max_z = Float.NEGATIVE_INFINITY;

                x_sum = 0;
                y_sum = 0;
                z_sum = 0;
            } else {
                et1.setText("LOG: " + Integer.toString(totalRecords) + "\n");
            }
        }
    }

    public void registerSensorListener() {
        //Create sensor manager
        SM = (SensorManager) this.mainAct.getSystemService(SENSOR_SERVICE);

        //Accelerometer sensor
        phoneSensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Register the sensor listener
        SM.registerListener(this, phoneSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregisterSensorListener() {
        SM.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not going to be using this for now
    }
}