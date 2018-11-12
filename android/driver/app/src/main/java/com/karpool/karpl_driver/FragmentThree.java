package com.karpool.karpl_driver;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentThree extends Fragment {


    private final static String KEY_USER_ID = "userID";

    private TextView signOut, help, userNote;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_three, container, false);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());



        signOut = rootView.findViewById(R.id.signOutButton);
        help = rootView.findViewById(R.id.helpButton);
        userNote = rootView.findViewById(R.id.userNote);


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().remove("userID").commit();
                Intent I = new Intent(getActivity(), LoginActivity.class);
                startActivity(I);
                getActivity().finish();
            }
        });


        userNote.setText("Welcome "+ prefs.getString(KEY_USER_ID, null));







        return rootView;
    }



}


