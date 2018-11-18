package com.karpool.karpl_passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class TripActivity extends AppCompatActivity {


    private TextView tripOrigin, tripDestination, tripDriver, tripDate, tripTime, tripPrice, tripSeats, driverRating;
    static private Button tripButton;


    private final static String KEY_TRIP_STATUS = "tripJoined"; // if trip is already joined or if you are viewing a trip
    private final static String KEY_TRIP_FRAG_MODE = "tripMode"; // if a trip is upcoming or already happened
    private final static String KEY_TRIP_DESTINATION = "tripdestination";
    private final static String KEY_TRIP_TIME = "time";
    private final static String KEY_TRIP_DATE = "date";
    private final static String KEY_TRIP_ORIGIN = "triplocation";
    private final static String KEY_TRIP_SEATS = "seats";
    private final static String KEY_TRIP_ID = "tripID";
    private final static String KEY_USER_ID = "userID";
    private final static String KEY_TRIP_PRICE = "tripprice";
    private final static String KEY_TRIP_DRIVER = "driver";

    private static String userID, tripID, tripStatus, tripMode;


    // TODO DRIVER RATING

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
        tripButton = (Button) findViewById(R.id.tripButton);
        tripSeats = (TextView) findViewById(R.id.tripSeats);
        driverRating = (TextView) findViewById(R.id.driverRating);


        tripStatus = prefs.getString(KEY_TRIP_STATUS, null); // JOINED OR VIEW
        tripMode = prefs.getString(KEY_TRIP_FRAG_MODE, null); // PAST OR UPCOMING

        tripID = prefs.getString(KEY_TRIP_ID, null);
        userID = prefs.getString(KEY_USER_ID, null);


        tripDate.setText(prefs.getString(KEY_TRIP_DATE, null));
        tripTime.setText(prefs.getString(KEY_TRIP_TIME, null));
        tripDestination.setText(prefs.getString(KEY_TRIP_DESTINATION, null));
        tripOrigin.setText(prefs.getString(KEY_TRIP_ORIGIN, null));
        tripDriver.setText(prefs.getString(KEY_TRIP_DRIVER, null));
        tripSeats.setText(prefs.getString(KEY_TRIP_SEATS, null));
        tripPrice.setText(prefs.getString(KEY_TRIP_PRICE, null));


        tripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripAction(tripStatus, userID, tripID);
            }
        });

        if(tripMode == null) {
            tripMode = "UPCOMING";
        }

        updateButton(tripStatus, tripMode);


    }





    /**
     * Uploads the button based on the current trip type
     * @param tripStatus whether this is a join or leave button
     * @param tripType whether it is an upcoming or past trip
     */
    public void updateButton(String tripStatus, String tripType) {
        if(tripType.equals("UPCOMING")) {
            if (tripStatus.equals("JOINED")) {
                tripButton.setText("LEAVE TRIP");
            } else {
                tripButton.setText("JOIN TRIP");

            }
        }
        else {
            tripButton.setText("RATE TRIP");
        }
    }


    /**
     * Represents an async processor to either join or leave a trip
     * @param tripStatus
     * @param userID
     * @param tripID
     */
    public void tripAction(String tripStatus, String userID, final String tripID) {
        if (tripStatus.equals("VIEW")) {

            HttpUtils.get("trips/" + tripID + "/add/"  + userID, new RequestParams(), new JsonHttpResponseHandler() {
                @Override
                public void onFinish() {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        // TODO How to check...
                        if (response.getString("name") != null) {
                            Fragment mFragment = null;
                            mFragment = new FragmentOne();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_fragmentholder, mFragment).commit();


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                }

            });


        } else {
            HttpUtils.get("trips/" + "/" + "close" + tripID, new RequestParams(), new JsonHttpResponseHandler() {
                @Override
                public void onFinish() {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getBoolean("response") == true) {
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                }
            });


        }
    }


}
