package com.example.myapplication;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;

public class Preference extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference);
        loadSettings();
    }

    private void loadSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean check_night = sp.getBoolean("NIGHT", false);
        if (check_night) {
            getListView().setBackgroundColor(Color.parseColor("#222222"));
        } else {
            getListView().setBackgroundColor(Color.parseColor("#ffffff"));

        }
        CheckBoxPreference check_night_instant = (CheckBoxPreference) findPreference("NIGHT");
        check_night_instant.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference prefs, Object obj) {
                boolean yes = (boolean) obj;
                if (yes) {
                    getListView().setBackgroundColor(Color.parseColor("#222222"));
                } else {
                    getListView().setBackgroundColor(Color.parseColor("#ffffff"));
                }
                return true;

            }
        });

        ListPreference lp = (ListPreference) findPreference("ORIENTATION");
        String origin = sp.getString("ORIENTATION", "false");
        if ("1".equals(origin)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
            lp.setSummary(lp.getEntry());
        } else if ("2".equals(origin)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            lp.setSummary(lp.getEntry());
        } else if ("3".equals(origin)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            lp.setSummary(lp.getEntry());
        }

        lp.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference prefs, Object obj) {

                String items = (String) obj;
                if (prefs.getClass().equals("ORIENTATION")) {
                    switch (items) {
                        case "1":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);

                            break;

                        case "2":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;

                        case "3":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;
                    }
                    ListPreference lpp = (ListPreference) prefs;
                    lpp.setSummary(lpp.getEntries()[lpp.findIndexOfValue(items)]);
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        loadSettings();
        super.onResume();
    }
}
