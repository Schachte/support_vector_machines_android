package com.schachte.android.svm;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

/**
 * Created by schachte on 3/18/17.
 */


public class SVM {

    Activity mainAct;
    private static final String TAG = "MainActivity";
    public static svm_model trainedModel;
    public float[] activitySum;

    public SVM(Activity mainAct, float[] activitySum) {

        this.activitySum = activitySum;

        InputStream trainedModelFile = mainAct.getResources().openRawResource(R.raw.trained_model);
        String modelData = convertStreamToString(trainedModelFile);
        BufferedReader bufferedModel = new BufferedReader(new StringReader(modelData));

        try {
            //Load the trained model for classifying new data points
            trainedModel = new svm().svm_load_model(bufferedModel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[][] test_data = new double[1][6];
        test_data[0][0] = activitySum[0];
        test_data[0][1] = activitySum[1];
        test_data[0][2] = activitySum[2];
        test_data[0][3] = activitySum[3];
        test_data[0][4] = activitySum[4];
        test_data[0][5] = activitySum[5];


        double[] ypred = svmPredict(test_data, trainedModel);
        TextView tv1 = (TextView)mainAct.findViewById(R.id.activity_type);

        Toast.makeText(mainAct, Double.toString(test_data[0][0]) +
                " " + Double.toString(test_data[0][1]) +
                " " + Double.toString(test_data[0][2]) +
                " " + Double.toString(test_data[0][3]) +
                " " + Double.toString(test_data[0][4]) +
                " " + Double.toString(test_data[0][5]) +

                " " , Toast.LENGTH_LONG).show();

        switch(String.valueOf(ypred[0])) {
            case "0.0":
                tv1.setText("Eating");
                break;
            case "1.0":
                tv1.setText("Walking");
                break;
            case "2.0":
                tv1.setText("Running");
                break;
            default:
                tv1.setText("Unclear");
                break;
        }
    }

    public static double[] svmPredict(double[][] xtest, svm_model model) {

        double[] yPred = new double[xtest.length];

        for (int k = 0; k < xtest.length; k++) {
            double[] fVector = xtest[k];

            svm_node[] nodes = new svm_node[fVector.length];

            for (int i = 0; i < fVector.length; i++) {
                svm_node node = new svm_node();
                node.index = i;
                node.value = fVector[i];
                nodes[i] = node;
            }

            int totalClasses = 3;
            int[] labels = new int[totalClasses];
            svm.svm_get_labels(model, labels);

            double[] prob_estimates = new double[totalClasses];
            yPred[k] = svm.svm_predict_probability(model, nodes, prob_estimates);
        }

        return yPred;

    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
