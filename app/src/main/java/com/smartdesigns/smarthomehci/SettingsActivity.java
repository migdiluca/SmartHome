package com.smartdesigns.smarthomehci;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainSettingsFragment()).commit();

    }

    public static class MainSettingsFragment extends PreferenceFragment {

            @Override
            public void onCreate(Bundle savedInstanceState){
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.preferences);
                Log.d("IM HERE!", Boolean.toString(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("dark_theme_checkbox",false)));
                Log.d("Preferece added",Boolean.toString(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("alarm_preference", false)));
                Log.d("Preferece added",Boolean.toString(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("ac_preference", false)));
                Log.d("Preferece added",Boolean.toString(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("blinds_preference", false)));
                Log.d("Preferece added",Boolean.toString(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("door_preference", false)));
                Log.d("Preferece added",Boolean.toString(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("lamp_preference", false)));
                Log.d("Preferece added",Boolean.toString(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("oven_preference", false)));
                Log.d("Preferece added",Boolean.toString(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("refrigerator_preference", false)));
                Log.d("Preferece added",Boolean.toString(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("timer_preference", false)));
            }

    }
}
