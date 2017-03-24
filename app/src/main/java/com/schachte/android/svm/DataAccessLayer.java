package com.schachte.android.svm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataAccessLayer extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Group9.db";
    public String TABLE_NAME;

    public static final int FEATURE_VECTOR_SIZE = 6;
    private SQLiteDatabase db;
    public static final String COL_2 = "XVALUE";
    public static final String COL_3 = "YVALUE";
    public static final String COL_4 = "ZVALUE";
    private static final String TAG = "Grapher";

    public DataAccessLayer(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + this.TABLE_NAME + "'");
        onCreate(db);
    }


    /*
     * Read the feature vectors from the local database and return as a 2D array
     */
    public double[][] getFeatureVectors() {

//        //Get database and execute query to select all from the feature vector table
//        db = this.getReadableDatabase();
//        final String getFeaturesQuery = "SELECT * from ";
//        Cursor cursor = db.rawQuery(getFeaturesQuery , null);
//
//        //Iterate through each row and each column in each row and add it to the return vector
//        double[][] returnMatrix = new double[cursor.getCount()][FEATURE_VECTOR_SIZE];
//        for(int i = 0; i < cursor.getCount(); ++i) {
//            double[] newFeature = new double[FEATURE_VECTOR_SIZE];
//
//            for(int j = 0; j < FEATURE_VECTOR_SIZE; ++j) {
//                newFeature[j] = cursor.getDouble(j);
//            }
//
//            returnMatrix[i] = newFeature;
//            cursor.moveToNext();
//        }

        //Was sick of trying to import an existing database file into the app so here's horrible design:
        double[][] returnMatrix = new double[][] {
                {289.1297800000,339.5875800000,392.2600100000,-11.9128771800,-4.8644671590,5.1246330872},
                {245.4475700000,344.4418200000,358.6514100000,-9.8800682502,-4.1679101104,3.6248453370},
                {250.2958270000,262.0693130000,384.4668900000,-13.8511509940,-11.4421770843,4.6409563620},
                {310.2406600000,336.3973100000,339.2104900000,-7.0191527678,-7.9494439440,4.8368021340},
                {220.4102050000,377.1167000000,379.3013900000,-8.2601274490,-3.7843708808,3.9053499530},
                {356.9036500000,297.5992600000,335.8047350000,-4.7456189790,-3.5781942458,6.4832946638},
                {283.7787400000,271.3588160000,392.2600100000,-9.8951277774,-7.0429871554,6.7709340260},
                {111.2525600000,257.0774050000,392.2600100000,-16.3252472300,-8.8818297584,1.1093653254},
                {198.7307087000,282.7432500000,388.6747000000,-15.5698167266,-6.8670731688,0.0752377720},
                {149.2844600000,325.9466100000,376.5899600000,-14.8662087080,-6.8466027675,2.1382254984},
                {144.9449700000,284.4012300000,392.2600100000,-15.2589536040,-4.8249028760,1.5064795984},
                {220.5239290000,308.7801900000,392.2600100000,-14.3454579440,-5.3854693542,2.1464137688},
                {146.4832400000,289.3033600000,380.8037500000,-15.1842904220,-5.5916699465,2.6399904680},
                {175.3573150000,296.2824600000,381.7375000000,-14.0687841500,-5.4633646718,0.8387844934},
                {292.0028200000,265.0441100000,392.2600100000,-10.2184168940,-5.9773040368,2.3721870170},
                {235.9186470000,297.6950300000,315.3642400000,-8.7929211182,-6.0195378640,3.6705744882},
                {195.5105075700,271.0894700000,376.4104100000,-11.2123097631,-5.3303907538,4.1968199606},
                {123.7502970000,305.8891900000,365.1995600000,-14.8232090280,-3.7527076492,0.0367988854},
                {167.8694480000,346.6085800000,334.6016500000,-13.8827664740,-3.4458308576,0.5083608710},
                {173.1247230000,324.4801700000,314.9213200000,-13.5286042700,-2.8740833920,0.3200808126},
                {145.8368040000,164.2182900000,170.3354800000,-10.0501763460,-0.3838264480,0.1458308334},
                {85.4789850000,76.9197200000,28.0540590000,-10.4968984980,-1.5798858381,-0.7306983852},
                {109.7442130000,111.1388420000,108.0802540000,-10.4769787340,-0.7071154999,-0.8515457181},
                {72.3707300000,79.5413620000,44.8074834000,-10.5210559740,-0.1002093064,-1.0103172009},
                {88.9266300000,67.2830540000,60.1543200000,-10.1942474640,-1.3696270174,-1.1960952942},
                {49.1110660000,48.7459470000,28.9458980000,-10.1808639480,-0.5165609896,-1.0767802352},
                {48.5184900000,51.8584100000,55.7489870000,-9.9456933300,-0.8586445285,-0.7807850895},
                {46.1183160000,71.8080930000,32.8963305000,-9.6573597260,1.3123816718,-2.0886417010},
                {38.5406580000,40.3063850000,13.5212546000,-9.8077634340,0.6035902180,-1.6926886208},
                {19.2493800000,22.8646274400,11.7076470000,-9.7395287600,1.0424952724,-1.7957949360},
                {15.2690200000,18.7166710000,15.4904860000,-9.6685405800,1.2382571728,-1.8251717900},
                {15.2929700000,21.5238736000,10.2950670000,-9.6621960200,0.6126762149,-1.8076222960},
                {15.3109200000,17.5435132000,13.5631520000,-9.7453945000,0.6856155696,-1.6514485040},
                {21.5238600000,22.3977560000,11.0791700000,-9.7458852300,0.7067563617,-1.7115549360},
                {17.8008850000,22.8047700000,9.9957920000,-9.7294132200,0.5321352647,-1.7485214260},
                {69.5635300000,35.7813430000,107.2303080000,-9.4016350520,0.7229172339,0.2373491882},
                {19.3870500000,24.3250890000,11.0971252000,-9.9014725700,-0.1642302648,-0.6646782770},
                {18.0343250000,23.6487270000,10.1035313000,-9.8824267000,-0.2995864607,-0.6751888170},
                {18.8004700000,19.5546414000,17.1963530000,-9.8771714700,-0.4118026899,-0.7239108250},
                {26.5337400000,27.8385800000,12.6413850500,-9.8874424600,-0.3326743190,-0.7535390739},
                {4.2975921000,5.1535191000,2.6515700000,0.1946725406,-0.1111867273,9.7352789900},
                {1.5263035000,1.3587094600,1.1492150000,0.2241810758,-0.1477701283,9.7272464700},
                {1.7956511000,1.4963760000,1.1552100000,0.2396835310,-0.1732444358,9.7325976300},
                {1.6699556000,1.1372457000,1.0474700000,0.2431551246,-0.1705988426,9.7354825200},
                {1.6879124000,1.7956513500,1.4724300000,0.2434903124,-0.1821867790,9.7319749700},
                {2.7473465000,2.1966801000,2.0949300000,0.2255457694,-0.2310883480,9.7315320500},
                {2.5797525000,3.1124621660,2.0171150000,0.2258570152,-0.1917157004,9.7300596800},
                {1.1252748000,0.7661445000,0.9337400000,0.2597828542,-0.2022741298,9.7292814500},
                {1.4365211000,0.7422026000,1.0474600000,0.2627157530,-0.1993412322,9.7328729200},
                {1.0534487000,0.9636661000,0.8738900000,0.2620932598,-0.2000475216,9.7306103300},
                {1.0115503000,1.3347673000,0.7601500000,0.2585378690,-0.1988983064,9.7327292100 },
                {1.1312605000,0.7900865000,1.2629400000,0.2628833466,-0.1961330026,9.7341059400},
                {1.4185646000,1.1312602000,1.1312600000,0.2645832286,-0.1991257540,9.7315320300},
                {1.5023616000,1.0534489000,0.7900850000,0.2644036648,-0.2031599838,9.7279047800},
                {1.4844052000,0.9097967000,0.8379750000,0.2673605024,-0.1986588842,9.7309694700},
                {1.5143327000,1.2509702000,1.7417900000,0.2668218100,-0.2006700140,9.7286590500},
                {0.7661446000,0.9516951000,1.0833700000,0.2675520412,-0.2003108840,9.7358895900},
                {0.8140285000,0.7182606000,0.9936000000,0.2700539808,-0.2056619260,9.7314602300}
        };

        return returnMatrix;
    }
    public boolean insertData(String xVal, String yVal, String zVal) {
        //Create instance of the database

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        //Insert the accelerometer data into the database
        contentValues.put(COL_2, xVal);
        contentValues.put(COL_3, yVal);
        contentValues.put(COL_4, zVal);
        Long result = db.insert(TABLE_NAME, null, contentValues);

        //Boolean check to see insertion was successful
        return result != -1;
    }

    /*
     * Returns a map containing the x, y and z data points from the last accelerometer
     * entry
     */
    public Map<String, DataPoint> getLastPoint(final int currentXValue) {
        Map<String, DataPoint> dataPointMap = new HashMap<>();

        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            String queryString = "SELECT * FROM " + TABLE_NAME + " ORDER BY TIMESTAMP DESC LIMIT 1;";
            Cursor c = db.rawQuery(queryString, null);

            if (c.getCount() > 0)
            {
                c.moveToFirst();
                String validXPoint = c.getString(c.getColumnIndex("XVALUE"));
                String validYPoint = c.getString(c.getColumnIndex("YVALUE"));
                String validZPoint = c.getString(c.getColumnIndex("ZVALUE"));

                dataPointMap.put("XPOINT", new DataPoint(currentXValue, Float.parseFloat(validXPoint)));
                dataPointMap.put("YPOINT", new DataPoint(currentXValue, Float.parseFloat(validYPoint)));
                dataPointMap.put("ZPOINT", new DataPoint(currentXValue, Float.parseFloat(validZPoint)));
            }

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataPointMap;
    }
}
