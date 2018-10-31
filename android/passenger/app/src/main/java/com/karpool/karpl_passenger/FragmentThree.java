package com.karpool.karpl_passenger;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;



public class FragmentThree extends PreferenceFragmentCompat {


    private final static String KEY_LOCATION = "userLocation";
    private final static String KEY_BUTTON = "signOut";


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.pref_general, rootKey);



        Preference button = findPreference(KEY_BUTTON);
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // TODO I will implement please fix database...
                return true;
            }
        });

        final Preference pref = getPreferenceManager().findPreference(KEY_LOCATION);
        pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object location) {
                pref.setSummary(location.toString());
                return true;
            }
        });
    }



}
