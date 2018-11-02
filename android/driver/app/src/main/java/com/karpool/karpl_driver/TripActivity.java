package com.karpool.karpl_driver;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.util.Calendar;

public class TripActivity extends AppCompatActivity {


    // TODO I think i will need to implement this myself once you guys figure out other database stuff since it involves sharedPreferences

    private TextView modifyOrigin, modifyDestination, modifyPrice, modifySeats;
    private static TextView modifyDate, modifyTime;
    private static String date, time; // FOR DATABASE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);


        modifyOrigin = (TextView) findViewById(R.id.modifyTripOrigin);
        modifyDestination = (TextView) findViewById(R.id.modifyTripDestination);
        modifyDate = (TextView) findViewById(R.id.modifyTripDate);
        modifyTime = (TextView) findViewById(R.id.modifyTripTime);
        modifyPrice = (TextView) findViewById(R.id.modifyTripPrice);
        modifySeats = (TextView) findViewById(R.id.modifyTripSeats);



        modifyOrigin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(TripActivity.this, "To modify your origin, you must delete all trips and modify it in the settings menu", Toast.LENGTH_LONG).show();
            }
        });

        modifyDestination.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modifyPopUp("destination", modifyDestination);

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

        modifyPrice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modifyPopUp("price", modifyPrice);


            }
        });

        modifySeats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modifyPopUp("seats", modifySeats);


            }
        });





    }



    private void modifyPopUp(String type, TextView text) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        final TextView updateInput = text;


        switch(type) {
            case "price":
                alert.setTitle("Price");
                alert.setMessage("Update your trip's price");
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "seats":
                alert.setTitle("Seats");
                alert.setMessage("Update your vehicle's capacity");
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "destination":
                alert.setTitle("Destination");
                alert.setMessage("Update your trip's destination");
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                break;

        }

        alert.setView(input);

        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateInput.setText(input.getText());

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
            modifyTime.setVisibility(View.VISIBLE);        }


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
            date = Integer.toString(year) + convertDate(month) + convertDate(day);
            modifyDate.setText(Integer.toString(year) + "-" +  convertDate(month) + "-" +  convertDate(day));
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




}
