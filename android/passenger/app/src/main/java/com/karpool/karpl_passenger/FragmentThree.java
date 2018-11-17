package com.karpool.karpl_passenger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


public class FragmentThree extends Fragment {


    private final static String KEY_USER_ID = "userID";
    private final static String KEY_USER_LOCATION = "userLocation";


    private TextView signOut, help, userNote, userLocation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_three, container, false);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        signOut = rootView.findViewById(R.id.signOutButton);
        help = rootView.findViewById(R.id.helpButton);
        userNote = rootView.findViewById(R.id.userNote);
        userLocation = rootView.findViewById(R.id.userLocation);


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().remove("userID").commit();
                Intent I = new Intent(getActivity(), LoginActivity.class);
                startActivity(I);
                getActivity().finish();
            }
        });

        userLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });


        userNote.setText("Welcome " + prefs.getString(KEY_USER_ID, null));
        userLocation.setText(prefs.getString(KEY_USER_LOCATION, null));


        return rootView;
    }


    /**
     * Opens a pop up dialog to enter a new location
     */
    private void openDialog() {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        final EditText input = new EditText(getActivity());


        alert.setTitle("Location");
        alert.setMessage("Update your current location");


        alert.setView(input);

        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                userLocation.setText(input.getText().toString());
                prefs.edit().putString(KEY_USER_LOCATION, input.getText().toString()).commit();


            }

        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }


}
