package com.karpool.karpl_passenger;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;


/**
 * Displays details of a specific trip and gives the user options to message a driver, leave a trip, or rate it.
 */
public class TripActivity extends AppCompatActivity {


    private TextView tripOrigin, tripDestination, tripDriver, tripDate, tripTime, tripPrice, tripSeats, tripDriverRating;
    static private Button tripButton, messageButton, rateButton;


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
    private final static String KEY_TRIP_DRIVER_NUMBER = "number";
    private final static String KEY_TRIP_DRIVER_RATING = "rating";
    private final static String KEY_TRIP_DRIVER = "driver";

    private static String userID, tripStatus, tripMode, driverNumber, driverName, driverRating;
    private static Integer tripID;
    protected static double userRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // main UI elements

        tripOrigin = findViewById(R.id.tripOrigin);
        tripDestination = findViewById(R.id.tripDestination);
        tripDate = findViewById(R.id.tripDate);
        tripTime = findViewById(R.id.tripTime);
        tripPrice = findViewById(R.id.tripPrice);
        tripDriver = findViewById(R.id.tripDriver);
        tripSeats = findViewById(R.id.tripSeats);
        tripDriverRating = findViewById(R.id.driverRating);
        tripButton = findViewById(R.id.tripButton);
        rateButton = findViewById(R.id.rateButton);
        messageButton = findViewById(R.id.messageButton);


        tripStatus = prefs.getString(KEY_TRIP_STATUS, null); // JOINED OR VIEW
        tripMode = prefs.getString(KEY_TRIP_FRAG_MODE, null); // PAST OR UPCOMING
        driverNumber = prefs.getString(KEY_TRIP_DRIVER_NUMBER, null);
        driverName = prefs.getString(KEY_TRIP_DRIVER, null);
        driverRating = prefs.getString(KEY_TRIP_DRIVER_RATING, null);

        tripID = Integer.parseInt(prefs.getString(KEY_TRIP_ID, null));
        userID = prefs.getString(KEY_USER_ID, null);

        // updating data based on preferences set in fragment 2 (trips fragment)

        tripDate.setText(prefs.getString(KEY_TRIP_DATE, null));
        tripTime.setText(prefs.getString(KEY_TRIP_TIME, null));
        tripDestination.setText(prefs.getString(KEY_TRIP_DESTINATION, null));
        tripOrigin.setText(prefs.getString(KEY_TRIP_ORIGIN, null));
        tripDriver.setText(driverName);
        tripSeats.setText(prefs.getString(KEY_TRIP_SEATS, null));
        tripPrice.setText("$" + prefs.getString(KEY_TRIP_PRICE, null));

        // formatting rating so that trailing zeroes do not show

        DecimalFormat df = new DecimalFormat("###.#");
        if (driverRating.equals("NaN")) {
            tripDriverRating.setText("No Rating ");
        } else {
            tripDriverRating.setText(df.format(Double.parseDouble(driverRating)) + "/5");
        }


        // to leave a trip

        tripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripAction();
            }
        });


        // message driver, opens intent for SMS

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                        + driverNumber)));
            }
        });


        // rate the driver, opens a dialog that allows them to rate the driver by filling stars

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();


            }
        });


        updateButton(tripStatus, tripMode);


    }


    /**
     * Represents an asynchronous task where there are three possibilities based on the static variable tripStatus.
     * If a user has not yet joined a trip, this will join a trip.
     * If a user has already joined a trip, but it has not occurred, this will leave a trip.
     * If a user has already joined a trip and it has already occurred, this will allow the user to rate the trip.
     */
    public void tripAction() {

        // as you have not joined the trip yet, you will want to view the trip. The only possible (network) option is to join a trip.

        if (tripStatus.equals("VIEW")) {

            HttpUtils.post("trips/" + tripID + "/add/" + userID, new RequestParams(), new JsonHttpResponseHandler() {
                @Override
                public void onFinish() {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getString("name") != null) {
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(TripActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error


                }

            });
        } else if (tripStatus.equals("JOINED")) {

            // Since we've already joined the trip, a button action here will be to leave the trip

            if (tripMode.equals("UPCOMING")) {

                HttpUtils.post("trips/" + tripID + "/remove/" + userID, new RequestParams(), new JsonHttpResponseHandler() {
                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getString("name") != null) {
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(TripActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

                    }
                });
            }

            // since it is a trip that has already occurred, you will want to rate the driver

            else {


                HttpUtils.post("drivers/rate/" + driverName + "/" + userRating, new RequestParams(), new JsonHttpResponseHandler() {
                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getJSONArray("ratings").length() != 0) {
                                finish();
                                startActivity(getIntent());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(TripActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

                    }
                });

            }


        }
    }


    /**
     * Uploads the button based on the current trip type
     *
     * @param tripStatus whether this is a join or leave button
     * @param tripType   whether it is an upcoming or past trip
     */
    public void updateButton(String tripStatus, String tripType) {
        if (tripType.equals("UPCOMING")) {
            messageButton.setVisibility(View.VISIBLE);
            tripButton.setVisibility(View.VISIBLE);
            rateButton.setVisibility(View.GONE);
            if (tripStatus.equals("JOINED")) {
                tripButton.setText("LEAVE TRIP");
            } else {
                tripButton.setText("JOIN TRIP");

            }
        } else {
            messageButton.setVisibility(View.GONE);
            tripButton.setVisibility(View.GONE);
            rateButton.setVisibility(View.VISIBLE);
        }
    }


    /**
     * This method displays a pop up with the option to rate a driver by inflating a linear layout.
     * It also calls an async task to update the drivers rating.
     */
    public void showRatingDialog() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);

        final Space horizontalSpace = new Space(this);
        final Space verticalSpace = new Space(this);
        final RatingBar rating = new RatingBar(this);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout horizontalLayout = new LinearLayout(this);
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams verticalSpacelp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        LinearLayout.LayoutParams horizontalSpacelp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        LinearLayout.LayoutParams ratinglp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );


        horizontalSpace.setLayoutParams(horizontalSpacelp);
        horizontalSpace.getLayoutParams().width = 30;

        verticalSpace.setLayoutParams(verticalSpacelp);
        verticalSpace.getLayoutParams().height = 30;

        rating.setLayoutParams(ratinglp);
        rating.setNumStars(5);
        rating.setStepSize(1);

        linearLayout.addView(verticalSpace);
        linearLayout.addView(horizontalLayout);
        horizontalLayout.addView((horizontalSpace));
        horizontalLayout.addView(rating);


        popDialog.setTitle("Rate your driver ");
        popDialog.setView(linearLayout);


        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            }
        });


        // Button OK
        popDialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        userRating = rating.getRating();
                        tripAction();
                        dialog.dismiss();
                    }

                })

                // Button Cancel
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        popDialog.create();
        popDialog.show();
    }


}
