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

    private List<Trip> tripsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TripAdapter mAdapter;

    private TextView noTrips;

    private String userID;
    private final static String KEY_TRIP_DESTINATION = "tripDestination";
    private final static String KEY_TRIP_TIME = "tripTime";
    private final static String KEY_TRIP_DATE = "tripDate";
    private final static String KEY_TRIP_ORIGIN = "tripOrigin";
    private final static String KEY_TRIP_SEATS = "searchSeats";
    private final static String KEY_TRIP_ID = "tripID";
    private final static String KEY_USER = "userID";

    private DisplayTripTask mDisplayTripTask = null;
    private boolean retrievalSuccess;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_two, container, false);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userID = prefs.getString(KEY_USER, null);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.myTripsRecyclerView);
        noTrips = (TextView) rootView.findViewById(R.id.noTrips);


        mAdapter = new TripAdapter(tripsList);
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


        mDisplayTripTask = new DisplayTripTask(userID);
        mDisplayTripTask.execute((Void) null);


        return rootView;
    }


    /**
     * Represents an asynchronous task to get the trip info
     */
    public class DisplayTripTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUser; //change to username


        DisplayTripTask(String userID) {
            mUser = userID;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpUtils.get("trips/drivers/" + mUser, new RequestParams(), new JsonHttpResponseHandler() {
                @Override
                public boolean getUseSynchronousMode() {
                    return false;
                }

                @Override
                public void setUseSynchronousMode(boolean useSynchronousMode) {

                }

                @Override
                public void onFinish() {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {

                        tripsList.clear();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String date = obj.getString("departureDate");
                            String year = date.substring(0, 4);
                            String remainder = date.substring(4, 8);
                            String time = obj.getString("departureTime");
                            JSONObject driver = obj.getJSONObject("driver");
                            String driverName = driver.getString("name");


                            tripsList.add(new Trip(obj.getString("departureLocation"), obj.getString("destination"), year + "-" + formatter(remainder, "-", 2),
                                    formatter(time, ":", 2), driverName, Integer.toString(obj.getInt("seatAvailable")), Integer.toString(obj.getInt("tripId"))));

                            retrievalSuccess = true;

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            });
            return retrievalSuccess;

        }


        @Override
        protected void onPostExecute(final Boolean success) {


            mDisplayTripTask = null;
            //showSignInProgress(false);

            if (success) {

                updateVisibility(true, false);
            }
        }

        @Override
        protected void onCancelled() {
            mDisplayTripTask = null;
            //showSignInProgress(false);
        }
    }



    /**
     * @param displayTrips if true, display recycler view
     * @param isError      if true, display invalid city error
     */
    private void updateVisibility(boolean displayTrips, boolean isError) {
        if (displayTrips) {
            mRecyclerView.setVisibility(View.VISIBLE);
            noTrips.setVisibility(View.GONE);
        } else {
            noTrips.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            if (isError) {
                noTrips.setVisibility(View.VISIBLE);
            }
        }

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