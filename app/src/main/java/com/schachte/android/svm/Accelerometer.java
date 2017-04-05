package com.schachte.android.svm;


/**
 * Created by schachte on 2/19/17.
 */

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import static android.content.Context.SENSOR_SERVICE;

public class Accelerometer implements SensorEventListener {

    private final int ACCE_FILTER_DATA_MINTIME = 100;
    private static final String TAG = "MainActivity";

    private int totalRecords;

    float min_x = Float.POSITIVE_INFINITY;
    float max_x = Float.NEGATIVE_INFINITY;

    private float min_y = Float.POSITIVE_INFINITY;
    private float max_y = Float.NEGATIVE_INFINITY;

    private float min_z = Float.POSITIVE_INFINITY;
    private float max_z = Float.NEGATIVE_INFINITY;

    private float x_sum = 0;
    private float y_sum = 0;
    private float z_sum = 0;

    private Sensor phoneSensor;
    private SensorManager SM;
    private long lastSaved = System.currentTimeMillis();

    private Activity mainAct;
    private SVM supportVectorClassification;

    private TextView et1;
    private TextView activityLabel;

    public Accelerometer(Activity mainAct) {
        //Reference to the main activity
        this.mainAct = mainAct;
        this.et1 = (TextView) this.mainAct.findViewById(R.id.log_data);
        this.activityLabel = (TextView)mainAct.findViewById(R.id.activity_type);
        this.supportVectorClassification = new SVM(mainAct);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if ((System.currentTimeMillis() - lastSaved) > ACCE_FILTER_DATA_MINTIME ) {

            this.lastSaved = System.currentTimeMillis();

            this.totalRecords += 1;

            this.max_x = Math.max(this.max_x, event.values[0]);
            this.min_x = Math.min(this.min_x, event.values[0]);
            this.x_sum += event.values[0];

            this.max_y = Math.max(this.max_y, event.values[1]);
            this.min_y = Math.min(this.min_y, event.values[1]);
            this.y_sum += event.values[1];

            this.max_z = Math.max(this.max_z, event.values[2]);
            this.min_z = Math.min(this.min_z, event.values[2]);
            this.z_sum += event.values[2];


            final int recordCount = 50;
            if (totalRecords == recordCount) {

                double[] dataVals = new double[6];

                dataVals[0] = 10 * (this.max_x - this.min_x);
                dataVals[1] = 10 * (this.max_y - this.min_y);
                dataVals[2] = 10 * (this.max_z - this.min_z);
                dataVals[3] = (this.x_sum/recordCount);
                dataVals[4] = (this.y_sum/recordCount);
                dataVals[5] = (this.z_sum/recordCount);

                this.activityLabel.setText( this.supportVectorClassification.svmPredict( dataVals ) );

                unregisterSensorListener();

                this.totalRecords = 0;
                this.min_x = Float.POSITIVE_INFINITY;
                this.max_x = Float.NEGATIVE_INFINITY;

                this.min_y = Float.POSITIVE_INFINITY;
                this.max_y = Float.NEGATIVE_INFINITY;

                this.min_z = Float.POSITIVE_INFINITY;
                this.max_z = Float.NEGATIVE_INFINITY;

                this.x_sum = 0;
                this.y_sum = 0;
                this.z_sum = 0;
            } else {
                this.et1.setText("LOG: Datapoint " + Integer.toString(totalRecords + 1) + "\n");
            }
        }
    }

    public void registerSensorListener() {
        //Create sensor manager
        this.SM = (SensorManager) this.mainAct.getSystemService(SENSOR_SERVICE);

        //Accelerometer sensor
        this.phoneSensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Register the sensor listener
        this.SM.registerListener(this, this.phoneSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregisterSensorListener() {
        SM.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not going to be using this for now
    }
}