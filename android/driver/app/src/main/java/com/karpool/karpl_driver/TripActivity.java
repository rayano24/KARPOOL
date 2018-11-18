package com.karpool.karpl_driver;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class TripActivity extends AppCompatActivity {

    private final static String KEY_TRIP_DESTINATION = "tripdestination";
    private final static String KEY_TRIP_TIME = "time";
    private final static String KEY_TRIP_DATE = "date";
    private final static String KEY_TRIP_ORIGIN = "triplocation";
    private final static String KEY_TRIP_SEATS = "seats";
    private final static String KEY_TRIP_ID = "tripID";
    private final static String KEY_USER_ID = "userID";
    private final static String KEY_TRIP_PRICE = "tripprice";
    private static String userID, tripID, tripMode;
    private final static String KEY_TRIP_FRAG_MODE = "tripMode";


    private TextView modifyOrigin, modifyDestination, modifyPrice, modifySeats;
    private static TextView modifyDate, modifyTime;
    private Button deleteButton;
    private static String date, time; // FOR DATABASE


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


        modifyOrigin = (TextView) findViewById(R.id.modifyTripOrigin);
        modifyDestination = (TextView) findViewById(R.id.modifyTripDestination);
        modifyDate = (TextView) findViewById(R.id.modifyTripDate);
        modifyTime = (TextView) findViewById(R.id.modifyTripTime);
        modifyPrice = (TextView) findViewById(R.id.modifyTripPrice);
        modifySeats = (TextView) findViewById(R.id.modifyTripSeats);
        deleteButton = findViewById(R.id.deleteButton);


        tripID = prefs.getString(KEY_TRIP_ID, null);
        userID = prefs.getString(KEY_USER_ID, null);
        tripMode = prefs.getString(KEY_TRIP_FRAG_MODE, null);


        String date = (prefs.getString(KEY_TRIP_DATE, null));

        String year = date.substring(0, 4);
        String remainder = date.substring(4, 8);
        String time = prefs.getString(KEY_TRIP_TIME, null);

        modifyTime.setText(prefs.getString(KEY_TRIP_TIME, null));
        modifyDate.setText(prefs.getString(KEY_TRIP_DATE, null));
        modifyDestination.setText(prefs.getString(KEY_TRIP_DESTINATION, null));
        modifyOrigin.setText(prefs.getString(KEY_TRIP_ORIGIN, null));
        modifySeats.setText(prefs.getString(KEY_TRIP_SEATS, null));
        modifyPrice.setText("$" + prefs.getString(KEY_TRIP_PRICE, null));


        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteTrip();

            }
        });


        modifyOrigin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog(modifyOrigin, "Origin", "Update your trip's starting position");

            }
        });

        modifyDestination.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog(modifyDestination, "Destination", "Update your trip's destination");
            }
        });


        modifyPrice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog(modifyPrice, "Price", "Update your trip's price");


            }
        });

        modifySeats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog(modifySeats, "Seats", "Update your vehicle's capacity");

            }
        });


        modifyDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new FragmentOne.DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });


        modifyTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new FragmentOne.TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        convertView(tripMode);
    }


    /**
     * Represents an async task to modify the trip
     *
     * @param newValue the new value
     * @param category a string to indicate what you are modifying
     */
    public void modifyTripTask(String newValue, String category) {

        final String mNewValue = newValue;
        final String mToModify = category;


        HttpUtils.post("trips/" + tripID + "/" + mToModify + "/" + mNewValue, new RequestParams(), new JsonHttpResponseHandler() {


            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO
                try {
                    if (response.getBoolean("response") == true) {
                        updateView(mToModify, mNewValue);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
    }


    /**
     * Represents an async task to delete the selected trip
     */
    public void deleteTrip() {

        HttpUtils.post("trips/close/" + tripID, new RequestParams(), new JsonHttpResponseHandler() {


            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO what kind of response is this?
                try {
                    if (response.getBoolean("response")) {
                        Fragment mFragment = null;
                        mFragment = new FragmentTwo();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_fragmentholder, mFragment).commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
    }


    /**
     * Opens the dialog for modifying a textEdit
     *
     * @param text        the text to modify
     * @param title       the title of the alert
     * @param description the description of the alert
     */
    private void openDialog(TextView text, String title, String description) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        final TextView updateInput = text;
        final String category = title;


        alert.setTitle(title);
        alert.setMessage(description);

        if (title.equals("Price") || title.equals("Seats")) {
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            input.setInputType(InputType.TYPE_CLASS_TEXT);
        }


        alert.setView(input);

        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                switch (category) {
                    case ("Origin"):
                        modifyTripTask(input.getText().toString(), KEY_TRIP_ORIGIN);
                        break;
                    case ("Destination"):
                        modifyTripTask(input.getText().toString(), KEY_TRIP_DESTINATION);
                        break;
                    case ("seats"):
                        modifyTripTask(input.getText().toString(), KEY_TRIP_SEATS);
                        break;
                    case ("price"):
                        modifyTripTask(input.getText().toString(), KEY_TRIP_PRICE);
                        break;


                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

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
            new TripActivity().modifyTripTask(time, KEY_TRIP_TIME);
            //modifyTime.setText(convertDate(hourOfDay) + ":" + convertDate(minute));

        }


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
            date = Integer.toString(year) + convertDate(day) + convertDate(month);
            new TripActivity().modifyTripTask(date, KEY_TRIP_DATE);
            // modifyDate.setText(Integer.toString(year) + "-" + convertDate(day) + "-" + convertDate(month));
        }

    }


    /**
     * Converts a date/time value to be two digits
     *
     * @param input the value you want to convert
     * @return a 2 digit output such as 03 if 3 were inputted
     */

    protected static String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }

    /**
     * @param category
     * @param newValue
     */
    public void updateView(String category, String newValue) {
        switch (category) {
            case (KEY_TRIP_ORIGIN):
                modifyOrigin.setText(newValue);
                break;
            case (KEY_TRIP_DESTINATION):
                modifyDestination.setText(newValue);
                break;
            case (KEY_TRIP_PRICE):
                modifyPrice.setText(newValue);
                break;
            case (KEY_TRIP_SEATS):
                modifySeats.setText(newValue);
                break;
            case (KEY_TRIP_DATE):
                modifyDate.setText(date.substring(0, 4) + "-" + date.substring(3, 5) + "-" + date.subSequence(5, 7));
                break;
            case (KEY_TRIP_TIME):
                modifyTime.setText(time.substring(0, 2) + ":" + time.substring(2, 4));
                break;
        }
    }

    /**
     * Removes the ability to modify values if a trip has already occurred
     * @param type the trip type (past or upcoming)
     */
    public void convertView(String type) {
        if (type.equals("PAST")) {
            modifyOrigin.setClickable(false);
            modifyDestination.setClickable(false);
            modifyPrice.setClickable(false);
            modifySeats.setClickable(false);
            modifyTime.setClickable(false);
            modifyDate.setClickable(false);
            deleteButton.setVisibility(View.INVISIBLE);
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
