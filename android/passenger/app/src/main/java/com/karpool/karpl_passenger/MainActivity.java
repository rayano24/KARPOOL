package com.karpool.karpl_passenger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    //List that holds the fragments
    private List<Fragment> fragments = new ArrayList<>(3);

    private static final String TAG_FRAGMENT_SEARCH = "tag_frag_search";
    private static final String TAG_FRAGMENT_TRIPS = "tag_frag_trips";
    private static final String TAG_FRAGMENT_ACCOUNT = "tag_frag_account";

    private String error = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    switchFragment(0, TAG_FRAGMENT_SEARCH);
                    return true;
                case R.id.navigation_trips:
                    switchFragment(1, TAG_FRAGMENT_TRIPS);
                    return true;
                case R.id.navigation_account:
                    switchFragment(2, TAG_FRAGMENT_ACCOUNT);
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


        switchFragment(0, TAG_FRAGMENT_SEARCH);



    }


    private void switchFragment(int pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragmentholder, fragments.get(pos), tag)
                .commit();
    }


    private void buildFragmentsList() {
        FragmentOne searchFragment = buildFragmentOne(getResources().getString(R.string.title_search));
        FragmentTwo tripsFragment = buildFragmentTwo(getResources().getString(R.string.title_trips));
        FragmentThree accountFragment = buildFragmentThree(getResources().getString(R.string.title_account));

        fragments.add(searchFragment);
        fragments.add(tripsFragment);
        fragments.add(accountFragment);
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
