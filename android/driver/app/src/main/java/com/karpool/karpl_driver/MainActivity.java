package com.karpool.karpl_driver;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * This class is responsible for handling the bottom navigation view and switching between fragments based on nav selections.
 * It also retrieves the driver's account rating as this reduces the appearance of refreshes.
 */
public class MainActivity extends AppCompatActivity {


    //List that holds the fragments
    private List<Fragment> fragments = new ArrayList<>(3);

    private static final String TAG_FRAGMENT_CREATE = "tag_frag_create";
    private static final String TAG_FRAGMENT_TRIPS = "tag_frag_trips";
    private static final String TAG_FRAGMENT_ACCOUNT = "tag_frag_account";


    private final static String KEY_USER_ID = "userID";
    private final static String KEY_RATING = "rating";
    private static String userID;


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
                case R.id.navigation_account:
                    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    userID = prefs.getString(KEY_USER_ID, null);
                    getUserRatingTask();
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

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        buildFragmentsList();


        switchFragment(0, TAG_FRAGMENT_CREATE);


        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        userID = prefs.getString(KEY_USER_ID, null);
        getUserRatingTask();

    }


    private void switchFragment(int pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragmentholder, fragments.get(pos), tag)
                .commit();
    }


    private void buildFragmentsList() {
        FragmentOne createFragment = buildFragmentOne("Create");
        FragmentTwo tripFragment = buildFragmentTwo("My Trips");
        FragmentThree accountFragment = buildFragmentThree("Account");

        fragments.add(createFragment);
        fragments.add(tripFragment);
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


    /**
     * Gets the user's rating through a method that returns a driver object.
     * Sets it to a preference in order to retrieve in fragment 3.
     */
    public void getUserRatingTask() {

        HttpUtils.get("drivers/" + userID, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                try {
                    if (response.getJSONArray("ratings").length() != 0) {
                        JSONArray ratingArray = response.getJSONArray("ratings");
                        double driverRating = 0.0;

                        for (int ratingCount = 0; ratingCount < ratingArray.length(); ratingCount++) {
                            driverRating += ratingArray.getDouble(ratingCount);

                        }

                        driverRating /= ratingArray.length();
                        DecimalFormat df = new DecimalFormat("###.#");
                        prefs.edit().putString(KEY_RATING, df.format(driverRating)).commit();


                    } else {
                        prefs.edit().putString(KEY_RATING, null).commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }


}
