package com.karpool.karpl_passenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * This fragment handles searching for a trip. It opens the TripActivity when needed.
 */
public class FragmentOne extends Fragment {


    private List<Trip> tripsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TripAdapter mAdapter;
    private SearchView citySearch;
    private Spinner searchSpinner;
    private TextView invalidCity;


    // preference keys

    private final static String KEY_TRIP_STATUS = "tripJoined"; // if trip is already joined or if you are viewing a trip
    private final static String KEY_TRIP_FRAG_MODE = "tripMode"; // if a trip is upcoming or already happened
    private final static String KEY_USER_LOCATION = "userLocation";
    private final static String KEY_TRIP_DESTINATION = "tripdestination";
    private final static String KEY_TRIP_TIME = "time";
    private final static String KEY_TRIP_DATE = "date";
    private final static String KEY_TRIP_ORIGIN = "triplocation";
    private final static String KEY_TRIP_SEATS = "seats";
    private final static String KEY_TRIP_ID = "tripID";
    private final static String KEY_TRIP_DRIVER = "driver";
    private final static String KEY_USER_ID = "userID";
    private final static String KEY_TRIP_DRIVER_NUMBER = "number";
    private final static String KEY_TRIP_DRIVER_RATING = "rating";
    private final static String KEY_TRIP_PRICE = "tripprice";


    private static String userLocation, userID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userLocation = prefs.getString(KEY_USER_LOCATION, null);
        userID = prefs.getString(KEY_USER_ID, null);


        // main view elements

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        citySearch = rootView.findViewById(R.id.citySearch);
        invalidCity = rootView.findViewById(R.id.invalidCity);
        searchSpinner = rootView.findViewById(R.id.searchSpinner);

        // setting up the recycler view to display the trips

        mAdapter = new TripAdapter(tripsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);


        // sort spinner
        String[] sortArray = {getResources().getString(R.string.sort_time), getResources().getString(R.string.sort_price)};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, sortArray);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(sortAdapter);
        searchSpinner.setSelection(0);


        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!citySearch.getQuery().toString().isEmpty()) {
                    if (searchSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.sort_time))) {
                        searchSpinner.clearFocus();
                        prepareTripData(citySearch.getQuery().toString(), "date");
                    } else {
                        searchSpinner.clearFocus();
                        prepareTripData(citySearch.getQuery().toString(), "price");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


        // search listener
        citySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                citySearch.clearFocus();
                return true;
            }

            // we are not using this
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            public void callSearch(String query) {
                if (userLocation == null) {
                    // shows a snackbar in order to tell the user to set their location
                    Snackbar setLocation = Snackbar.make(getActivity().findViewById(android.R.id.content), "You must set your location in the accounts menu!", Snackbar.LENGTH_LONG * 2);
                    setLocation.setAction("Update", new snackBarInfo());
                    setLocation.show();

                } else {
                    if (searchSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.sort_time))) {
                        prepareTripData(citySearch.getQuery().toString(), "date");
                    } else {
                        prepareTripData(citySearch.getQuery().toString(), "price");
                    }
                }

            }

        });


        // Click listener for trip selection
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
                prefs.edit().putString(KEY_TRIP_PRICE, trip.getPrice()).commit();
                prefs.edit().putString(KEY_TRIP_DRIVER_NUMBER, trip.getDriverNumber()).commit();
                prefs.edit().putString(KEY_TRIP_DRIVER_RATING, trip.getDriverRating()).commit();
                prefs.edit().putString(KEY_TRIP_STATUS, "VIEW").commit();
                prefs.edit().putString(KEY_TRIP_FRAG_MODE, "UPCOMING").commit();
                Intent I = new Intent(getActivity(), TripActivity.class);
                startActivity(I);
            }

            @Override
            public void onLongClick(View view, int position) {
                // do nothing
            }
        }));

        return rootView;
    }



    // If the user moves to the TripActivity and joins the trip, this will clear the front page when they go back
    @Override
    public void onResume() {
        super.onResume();
        citySearch.setQuery("", false);
        citySearch.clearFocus();
        tripsList.clear();
        mAdapter.notifyDataSetChanged();
    }


    /**
     * Queries trips based on the destination that the user inputted.
     * Two options: Query based on trip time, or lowest price.
     *
     * @param destination the desired trip destination
     * @param searchType  date or price based on the criteria discussed above
     */
    private void prepareTripData(String destination, String searchType) {

        tripsList.clear();

        HttpUtils.get("trips/" + userLocation + "/" + destination + "/" + userID + "/" + searchType, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                updateVisibility(false, true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    tripsList.clear();

                    // parse all the data needed and put each trip one by one into the recycler view, if it is empty, display the error message
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        String date = obj.getString("departureDate");
                        String year = date.substring(0, 4);
                        String remainder = date.substring(4, 8);
                        String time = obj.getString("departureTime");
                        JSONObject driver = obj.getJSONObject("driver");
                        String driverName = driver.getString("name");
                        String driverNumber = driver.getString("phoneNumber");
                        Boolean tripComplete = obj.getBoolean("tripComplete");

                        JSONArray ratingArray = driver.getJSONArray("ratings");
                        double driverRating = 0.0;

                        for (int ratingCount = 0; ratingCount < ratingArray.length(); ratingCount++) {
                            driverRating += ratingArray.getDouble(ratingCount);
                        }
                        driverRating /= ratingArray.length();


                        if (!tripComplete) {
                            tripsList.add(new Trip(obj.getString("departureLocation"), obj.getString("destination"), year + "-" + formatter(remainder, "-", 2),
                                    formatter(time, ":", 2), driverName, driverNumber, Double.toString(driverRating), Integer.toString(obj.getInt("seatAvailable")), Integer.toString(obj.getInt("price")), Integer.toString(obj.getInt("tripId"))));
                        }

                    }

                    updateVisibility(true, false);


                    if (tripsList.isEmpty()) {
                        updateVisibility(false, true);
                    }

                    mAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error
            }
        });


    }


    /**
     * @param displayTrips if true, display recycler view
     * @param isError      if true, display invalid city error
     */
    private void updateVisibility(boolean displayTrips, boolean isError) {
        if (displayTrips) {
            mRecyclerView.setVisibility(View.VISIBLE);
            invalidCity.setVisibility(View.GONE);
        } else {
            invalidCity.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            if (isError) {
                invalidCity.setVisibility(View.VISIBLE);
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

    /**
     * To switch fragments when the snackbar button is clicked
     */
    private class snackBarInfo implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Fragment mFragment = new FragmentThree();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_fragmentholder, mFragment).commit();
        }
    }

}