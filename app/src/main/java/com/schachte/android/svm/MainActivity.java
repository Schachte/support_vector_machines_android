package com.schachte.android.svm;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import libsvm.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static svm_model trainedModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream trainedModelFile = getResources().openRawResource(R.raw.trained_model);
        String modelData = convertStreamToString(trainedModelFile);
        BufferedReader bufferedModel = new BufferedReader(new StringReader(modelData));

        try {
            trainedModel = new svm().svm_load_model(bufferedModel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[][] test_data = new double[1][3];
        test_data[0][0] = 13.144;
        test_data[0][1] = -9.8066;
        test_data[0][2] = 486.7;


        double[] ypred = svmPredict(test_data, trainedModel);
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
