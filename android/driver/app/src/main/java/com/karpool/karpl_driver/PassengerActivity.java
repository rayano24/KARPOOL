package com.karpool.karpl_driver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PassengerActivity extends Activity {

    private static String tripID;
    private final static String KEY_TRIP_ID = "tripID";

    private List<Passenger> passengerList = new ArrayList<>();
    private RecyclerView passengerRecyclerView;
    private PassengerAdapter passengerAdapter;

    TextView noPassengersJoined;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        tripID = prefs.getString(KEY_TRIP_ID, null);

        passengerRecyclerView = findViewById(R.id.passengersRecyclerView);
        noPassengersJoined = findViewById(R.id.noPassengersJoined);



        passengerAdapter = new PassengerAdapter(passengerList);

        RecyclerView.LayoutManager upcomingLayoutManager = new LinearLayoutManager(PassengerActivity.this);
        passengerRecyclerView.setLayoutManager(upcomingLayoutManager);
        passengerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        passengerRecyclerView.addItemDecoration(new DividerItemDecoration(PassengerActivity.this, LinearLayoutManager.VERTICAL));
        passengerRecyclerView.setAdapter(passengerAdapter);

        displayPassengersTask();



    }

    /**
     * Represents an async task to display the trip
     *
     */
    public void displayPassengersTask() {

        HttpUtils.get("trips/" + tripID + "/passengers", new RequestParams(), new JsonHttpResponseHandler() {


            @Override
            public void onFinish() {
                passengerList.clear();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // TODO
                passengerList.clear();

                try {
                    for(int i = 0; i < response.length(); i++) {
                        JSONObject passenger = response.getJSONObject(i);
                        String passengerNumber = passenger.getString("phoneNumber");
                        String areaCode = passengerNumber.substring(0, 3);
                        String firstHalfNumber = passengerNumber.substring(3, 6);
                        String secondHalfNumber = passengerNumber.substring(6, 10);
                        String formattedNumber = "(" + areaCode + ") " + firstHalfNumber + "-" + secondHalfNumber;

                        passengerList.add(new Passenger(passenger.getString("name"), formattedNumber));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(passengerList.size() == 0) {
                    noPassengersJoined.setVisibility(View.VISIBLE);

                }

                passengerAdapter.notifyDataSetChanged();


            }

        });
    }




}
