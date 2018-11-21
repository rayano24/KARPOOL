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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class FragmentThree extends Fragment {


    private final static String KEY_USER_ID = "userID";
    private final static String KEY_RATING = "rating";
    private static String userID;


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
                prefs.edit().remove(KEY_USER_ID).commit();
                Intent I = new Intent(getActivity(), LoginActivity.class);
                startActivity(I);
                getActivity().finish();
            }
        });

        userID = prefs.getString(KEY_USER_ID, null);

       /* String rating = prefs.getString(KEY_RATING, null);
        if(rating == null) {
            userNote.setText("Welcome "+ prefs.getString(KEY_USER_ID, null));
        }
        else {
            userNote.setText("Welcome "+ prefs.getString(KEY_USER_ID, null) + " (" + rating + ")");


        }

        */

        userNote.setText("Welcome "+ userID);




        return rootView;
    }


    public void displayRatingTask() {

        HttpUtils.get("drivers/rate/" + userID, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getDouble("rating") != -1) {

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


