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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Displays details of a specific trip and gives the user the ability to modify it if a user has not joined.
 * Can also delete or close a trip, and click to open a PassengerActivity
 */
public class TripActivity extends AppCompatActivity {


    // Pref keys

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


    // UI elements

    private TextView modifyOrigin, modifyDestination, modifyPrice, modifySeats, noPassengersJoined;
    private static TextView modifyDate, modifyTime;
    private Button deleteButton, viewPassengerButton, closeButton;
    private static String date, time; // FOR DATABASE


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


        modifyOrigin = findViewById(R.id.modifyTripOrigin);
        modifyDestination = findViewById(R.id.modifyTripDestination);
        modifyDate = findViewById(R.id.modifyTripDate);
        modifyTime = findViewById(R.id.modifyTripTime);
        modifyPrice = findViewById(R.id.modifyTripPrice);
        modifySeats = findViewById(R.id.modifyTripSeats);
        deleteButton = findViewById(R.id.deleteButton);
        viewPassengerButton = findViewById(R.id.messageButton);
        closeButton = findViewById(R.id.closeButton);


        tripID = prefs.getString(KEY_TRIP_ID, null);
        userID = prefs.getString(KEY_USER_ID, null);
        tripMode = prefs.getString(KEY_TRIP_FRAG_MODE, null);


        // setting values based on recycler view on fragment 2

        modifyTime.setText(prefs.getString(KEY_TRIP_TIME, null));
        modifyDate.setText(prefs.getString(KEY_TRIP_DATE, null));
        modifyDestination.setText(prefs.getString(KEY_TRIP_DESTINATION, null));
        modifyOrigin.setText(prefs.getString(KEY_TRIP_ORIGIN, null));
        modifySeats.setText(prefs.getString(KEY_TRIP_SEATS, null));
        modifyPrice.setText("$" + prefs.getString(KEY_TRIP_PRICE, null));


        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setTripStatus("delete");

            }
        });


        // to close a trip after it is completed
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setTripStatus("close");

            }
        });


        // view the list of passengers
        viewPassengerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent I = new Intent(TripActivity.this, PassengerActivity.class);
                startActivity(I);
            }
        });


        // modify the starting location
        modifyOrigin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog(modifyOrigin, "Origin", "Update your trip's starting position"); // opens a popup dialog to enter the new origin, confirming input opens an async task to modify

            }
        });


        // modify the destination
        modifyDestination.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog(modifyDestination, "Destination", "Update your trip's destination");
            }
        });


        // modify the price
        modifyPrice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog(modifyPrice, "Price", "Update your trip's price");


            }
        });

        // modify the number of seats
        modifySeats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog(modifySeats, "Seats", "Update your vehicle's capacity");

            }
        });


        // modify the date - opens a date picker fragment where an async task is called after a date is selected
        modifyDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });


        // modify the time - opens a time picker fragment wehre an async task is called after a time is selected
        modifyTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        convertView(tripMode); //  if a trip already occurred, disable ability to modify, close, delete or view passengers
    }


    /**
     * Represents an async task to modify any aspect of the trip. The category indicates what you want to modify.
     *
     * @param newValue the new value
     * @param category a string to indicate what you are modifying
     */
    public void modifyTripTask(final String newValue, final String category) {


        if (category.equals(KEY_TRIP_PRICE) || category.equals(KEY_TRIP_SEATS)) {

            HttpUtils.post("trips/" + Integer.parseInt(tripID) + "/" + category + "/" + Integer.parseInt(newValue), new RequestParams(), new JsonHttpResponseHandler() {


                @Override
                public void onFinish() {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getBoolean("response")) {
                            updateView(category, newValue); //  updates the UI with the new value

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Toast.makeText(TripActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

                }

            });
        } else {


            HttpUtils.post("trips/" + Integer.parseInt(tripID) + "/" + category + "/" + newValue, new RequestParams(), new JsonHttpResponseHandler() {


                @Override
                public void onFinish() {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // TODO
                    try {
                        if (response.getBoolean("response")) {
                            updateView(category, newValue);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Toast.makeText(TripActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

                }

            });
        }
    }


    /**
     * Represents an asynchronous task to either set a trip to closed or delete it.
     *
     * @param status a string that can either be close, or delete based on the desired outcome
     */
    public void setTripStatus(String status) {

        HttpUtils.post("trips/" + status + "/" + tripID, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //todo
                finish();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(TripActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

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
                // calls the necessary async tasks
                switch (category) {
                    case ("Origin"):
                        modifyTripTask(input.getText().toString(), KEY_TRIP_ORIGIN);
                        break;
                    case ("Destination"):
                        modifyTripTask(input.getText().toString(), KEY_TRIP_DESTINATION);
                        break;
                    case ("Seats"):
                        modifyTripTask(input.getText().toString(), KEY_TRIP_SEATS);
                        break;
                    case ("Price"):
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


    /**
     * Updates the UI element with the new value inputted by the user
     *
     * @param category what you are modifying
     * @param newValue the new value you want to set
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
                modifyPrice.setText("$" + newValue);
                break;
            case (KEY_TRIP_SEATS):
                modifySeats.setText(newValue);
                break;
            case (KEY_TRIP_DATE):
                final String formattedDate = newValue.substring(0, 4) + "-" + newValue.substring(4, 6) + "-" + newValue.subSequence(6, 8);
                modifyDate.setText(formattedDate);
                break;
            case (KEY_TRIP_TIME):
                final String formattedTime = time.substring(0, 2) + ":" + time.substring(2, 4);
                modifyTime.setText(formattedTime);
                break;
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
     * Removes the ability to modify values if a trip has already occurred
     *
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
            viewPassengerButton.setVisibility(View.INVISIBLE);
            closeButton.setVisibility(View.INVISIBLE);
        }
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
            new TripActivity().modifyTripTask(time, KEY_TRIP_TIME);
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
            new TripActivity().modifyTripTask(date, KEY_TRIP_DATE);
        }

    }


}
