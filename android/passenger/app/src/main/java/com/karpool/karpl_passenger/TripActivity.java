package com.karpool.karpl_passenger;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TripActivity extends AppCompatActivity {



    private ImageView driverRating1, driverRating2, driverRating3, driverRating4, driverRating5;
    private TextView tripOrigin, tripDestination, tripDriver, tripDate, tripTime, tripPrice;
    static private Button tripButton;
    private final static String KEY_PAST_FRAGMENT = "pastFrag";

    private final static String KEY_TRIP_DESTINATION = "tripDestination";
    private final static String KEY_TRIP_TIME= "tripTime";
    private final static String KEY_TRIP_DATE = "tripDate";
    private final static String KEY_TRIP_ORIGIN = "tripOrigin";
    private final static String KEY_TRIP_DRIVER = "searchDriver";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);




        tripOrigin = (TextView) findViewById(R.id.tripOrigin);
        tripDestination = (TextView) findViewById(R.id.tripDestination);
        tripDate = (TextView) findViewById(R.id.tripDate);
        tripTime = (TextView) findViewById(R.id.tripTime);
        tripPrice = (TextView) findViewById(R.id.tripPrice);
        tripDriver = (TextView) findViewById(R.id.tripDriver);
        tripButton = (Button)  findViewById(R.id.joinButton);

        driverRating1 = (ImageView) findViewById(R.id.driverRating1);
        driverRating2 = (ImageView) findViewById(R.id.driverRating2);
        driverRating3 = (ImageView) findViewById(R.id.driverRating3);
        driverRating4 = (ImageView) findViewById(R.id.driverRating4);
        driverRating5 = (ImageView) findViewById(R.id.driverRating5);

        updateButton(prefs.getString(KEY_PAST_FRAGMENT, null));
        tripDate.setText(prefs.getString(KEY_TRIP_DATE, null));
        tripTime.setText(prefs.getString(KEY_TRIP_TIME, null));
        tripDestination.setText(prefs.getString(KEY_TRIP_DESTINATION, null));
        tripOrigin.setText(prefs.getString(KEY_TRIP_ORIGIN, null));
        tripDriver.setText(prefs.getString(KEY_TRIP_DRIVER, null));
        //tripSeats.setText(prefs.getString(KEY_TRIP_ORIGIN, null));






    }


    private static void updateRating(int rating) {

    }

    public static void updateButton(String currentFrag) {
        if(currentFrag.equals("JOIN")) {
            tripButton.setText("JOIN TRIP");
        }
        else {
            tripButton.setText("DELETE TRIP");

        }
    }
}
