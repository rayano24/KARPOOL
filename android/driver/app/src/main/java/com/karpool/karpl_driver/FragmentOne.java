package com.karpool.karpl_driver;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

/**
 * Allows the user to create a trip.
 */
public class FragmentOne extends Fragment {

    private Button createButton;
    private static String date, time; // FOR DATABASE
    private EditText newDestination, newPrice, newSeats, newOrigin;
    private static TextView newTime, newDate;


    private static String userID;
    private final static String KEY_USER = "userID";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userID = prefs.getString(KEY_USER, null);


        createButton = rootView.findViewById(R.id.createButton);
        newPrice = rootView.findViewById(R.id.newTripPrice);
        newDestination = rootView.findViewById(R.id.newTripDestination);
        newSeats = rootView.findViewById(R.id.newTripSeats);
        newOrigin = rootView.findViewById(R.id.newTripOrigin);
        newTime = rootView.findViewById(R.id.newTripTime);
        newDate = rootView.findViewById(R.id.newTripDate);



        // starts async task
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                attemptTripCreation(userID, newOrigin.getText().toString(), newDestination.getText().toString(),
                        newSeats.getText().toString(), time, date, newPrice.getText().toString());
            }
        });

        // set date
        newDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });


        // set time
        newTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");


            }
        });


        return rootView;
    }


    /**
     * Makes sure that all user-entered fields are filled
     *
     * @param mUser        needed for tripCreationTask
     * @param mOrigin      checks if the entered origin is empty
     * @param mDestination checks if the entered destination is empty
     * @param mSeats       checks if the entered number of seats is empty
     * @param mTime        checks if a time was selected with time picker
     * @param mDate        checks if a date was selected with date picker
     * @param mPrice       checks if the entered price is empty
     */

    private void attemptTripCreation(String mUser, String mOrigin, String mDestination, String mSeats, String mTime, String mDate, String mPrice) {

        Boolean cancel = false;


        if ((TextUtils.isEmpty(mOrigin.trim())) || TextUtils.isEmpty(mDestination.trim()) || TextUtils.isEmpty(mSeats.trim()) || TextUtils.isEmpty(mSeats.trim()) || date == null || time == null) {
            Toast.makeText(getActivity(), "You cannot leave any fields blank!", Toast.LENGTH_LONG).show();
            cancel = true;
        }

        if (!cancel) {
            createTripTask(mUser, mOrigin, mDestination, Integer.parseInt(mSeats), mTime, mDate, Integer.parseInt(mPrice));
        }

    }


    /**
     * Represents an asynchronous task that allows a user to create a trip
     *
     * @param mUser        userID of the user creating the trip
     * @param mOrigin      trip origin
     * @param mDestination trip destination
     * @param mSeats       number of seats
     * @param mTime        trip time
     * @param mDate        trip date
     * @param mPrice       price per individual
     */
    public void createTripTask(String mUser, String mOrigin, String mDestination, int mSeats, String mTime, String mDate, int mPrice) {


        HttpUtils.post("trips/" + mUser + "/" + mOrigin.replaceAll(" ", "_") + "/" + mDestination.replaceAll(" ", "_") + "/" + mSeats + "/" + mTime + "/" + mDate + "/" + mPrice, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (!response.isNull("destination")) {
                        clearFields();


                    } else {
                        Toast.makeText(getActivity(), response.getString("error"), Toast.LENGTH_LONG).show(); //  show error
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getActivity(), "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

            }

        });


    }


    /**
     * Opens a dialog in order to enter a time.
     * When time is entered, starts an asynchronous task to update the time
     */
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
            newTime.setText(convertDate(hourOfDay) + ":" + convertDate(minute));
        }


    }


    /**
     * Opens a dialog in order to enter a date.
     * When a date is entered, starts an asynchronous task to update the date.
     */
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
            date = Integer.toString(year) + convertDate(month + 1) + convertDate(day);
            newDate.setText(Integer.toString(year) + "-" + convertDate(month + 1) + "-" + convertDate(day));

        }
    }


    /**
     * Converts a date/time value to be two digits
     *
     * @param input the value you want to convert
     * @return a 2 digit output such as 03 if 3 were inputted
     */
    private static String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }


    /**
     * Clears all fields after creating a trip
     */
    private void clearFields() {
        newOrigin.setText("");
        newDestination.setText("");
        newPrice.setText("");
        newSeats.setText("");
        newOrigin.clearFocus();
        newDestination.clearFocus();
        newPrice.clearFocus();
        newSeats.clearFocus();
        newDate.setText(getString(R.string.create_trip_date));
        newTime.setText(getString(R.string.create_trip_time));


    }


}

