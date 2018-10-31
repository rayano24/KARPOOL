package com.karpool.karpl_passenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class TripActivity extends AppCompatActivity {


    // TODO I think i will need to implement this myself once you guys figure out other database stuff since it involves sharedPreferences

    private ImageView driverRating1, driverRating2, driverRating3, driverRating4, driverRating5;
    private TextView tripOrigin, tripDestination, tripDriver, tripDate, tripTime, tripPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);


        tripOrigin = (TextView) findViewById(R.id.tripOrigin);
        tripDestination = (TextView) findViewById(R.id.tripDestination);
        tripDate = (TextView) findViewById(R.id.tripDate);
        tripTime = (TextView) findViewById(R.id.tripTime);
        tripPrice = (TextView) findViewById(R.id.tripPrice);
        tripDriver = (TextView) findViewById(R.id.tripDriver);

        driverRating1 = (ImageView) findViewById(R.id.driverRating1);
        driverRating2 = (ImageView) findViewById(R.id.driverRating2);
        driverRating3 = (ImageView) findViewById(R.id.driverRating3);
        driverRating4 = (ImageView) findViewById(R.id.driverRating4);
        driverRating5 = (ImageView) findViewById(R.id.driverRating5);




    }
}
