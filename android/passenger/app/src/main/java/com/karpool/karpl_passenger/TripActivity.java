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


    private ImageView driverRating1, driverRating2, driverRating3, driverRating4, driverRating5;
    private TextView tripOrigin, tripDestination, tripDriver, tripDate, tripTime, tripPrice, tripSeats, tripRating;
    static private Button tripButton;
    private final static String KEY_PAST_FRAGMENT = "pastFrag";

    private final static String KEY_TRIP_DESTINATION = "tripDestination";
    private final static String KEY_TRIP_TIME = "tripTime";
    private final static String KEY_TRIP_DATE = "tripDate";
    private final static String KEY_TRIP_ORIGIN = "tripOrigin";
    private final static String KEY_TRIP_DRIVER = "searchDriver";
    private final static String KEY_TRIP_SEATS = "searchSeats";
    private final static String KEY_TRIP_ID = "tripID";
    private final static String KEY_USER_ID = "userID";
    private final static String KEY_USER_LOCATION = "userLocation";


    private static String userID, currentFrag, tripID, userLocation;


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
        tripButton = (Button) findViewById(R.id.joinButton);
        tripSeats = (TextView) findViewById(R.id.tripSeats);
        tripRating = (TextView) findViewById(R.id.tripRating);


        currentFrag = prefs.getString(KEY_PAST_FRAGMENT, null);
        tripID = prefs.getString(KEY_TRIP_ID, null);
        userID = prefs.getString(KEY_USER_ID, null);
        userLocation = prefs.getString(KEY_USER_LOCATION, null);





        updateButton(currentFrag);
        tripDate.setText(prefs.getString(KEY_TRIP_DATE, null));
        tripTime.setText(prefs.getString(KEY_TRIP_TIME, null));
        tripDestination.setText(prefs.getString(KEY_TRIP_DESTINATION, null));
        tripOrigin.setText(prefs.getString(KEY_TRIP_ORIGIN, null));
        tripDriver.setText(prefs.getString(KEY_TRIP_DRIVER, null));
        tripSeats.setText(prefs.getString(KEY_TRIP_SEATS, null));


        tripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripAction(currentFrag, userID, tripID);

            }
        });


    }


    private static void updateRating(int rating) {

    }

    public static void updateButton(String currentFrag) {
        if (currentFrag.equals("JOIN")) {
            tripButton.setText("JOIN TRIP");
        } else {
            tripButton.setText("DELETE TRIP");

        }
    }


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
            });


        } else

        {




        }
    }



}
