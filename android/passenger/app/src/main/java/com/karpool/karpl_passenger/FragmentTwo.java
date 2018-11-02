package com.karpool.karpl_passenger;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FragmentTwo extends Fragment {


    private RecyclerView.LayoutManager mLayoutManager;

    private List<Trip> tripsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private searchAdapter mAdapter;

    private TextView noTrips;

    private final static String KEY_USER = "userID";
    private final static String KEY_PAST_FRAGMENT = "pastFrag";


    private final static String KEY_LOCATION = "userLocation";
    private final static String KEY_TRIP_DESTINATION = "tripDestination";
    private final static String KEY_TRIP_TIME= "tripTime";
    private final static String KEY_TRIP_DATE = "tripDate";
    private final static String KEY_TRIP_ORIGIN = "tripOrigin";
    private final static String KEY_TRIP_DRIVER = "searchDriver";
    private final static String KEY_TRIP_SEATS = "searchSeats";
    private final static String KEY_TRIP_ID = "tripID";





    private String userID;
    // TODO The logic I am going for here: When the user logs in, their account ID will be saved through sharedPreferences (will add this when you guys figure out databases). This will be used for loading their trips.



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_two, container, false);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userID = prefs.getString(KEY_USER, null);
        prefs.edit().putString(KEY_PAST_FRAGMENT, "JOIN").commit();




        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.myTripsRecyclerView);
        noTrips = (TextView) rootView.findViewById(R.id.noTrips);




        mAdapter = new searchAdapter(tripsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);



        // Click listener for trip selection
        // TODO open and populate the trip activity when this is selected
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Trip trip = tripsList.get(position);
                prefs.edit().putString(KEY_TRIP_DATE, trip.getDate()).commit();
                prefs.edit().putString(KEY_TRIP_DESTINATION, trip.getDestination()).commit();
                prefs.edit().putString(KEY_TRIP_ORIGIN, trip.getOrigin()).commit();
                prefs.edit().putString(KEY_TRIP_TIME, trip.getTime()).commit();
                prefs.edit().putString(KEY_TRIP_DRIVER, trip.getDriver()).commit();
                prefs.edit().putString(KEY_TRIP_ID, trip.getTripID()).commit();
                prefs.edit().putString(KEY_TRIP_SEATS, trip.getSeats()).commit();
                Intent I = new Intent(getActivity(), TripActivity.class);
                startActivity(I);


            }

            @Override
            public void onLongClick(View view, int position) {
                // do nothing
            }
        }));


        prepareUserTripData(userID);


        return rootView;
    }

    /**
     * Updates the trip information
     */
    private void prepareUserTripData(String user) {
        // TODO Same logic as fragment 1 but we are just adding trips based on UserID
        tripsList.clear();

        HttpUtils.get("trips/" + userID, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.getString("departureLocation").isEmpty()) {
                        noTrips.setVisibility(View.VISIBLE);
                    }
                    else {
                        String date = response.getString("departureDate");
                        String year = date.substring(0, 4);
                        String remainder = date.substring(4,8);
                        String time = response.getString("departureTime");
                        tripsList.add(new Trip(response.getString("departureLocation"), response.getString("destination"), year + "-" + formatter(remainder, "-", 2),
                                formatter(time, ":", 2), response.getString("driver"), response.getString("seatAvailable"), response.getString("tripId")));
                        noTrips.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });




        mAdapter.notifyDataSetChanged();
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