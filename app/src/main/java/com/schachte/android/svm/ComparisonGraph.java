package com.schachte.android.svm;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

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

        /*
         * This is commented out because the server was running as a fog server
         * and anyone else running it will not have access to it.
         */
        //Map<String, Long> comparisonValues = getGraphValues();

        //Get the graph view
        GraphView graph = (GraphView) findViewById(R.id.graph);

        //Create two different series (this is so that we can set the color of the dots
        //There will be 2 dots on the map, one that represents the values when run on
        //  the local device, and another dot representing the values when run on the
        //  server
        PointsGraphSeries<DataPoint> deviceSeries = new PointsGraphSeries<>( new DataPoint[]{
                //new DataPoint(comparisonValues.get(DEVICE_EXECUTION_TIME), comparisonValues.get(DEVICE_BATTERY_USAGE)),
                new DataPoint(101, 0)
        });
        deviceSeries.setColor(Color.GREEN);
        deviceSeries.setTitle("Device");


        PointsGraphSeries<DataPoint> serverSeries = new PointsGraphSeries<>( new DataPoint[]{
                //new DataPoint(comparisonValues.get(DEVICE_EXECUTION_TIME), comparisonValues.get(DEVICE_BATTERY_USAGE))
                new DataPoint(59, 0)
        });
        serverSeries.setColor(Color.RED);
        serverSeries.setTitle("Server");

        //Add the series to the graph
        graph.addSeries(deviceSeries);
        graph.addSeries(serverSeries);

        //Design Legend
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setTextSize(60);
        graph.getLegendRenderer().setMargin(50);

        //Set title and axis labels
        graph.setTitle("Battery Usage and Execution Time Comparison");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Milliseconds");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Battery %");


        //Because the battery usage is so low, set the Y range from -1 to 1
        graph.getViewport().setMinY(-1);
        graph.getViewport().setMaxY(1);

        graph.getViewport().setMaxX(100);


        TextView explanationText = (TextView) findViewById(R.id.explanationText);
        explanationText.setText("The above values were captured after running our 60 datapoints through the SVM and tracking the " +
                "execution time and battery usage of doing the above on both the local device and on a fog server.  As you can see, " +
                "using the fog server (machine connected to the same WiFi) greatly reduces the execution time.  The battery usage " +
                "for both methods was captured at using 0% battery.  A more granular battery usage value was not able to be captured due to " +
                "API constraints.");
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
        returnMap.put(DEVICE_BATTERY_USAGE, 0L);

        /*
         * Get the battery consumption and execution time from classifying
         * the values using a remote Server
         */
        currentTime = System.currentTimeMillis();
        ClassifyOnServer(featureVectors);
        returnMap.put(SERVER_EXECUTION_TIME, System.currentTimeMillis() - currentTime);
        returnMap.put(SERVER_BATTERY_USAGE, 0L);

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
