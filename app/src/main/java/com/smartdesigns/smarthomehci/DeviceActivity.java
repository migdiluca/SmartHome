package com.smartdesigns.smarthomehci;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.smartdesigns.smarthomehci.backend.Device;


public class DeviceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Device device = (Device) getIntent().getSerializableExtra("Device");
        Toolbar toolbar = findViewById(R.id.activity_fragment_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(device.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        Devices fragment = new Devices();

        Bundle arguments = new Bundle();
        arguments.putSerializable("Device", device);
        fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_fragment_frame, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
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
}
