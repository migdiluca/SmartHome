package com.smartdesigns.smarthomehci;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

/**
 * A {@link PreferenceActivity} that presents a set of application activity_fragment. On
 * handset devices, activity_fragment are presented as a single list. On tablets,
 * activity_fragment are split by category, with category headers shown to the left of
 * the list of activity_fragment.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Toolbar toolbar = findViewById(R.id.activity_fragment_toolbar);
        (toolbar).setTitle(R.string.title_activity_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        getFragmentManager().beginTransaction().replace(R.id.activity_fragment_frame, new MainSettingsFragment()).commit();
    }

    private void setTheme(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean darkTheme = preferences.getBoolean("dark_theme_checkbox",false);
        if(darkTheme == true) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public static class MainSettingsFragment extends PreferenceFragment {

            @Override
            public void onCreate(Bundle savedInstanceState){
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.preferences);
            }

    }

    @Override
    public void onResume() {
        super.onResume();
        Home.activityResumed();
        Home.setCurrentClass(SettingsActivity.class);
    }

    @Override
    public void onPause() {
        super.onPause();
        Home.activityPaused();
    }
}
