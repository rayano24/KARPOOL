package com.karpool.karpl_driver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    //List that holds the fragments
    private List<Fragment> fragments = new ArrayList<>(3);

    private static final String TAG_FRAGMENT_CREATE = "tag_frag_create";
    private static final String TAG_FRAGMENT_TRIPS = "tag_frag_trips";
    private static final String TAG_FRAGMENT_SETTINGS = "tag_frag_settings";

    private List<String> participantNames = new ArrayList<>();
    private ArrayAdapter<String> participantAdapter;
    private List<String> eventNames = new ArrayList<>();
    private ArrayAdapter<String> eventAdapter;
    private static final String error = null; //error handling





    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_create:
                    switchFragment(0, TAG_FRAGMENT_CREATE);
                    return true;
                case R.id.navigation_trips:
                    switchFragment(1, TAG_FRAGMENT_TRIPS);
                    return true;
                case R.id.navigation_settings:
                    switchFragment(2, TAG_FRAGMENT_SETTINGS);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        buildFragmentsList();


        switchFragment(0, TAG_FRAGMENT_CREATE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("prevStarted", false);
        if(!previouslyStarted)

        {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("prevStarted", Boolean.TRUE);
            edit.commit();

            AlertDialog.Builder alert = new AlertDialog.Builder(this);


            alert.setTitle("Notice");
            alert.setMessage("Don't forget to set your location in the settings menu");




            alert.setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }







    }


    private void switchFragment(int pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragmentholder, fragments.get(pos), tag)
                .commit();
    }


    private void buildFragmentsList() {
        FragmentOne homeFragment = buildFragmentOne("Create");
        FragmentTwo dashboardFragment = buildFragmentTwo("My Trips");
        FragmentThree notificationsFragment = buildFragmentThree("Settings");

        fragments.add(homeFragment);
        fragments.add(dashboardFragment);
        fragments.add(notificationsFragment);
    }

    private FragmentOne buildFragmentOne(String title) {
        FragmentOne fragment = new FragmentOne();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    private FragmentTwo buildFragmentTwo(String title) {
        FragmentTwo fragment = new FragmentTwo();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    private FragmentThree buildFragmentThree(String title) {
        FragmentThree fragment = new FragmentThree();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }



}
