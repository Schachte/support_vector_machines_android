package com.schachte.android.svm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ComparisonGraph extends AppCompatActivity {

    private final String DEVICE_EXECUTION_TIME = "deviceExecutionTime";
    private final String DEVICE_BATTERY_USAGE = "deviceBatteryUsage";
    private final String SERVER_EXECUTION_TIME = "serverExecutionTime";
    private final String SERVER_BATTERY_USAGE = "serverBatteryUsage";
    private final String ENDPOINT_URL = "http://192.168.1.5:8080/classifyMultiple";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison_graph);
        Map<String, Long> comparisonValues = getGraphValues();
        System.out.println(comparisonValues.toString());
    }


    public Map<String, Long> getGraphValues() {
        Map<String, Long> returnMap = new HashMap<>();
        DataAccessLayer dal = new DataAccessLayer(this);
        double[][] featureVectors = dal.getFeatureVectors();

        /*
         * Get the battery consumption and execution time from classifying
         * the values on the local Android device
         */
        long currentTime = System.currentTimeMillis();
        ClassifyOnDevice(featureVectors);
        returnMap.put(DEVICE_EXECUTION_TIME, System.currentTimeMillis() - currentTime);

        /*
         * Get the battery consumption and execution time from classifying
         * the values using a remote Server
         */
        currentTime = System.currentTimeMillis();
        ClassifyOnServer(featureVectors);
        returnMap.put(SERVER_EXECUTION_TIME, System.currentTimeMillis() - currentTime);


        return returnMap;
    }

    /*
     * Throw all of the values through the SVM to see how long it takes to classify them
     * and the battery usage
     */
    private void ClassifyOnDevice(double[][] featureVectors) {
        SVM svm = new SVM(ComparisonGraph.this);

        for(int i = 0; i < featureVectors.length; i++) {
            svm.svmPredict(featureVectors[i]);
        }
    }

    private void ClassifyOnServer(double[][] featureVectors) {

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(int i = 0; i < featureVectors.length; i++)
            builder.append(featureVectors[i].toString());

        String anotherText = Arrays.deepToString(featureVectors);
        try {
            URL url = new URL(ENDPOINT_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.getOutputStream().write( anotherText.getBytes() );

            InputStream serverResponseStream;
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                serverResponseStream = connection.getErrorStream();
            } else {
                serverResponseStream = connection.getInputStream();
            }

            String line;
            BufferedReader bfr = new BufferedReader(new InputStreamReader(serverResponseStream));
            final StringBuilder outputBuilder = new StringBuilder();
            while ((line = bfr.readLine()) != null) {
                outputBuilder.append(line);
            }
            bfr.close();


        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
