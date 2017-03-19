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

import static android.content.Context.SENSOR_SERVICE;

public class Accelerometer implements SensorEventListener {

    private static final String TAG = "MainActivity";

    static int ACCE_FILTER_DATA_MIN_TIME = 0; // 3000ms
    private int totalRecords;
    public float x,y,z;

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
//        if ((System.currentTimeMillis() - lastSaved) > ACCE_FILTER_DATA_MIN_TIME) {
            lastSaved = System.currentTimeMillis();
            totalRecords+=1;

            x+=event.values[0];
            y+=event.values[1];
            z+=event.values[2];

            if (totalRecords == 50) {
                Toast.makeText(mainAct, "Completed all 150 records" , Toast.LENGTH_LONG).show();
                Toast.makeText(mainAct, Float.toString(x) + " " + Float.toString(y) + " " + Float.toString(z), Toast.LENGTH_LONG).show();

                float[] dataVals = new float[3];
                dataVals[0] = x;
                dataVals[1] = y;
                dataVals[2] = z;

                supportVectorClassification = new SVM(mainAct, dataVals);

                unregisterSensorListener();
                totalRecords = 0;
                x=0;
                y=0;
                z=0;
                et1.setText("" + "\n");
            }
            else {
                et1.append("LOG: " + Integer.toString(totalRecords) + "\n");
            }
//        }
    }

    public void registerSensorListener() {
        //Create sensor manager
        SM = (SensorManager) this.mainAct.getSystemService(SENSOR_SERVICE);

        //Accelerometer sensor
        phoneSensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Register the sensor listener
        SM.registerListener(this, phoneSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensorListener() {
        SM.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not going to be using this for now
    }
}