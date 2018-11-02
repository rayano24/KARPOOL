package com.karpool.karpl_driver;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class FragmentOne extends Fragment {

    private Button tripTimeButton, tripDateButton, createButton;
    protected static TextView dateLabel, timeLabel;
    private static String date, time; // FOR DATABASE
    private EditText newDestination, newPrice, newSeats;

    private final static String KEY_LOCATION = "userLocation";

    private static String userLocation, userID;
    private final static String KEY_USER = "userID";






    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            // listener implementation
            if (key.equals(KEY_LOCATION)) {
                userLocation = prefs.getString(KEY_LOCATION, " ");
            }

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(listener);
        userID = prefs.getString(KEY_USER, null);

        userLocation = prefs.getString(KEY_LOCATION,  null);


        tripTimeButton = (Button) rootView.findViewById(R.id.newTime);
        tripDateButton = (Button) rootView.findViewById(R.id.newDate);
        createButton = (Button) rootView.findViewById(R.id.createButton);
        dateLabel = (TextView) rootView.findViewById(R.id.dateLabel);
        timeLabel = (TextView) rootView.findViewById(R.id.timeLabel);
        newPrice = (EditText) rootView.findViewById(R.id.newPrice);
        newDestination = (EditText) rootView.findViewById(R.id.newDestination);
        newSeats = (EditText) rootView.findViewById(R.id.newSeats);






        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createTrip(userID, userLocation, newDestination.getText().toString(), newSeats.getText().toString(), time, date, newPrice.getText().toString());


            }
        });

        tripDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });


        tripTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");


            }
        });



        return rootView;
    }


    private void createTrip(String userID, String userLocation, String destination, String seats, String time, String Date, String price) {



                HttpUtils.get("trips/" + userID + "/" + userLocation + "/" + destination + "/" + seats + "/" + time + "/" + date + "/" + price, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("response") == true) {
                        clearFields();
                        Toast.makeText(getActivity(), "The trip was successfully created", Toast.LENGTH_SHORT).show();


                    }
                    else {
                        Toast.makeText(getActivity(), "Failed to create a trip", Toast.LENGTH_SHORT).show();

                    }





                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });

    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time = convertDate(hourOfDay) + convertDate(minute);
            timeLabel.setText(convertDate(hourOfDay) + ":" + convertDate(minute));
            timeLabel.setVisibility(View.VISIBLE);        }


    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            // new instance of datePickerDialog
            Dialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

            //setting minimum time
            ((DatePickerDialog) datePickerDialog).getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // TODO DATE OUTPUT
            date = Integer.toString(year) + convertDate(month) + convertDate(day);
            dateLabel.setText(Integer.toString(year) + "-" +  convertDate(month) + "-" +  convertDate(day));
            dateLabel.setVisibility(View.VISIBLE);

        }
    }

    protected static String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }

    protected void clearFields() {
        newDestination.setText("");
        newPrice.setText("");
        newSeats.setText("");

    }








}

