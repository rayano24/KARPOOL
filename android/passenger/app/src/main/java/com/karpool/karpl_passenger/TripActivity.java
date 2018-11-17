package com.karpool.karpl_passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
    private final static String KEY_PAST_FRAGMENT = "pastFrag";

    private final static String KEY_TRIP_DESTINATION = "tripdestination";
    private final static String KEY_TRIP_TIME = "time";
    private final static String KEY_TRIP_DATE = "date";
    private final static String KEY_TRIP_ORIGIN = "triplocation";
    private final static String KEY_TRIP_SEATS = "seats";
    private final static String KEY_TRIP_ID = "tripID";
    private final static String KEY_USER_ID = "userID";
    private final static String KEY_TRIP_PRICE = "tripprice";
    private final static String KEY_TRIP_DRIVER = "driver";

    private static String userID, tripID, currentFrag;


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


        currentFrag = prefs.getString(KEY_PAST_FRAGMENT, null);
        tripID = prefs.getString(KEY_TRIP_ID, null);
        userID = prefs.getString(KEY_USER_ID, null);


        updateButton(currentFrag);

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
                tripAction(currentFrag, userID, tripID);
                finish();
            }
        });


    }


    public static void updateButton(String currentFrag) {
        if (currentFrag.equals("JOIN")) {
            tripButton.setText("JOIN TRIP");
        } else {
            tripButton.setText("LEAVE TRIP");

        }
    }


    /**
     * Represents an async processor to either join or leave a trip
     * @param currentFrag
     * @param userID
     * @param tripID
     */
    public static void tripAction(String currentFrag, String userID, final String tripID) {
        if (currentFrag.equals("JOIN")) {

            HttpUtils.get("trips/" + tripID + "/" + "add" + "/" + userID, new RequestParams(), new JsonHttpResponseHandler() {
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

                Toast toast;
            });


        } else
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


        {


        }
    }


}
