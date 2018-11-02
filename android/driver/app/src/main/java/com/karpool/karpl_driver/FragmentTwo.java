package com.karpool.karpl_driver;

import android.content.Intent;
import android.os.Bundle;
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
    private tripAdapter mAdapter;

    private TextView noTrips;

    private String userID = "hey";
    // TODO The logic I am going for here: When the user logs in, their account ID will be saved through sharedPreferences (will add this when you guys figure out databases). This will be used for loading their trips.


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_two, container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.myTripsRecyclerView);
        noTrips = (TextView) rootView.findViewById(R.id.noTrips);


        mAdapter = new tripAdapter(tripsList);
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
                Intent I = new Intent(getActivity(), TripActivity.class);
                startActivity(I);

            }

            @Override
            public void onLongClick(View view, int position) {
                // do nothing
            }
        }));


        prepareTripData(userID);


        return rootView;
    }

    /**
     * Updates the trip information
     */
    private void prepareTripData(String destination) {

        tripsList.clear();


        // TODO THIS SHOULD BE BASED ON THE DRIVERS TRIPS


           /* HttpUtils.get("trips/" + userLocation + "/" + destination, new RequestParams(), new JsonHttpResponseHandler() {
                @Override
                public void onFinish() {
                    updateVisibility(false, true);
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {

                        tripsList.clear();


                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String date = obj.getString("departureDate");
                            String year = date.substring(0, 4);
                            String remainder = date.substring(4,8);
                            String time = obj.getString("departureTime");
                            tripsList.add(new Trip(obj.getString("departureLocation"), obj.getString("destination"), year + "-" + formatter(remainder, "-", 2),
                                    formatter(time, ":", 2)));
                        }
                        updateVisibility(true, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    updateVisibility(false, true);
                    tripsList.clear();

                }
            }); */



        mAdapter.notifyDataSetChanged();
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