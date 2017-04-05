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

    public static svm_model trainedModel;

    public SVM(Activity mainAct) {

        if (trainedModel == null) {
            try {
                //Read the trained model from the text file
                InputStream trainedModelFile = mainAct.getResources().openRawResource(R.raw.trained_model);
                String modelData = convertStreamToString(trainedModelFile);
                BufferedReader bufferedModel = new BufferedReader(new StringReader(modelData));

                //Create the svm model object that will be used for predicting
                //Load the trained model for classifying new data points
                trainedModel = svm.svm_load_model(bufferedModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * This method will take in a feature vector and return the String classification
     * that is determined by the SVM model
     */
    public String svmPredict(double[] activityData) {

        svm_node[] nodes = new svm_node[activityData.length];

        for (int i = 0; i < activityData.length; i++) {
            svm_node node = new svm_node();
            node.index = i;
            node.value = activityData[i];
            nodes[i] = node;
        }

        int totalClasses = 3;
        int[] labels = new int[totalClasses];
        svm.svm_get_labels(trainedModel, labels);

        //Get the prediction from the machine
        double[] prob_estimates = new double[totalClasses];
        double prediction = svm.svm_predict_probability(trainedModel, nodes, prob_estimates);

        //Decode the double classification to a string and return it
        String classification;
        switch( String.valueOf( prediction ) ) {
            case "0.0":
                classification = "Eating";
                break;
            case "1.0":
                classification = "Walking";
                break;
            case "2.0":
                classification = "Running";
                break;
            default:
                classification = "Unclear";
                break;
        }
        return classification;
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
