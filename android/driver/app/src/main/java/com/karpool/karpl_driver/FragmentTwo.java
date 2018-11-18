package com.karpool.karpl_driver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FragmentTwo extends Fragment {


    private RecyclerView.LayoutManager mLayoutManager;

    private List<Trip> upcomingTripsList = new ArrayList<>();
    private List<Trip> pastTripsList = new ArrayList<>();
    private RecyclerView upcomingRecyclerView, pastRecyclerView;
    private TripAdapter upcomingAdapter, pastAdapter;

    private TextView noPastTrips, noUpcomingTrips;

    private String userID;
    // the keys correspond to the modify mappings in the controller
    // todo please fix the mapping names because they are not nice
    private final static String KEY_TRIP_DESTINATION = "tripdestination";
    private final static String KEY_TRIP_TIME = "time";
    private final static String KEY_TRIP_DATE = "date";
    private final static String KEY_TRIP_ORIGIN = "triplocation";
    private final static String KEY_TRIP_SEATS = "seats";
    private final static String KEY_TRIP_PRICE = "tripprice";
    private final static String KEY_TRIP_ID = "tripID";
    private final static String KEY_USER = "userID";
    private final static String KEY_TRIP_FRAG_MODE = "tripMode";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_two, container, false);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userID = prefs.getString(KEY_USER, null);


        upcomingRecyclerView = (RecyclerView) rootView.findViewById(R.id.myTripsRecyclerView);
        pastRecyclerView = (RecyclerView) rootView.findViewById(R.id.myOldTripsRecyclerView);


        noPastTrips = (TextView) rootView.findViewById(R.id.noPastTrips);
        noUpcomingTrips = (TextView) rootView.findViewById(R.id.noUpcomingTrips);



        upcomingAdapter = new TripAdapter(upcomingTripsList);
        pastAdapter = new TripAdapter(pastTripsList);

        RecyclerView.LayoutManager upcomingLayoutManager = new LinearLayoutManager(getActivity());
        upcomingRecyclerView.setLayoutManager(upcomingLayoutManager);
        upcomingRecyclerView.setItemAnimator(new DefaultItemAnimator());
        upcomingRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        upcomingRecyclerView.setAdapter(upcomingAdapter);

        RecyclerView.LayoutManager pastLayoutManager = new LinearLayoutManager(getActivity());
        pastRecyclerView.setLayoutManager(pastLayoutManager);
        pastRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pastRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        pastRecyclerView.setAdapter(pastAdapter);


        // Click listener for trip selection
        upcomingRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), upcomingRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Trip trip = upcomingTripsList.get(position);
                prefs.edit().putString(KEY_TRIP_DATE, trip.getDate()).commit();
                prefs.edit().putString(KEY_TRIP_DESTINATION, trip.getDestination()).commit();
                prefs.edit().putString(KEY_TRIP_ORIGIN, trip.getOrigin()).commit();
                prefs.edit().putString(KEY_TRIP_TIME, trip.getTime()).commit();
                prefs.edit().putString(KEY_TRIP_ID, trip.getTripID()).commit();
                prefs.edit().putString(KEY_TRIP_SEATS, trip.getSeats()).commit();
                prefs.edit().putString(KEY_TRIP_PRICE, trip.getPrice()).commit();
                prefs.edit().putString(KEY_TRIP_FRAG_MODE, "UPCOMING").commit();
                Intent I = new Intent(getActivity(), TripActivity.class);
                startActivity(I);
            }

            @Override
            public void onLongClick(View view, int position) {
                // do nothing
            }
        }));


        pastRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), pastRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Trip trip = pastTripsList.get(position);
                prefs.edit().putString(KEY_TRIP_DATE, trip.getDate()).commit();
                prefs.edit().putString(KEY_TRIP_DESTINATION, trip.getDestination()).commit();
                prefs.edit().putString(KEY_TRIP_ORIGIN, trip.getOrigin()).commit();
                prefs.edit().putString(KEY_TRIP_TIME, trip.getTime()).commit();
                prefs.edit().putString(KEY_TRIP_ID, trip.getTripID()).commit();
                prefs.edit().putString(KEY_TRIP_SEATS, trip.getSeats()).commit();
                prefs.edit().putString(KEY_TRIP_PRICE, trip.getPrice()).commit();
                prefs.edit().putString(KEY_TRIP_FRAG_MODE, "PAST").commit();
                Intent I = new Intent(getActivity(), TripActivity.class);
                startActivity(I);
            }

            @Override
            public void onLongClick(View view, int position) {
                // do nothing
            }
        }));




        displayTrips(userID);


        return rootView;
    }


    /**
     * Represents an asynchronous task to get the trip info
     */

    public void displayTrips(String mUser) {

        HttpUtils.get("trips/drivers/" + mUser, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    upcomingTripsList.clear();
                    pastTripsList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        String date = obj.getString("departureDate");
                        String year = date.substring(0, 4);
                        String remainder = date.substring(4, 8);
                        String time = obj.getString("departureTime");
                        JSONObject driver = obj.getJSONObject("driver");
                        String driverName = driver.getString("name");

                        Boolean tripStatus = obj.getBoolean("tripComplete");


                        if (!tripStatus) {
                            upcomingTripsList.add(new Trip(obj.getString("departureLocation"), obj.getString("destination"), year + "-" + formatter(remainder, "-", 2),
                                    formatter(time, ":", 2), driverName, Integer.toString(obj.getInt("seatAvailable")), Integer.toString(obj.getInt("price")), Integer.toString(obj.getInt("tripId"))));
                        }
                        else {
                            pastTripsList.add(new Trip(obj.getString("departureLocation"), obj.getString("destination"), year + "-" + formatter(remainder, "-", 2),
                                    formatter(time, ":", 2), driverName, Integer.toString(obj.getInt("seatAvailable")), Integer.toString(obj.getInt("price")), Integer.toString(obj.getInt("tripId"))));
                        }

                        upcomingAdapter.notifyDataSetChanged();
                        pastAdapter.notifyDataSetChanged();
                    }

                    if (upcomingTripsList.isEmpty())
                        noUpcomingTrips.setVisibility(View.VISIBLE);

                    if(pastTripsList.isEmpty())
                        noPastTrips.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (upcomingTripsList.isEmpty())
                    noUpcomingTrips.setVisibility(View.VISIBLE);

                if(pastTripsList.isEmpty())
                    noPastTrips.setVisibility(View.VISIBLE);


            }
        });

    }


    /**
     * Used for date and time formatting
     *
     * @param text   the string you want to format
     * @param insert the character to insert
     * @param n      insert every n characters
     * @return
     */
    public static String formatter(
            String text, String insert, int n) {
        StringBuilder builder = new StringBuilder(
                text.length() + insert.length() * (text.length() / n) + 1);

        int index = 0;
        String prefix = "";
        while (index < text.length()) {

            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index,
                    Math.min(index + n, text.length())));
            index += n;
        }
        return builder.toString();
    }
}