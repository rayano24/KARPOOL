package com.karpool.karpl_driver;

import android.os.Bundle;
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

    private static final String TAG_FRAGMENT_HOME = "tag_frag_home";
    private static final String TAG_FRAGMENT_DASHBOARD = "tag_frag_dashboard";
    private static final String TAG_FRAGMENT_NOTIFICATIONS = "tag_frag_notifications";

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
                case R.id.navigation_home:
                    switchFragment(0, TAG_FRAGMENT_HOME);
                    return true;
                case R.id.navigation_dashboard:
                    switchFragment(1, TAG_FRAGMENT_DASHBOARD);
                    return true;
                case R.id.navigation_notifications:
                    switchFragment(2, TAG_FRAGMENT_NOTIFICATIONS);
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


        switchFragment(0, TAG_FRAGMENT_HOME);

        refreshErrorMessage(); //keep this at the end



    }


    private void switchFragment(int pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragmentholder, fragments.get(pos), tag)
                .commit();
    }


    private void buildFragmentsList() {
        FragmentOne homeFragment = buildFragmentOne("Home");
        FragmentTwo dashboardFragment = buildFragmentTwo("Dashboard");
        FragmentThree notificationsFragment = buildFragmentThree("Notifications");

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

    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = (TextView) findViewById(0); //dont leave this as 0 pls pls
        tvError.setText(error);

        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }

    }

    private Bundle getTimeFromLabel(String text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split(":");
        int hour = 12;
        int minute = 0;

        if (comps.length == 2) {
            hour = Integer.parseInt(comps[0]);
            minute = Integer.parseInt(comps[1]);
        }

        rtn.putInt("hour", hour);
        rtn.putInt("minute", minute);

        return rtn;
    }

    private Bundle getDateFromLabel(String text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split("-");
        int day = 1;
        int month = 1;
        int year = 1;

        if (comps.length == 3) {
            day = Integer.parseInt(comps[0]);
            month = Integer.parseInt(comps[1]);
            year = Integer.parseInt(comps[2]);
        }

        rtn.putInt("day", day);
        rtn.putInt("month", month-1);
        rtn.putInt("year", year);

        return rtn;
    }

    public void showTimePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getTimeFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getDateFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setTime(int id, int h, int m) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d:%02d", h, m));
    }

    public void setDate(int id, int d, int m, int y) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d-%02d-%04d", d, m + 1, y));
    }



}
