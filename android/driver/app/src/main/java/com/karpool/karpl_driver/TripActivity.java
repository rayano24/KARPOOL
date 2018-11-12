package com.karpool.karpl_driver;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private final static String KEY_TRIP_DESTINATION = "tripDestination";
    private final static String KEY_TRIP_TIME = "tripTime";
    private final static String KEY_TRIP_DATE = "tripDate";
    private final static String KEY_TRIP_ORIGIN = "tripOrigin";
    private final static String KEY_TRIP_DRIVER = "searchDriver";
    private final static String KEY_TRIP_SEATS = "searchSeats";
    private final static String KEY_TRIP_ID = "tripID";
    private final static String KEY_USER_ID = "userID";
    private final static String KEY_USER_LOCATION = "userLocation";
    private static String userID, tripID, userLocation;


    private TextView modifyOrigin, modifyDestination, modifyPrice, modifySeats;
    private static TextView modifyDate, modifyTime;
    private static String date, time; // FOR DATABASE


    private ModifyTripTask mModifyTripTask = null;
    private boolean valueModified;


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


        tripID = prefs.getString(KEY_TRIP_ID, null);
        userID = prefs.getString(KEY_USER_ID, null);


        String date = (prefs.getString(KEY_TRIP_DATE, null));

        String year = date.substring(0, 4);
        String remainder = date.substring(4, 8);
        String time = prefs.getString(KEY_TRIP_TIME, null);

        modifyTime.setText(formatter(time, ":", 2));
        modifyDate.setText(year + "-" + formatter(remainder, "-", 2));
        modifyDestination.setText(prefs.getString(KEY_TRIP_DESTINATION, null));
        modifyOrigin.setText(prefs.getString(KEY_TRIP_ORIGIN, null));
        modifySeats.setText(prefs.getString(KEY_TRIP_SEATS, null));




        modifyOrigin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog(modifyOrigin, "Origin", "Update your trip's starting position" );

            }
        });

        modifyDestination.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog(modifyDestination, "Destination", "Update your trip's destination" );
            }
        });



        modifyPrice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog(modifyPrice, "Price", "Update your trip's price" );



            }
        });

        modifySeats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog(modifySeats, "Seats", "Update your vehicle's capacity " );

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

    }


    /**
     * Represents an asynchronous modify trip task
     * Takes the new value of what you are modifying as well as the string to modify to indicate what you are changing
     */
    public class ModifyTripTask extends AsyncTask<Void, Void, Boolean> {

        private final String mNewValue;
        private final String mToModify;


        ModifyTripTask(String value, String toModify) {
            mNewValue = value;
            mToModify = toModify;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpUtils.post("trips/" + tripID + "/" + mToModify + "/" + mNewValue, new RequestParams(), new JsonHttpResponseHandler() {
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
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                   // TODO

                    /* try {
                        //


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    */
                }

            });
            return valueModified;
        }


        @Override
        protected void onPostExecute(final Boolean success) {


            mModifyTripTask = null;


            if (success) {

            }
        }

        @Override
        protected void onCancelled() {
            mModifyTripTask = null;
        }
    }


    /**
     * Opens the dialog for modifying a textEdit
     * @param text the text to modify
     * @param title the title of the alert
     * @param description the description of the alert
     */
    private void openDialog(TextView text, String title, String description) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        final TextView updateInput = text;
        final String category = title;


        alert.setTitle(title);
        alert.setMessage(description);

        if(title.equals("Price") || title.equals("Seats")) {
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else {
            input.setInputType(InputType.TYPE_CLASS_TEXT);
        }


        alert.setView(input);

        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateInput.setText(input.getText());
                switch(category) {
                    case("Origin"):
                        mModifyTripTask = new ModifyTripTask(input.getText().toString(), KEY_TRIP_ORIGIN);
                        mModifyTripTask.execute((Void) null);
                        break;
                    case("Destination"):
                        mModifyTripTask = new ModifyTripTask(input.getText().toString(), KEY_TRIP_DESTINATION);
                        mModifyTripTask.execute((Void) null);
                        break;
                    case("seats"):
                        mModifyTripTask = new ModifyTripTask(input.getText().toString(), KEY_TRIP_SEATS);
                        mModifyTripTask.execute((Void) null);
                        break;
                    case("price"):
                        // todo
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
            modifyTime.setText(convertDate(hourOfDay) + ":" + convertDate(minute));
            modifyTime.setVisibility(View.VISIBLE);
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
            modifyDate.setText(Integer.toString(year) + "-" + convertDate(day) + "-" + convertDate(month));
            modifyDate.setVisibility(View.VISIBLE);
        }

    }

    protected static String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
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
