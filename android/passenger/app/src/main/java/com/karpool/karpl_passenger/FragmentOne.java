package com.karpool.karpl_passenger;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FragmentOne extends Fragment {

    private String error = null;

//    private void refreshErrorMessage() {
//        // set the error message
//        TextView tvError = (TextView) findViewById(R.id.error);
//        tvError.setText(error);
//
//        if (error == null || error.length() == 0) {
//            tvError.setVisibility(View.GONE);
//        } else {
//            tvError.setVisibility(View.VISIBLE);
//        }
//
//    }
//
//    public void addParticipant(View v) {
//        error = "";
//        final TextView tv = (TextView) findViewById(R.id.newparticipant_name);
//        HttpUtils.post("participants/" + tv.getText().toString(), new RequestParams(), new JsonHttpResponseHandler() {
//            @Override
//            public void onFinish() {
//                refreshErrorMessage();
//                tv.setText("");
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                try {
//                    error += errorResponse.get("message").toString();
//                } catch (JSONException e) {
//                    error += e.getMessage();
//                }
//                refreshErrorMessage();
//            }
//        });
//    }


    private RecyclerView.LayoutManager mLayoutManager;

    private List<Trip> tripsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private searchAdapter mAdapter;

    private SearchView citySearch;
    private Spinner searchSpinner;

    private String sortSelection;


    private TextView invalidCity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        citySearch = (SearchView) rootView.findViewById(R.id.citySearch);
        invalidCity = (TextView) rootView.findViewById(R.id.invalidCity);
        searchSpinner = (Spinner) rootView.findViewById(R.id.searchSpinner);


        mAdapter = new searchAdapter(tripsList);
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
        sortSelection = searchSpinner.getSelectedItem().toString();


        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sortSelection = searchSpinner.getSelectedItem().toString();
                if(!citySearch.getQuery().toString().isEmpty()) {
                    prepareTripData(citySearch.getQuery().toString());
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
                // This is where the method search is done
                prepareTripData(query);

            }

        });


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


        return rootView;
    }

    /**
     * Updates the trip information
     */
    private void prepareTripData(String destination) {
        // TODO Look into calling the database here. You have the destintion, need the origin, date and time. Probably needs some kind of for loop in the trips array.

        // TODO Regarding sorting, you use the spinner, to get the spinner selection do, searchSpinner.getSelectedItem().toString(); (output is the string). I already handled this though. Just do if sortSelection is equal to the specific category
        // An external method could be needed
        tripsList.clear();



        if (destination.equals("Compton")) {
            tripsList.add(new Trip("MONTREAL", "COMPTON", "2018-10-31", "15:00"));
            tripsList.add(new Trip("MONTREAL", "COMPTON", "2018-10-31", "18:00"));
            tripsList.add(new Trip("MONTREAL", "COMPTON", "2018-10-31", "22:00"));

            updateVisibility(true, false);

        } else {
            updateVisibility(false, true);

        }


        mAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param displayTrips if true, display recycler view
     * @param isError if true, display invalid city error
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

}