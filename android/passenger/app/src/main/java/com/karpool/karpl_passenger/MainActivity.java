package com.karpool.karpl_passenger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    //List that holds the fragments
    private List<Fragment> fragments = new ArrayList<>(3);

    private static final String TAG_FRAGMENT_HOME = "tag_frag_home";
    private static final String TAG_FRAGMENT_DASHBOARD = "tag_frag_dashboard";
    private static final String TAG_FRAGMENT_NOTIFICATIONS = "tag_frag_notifications";



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

}
