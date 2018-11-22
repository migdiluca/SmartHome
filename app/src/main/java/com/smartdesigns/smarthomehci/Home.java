package com.smartdesigns.smarthomehci;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.smartdesigns.smarthomehci.Utils.OnFragmentInteractionListener;
import com.smartdesigns.smarthomehci.backend.Device;

import java.util.Stack;


public class Home extends AppCompatActivity implements OnFragmentInteractionListener {

    private FrameLayout mMainFrame;

    private static Stack<Fragment> bottomStacks[] = new Stack[3];

    private static int currentMode = 0;
    static private Home homeInstance = null;

    private SwipeRefreshLayout sr;
    private static Toolbar mainToolbar;
    private static Toolbar deviceToolbar;

    public static Home getInstance() {
        return homeInstance;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_rooms:
                    currentMode = 0;
                    if(bottomStacks[currentMode].empty()){
                        endApp();
                        return true;
                    }
                    setFragment(bottomStacks[currentMode].pop());
                    return true;
                case R.id.navigation_routines:
                    currentMode = 1;
                    if(bottomStacks[currentMode].empty()){
                        endApp();
                        return true;
                    }
                    setFragment(bottomStacks[currentMode].pop());
                    return true;
                case R.id.navigation_cameras:
                    currentMode = 2;
                    if(bottomStacks[currentMode].empty()){
                        endApp();
                        return true;
                    }
                    setFragment(bottomStacks[currentMode].pop());
                    return true;
            }
            return false;
        }
    };

    public static void setNavColor(int color){
        BottomNavigationView navigation = (BottomNavigationView) homeInstance.findViewById(R.id.navigation);
        navigation.setBackgroundColor(homeInstance.getResources().getColor(color));

    }

    public void setDeviceFragment( Device device){

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            Devices fragment = new Devices();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            Bundle arguments = new Bundle();
            arguments.putSerializable("Device", device);
            fragment.setArguments(arguments);
            fragmentTransaction.replace(R.id.device_frame, fragment);
            fragmentTransaction.commit();
            deviceToolbar.setTitle(device.getName());
        } else {
            Intent intent = new Intent(getBaseContext(), DeviceActivity.class);
            intent.putExtra("Device",device);
            startActivity(intent);
        }
    }

    public void setFragment(Fragment fragment){
        bottomStacks[currentMode].push((Fragment)fragment);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static android.support.v7.app.ActionBar getMainActionBar(){
        return Home.getInstance().getSupportActionBar();
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
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return;
        }
        homeInstance = this;

        mainToolbar = findViewById(R.id.main_toolbar);
        if(getSupportActionBar() == null) {
            setSupportActionBar(mainToolbar);
        }

        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            deviceToolbar = findViewById(R.id.device_toolbar);
            deviceToolbar.setTitle(R.string.title_device);
        } else {
            deviceToolbar = mainToolbar;
        }

        mainToolbar.inflateMenu(R.menu.up_menu);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        setEvents();

        loadFragments();
        Intent intent = new Intent(Home.this, NotificationBroadcastReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        long interval;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String intervalPreference = sharedPreferences.getString("interval_preference","1 min");

        if(intervalPreference.equals("1 min") ){
            interval = 1;
        }else if(intervalPreference.equals("30 min")){
            interval = AlarmManager.INTERVAL_HALF_HOUR;
        }else{
            interval = AlarmManager.INTERVAL_HOUR;
        }
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+ interval,interval, alarmIntent);
    }

    private void loadFragments() {
        RoomFragment roomFragment = new RoomFragment();
        RoutinesFragment routinesFragment = new RoutinesFragment();
        FavouritesFragment favouritesFragment = new FavouritesFragment();


        if(bottomStacks[0] == null){
            for(int i = 0; i<3; i++) {
                bottomStacks[i] = new Stack<>();
            }
        }

        if(!bottomStacks[currentMode].empty()) {
            setFragment(bottomStacks[currentMode].pop());
            return;
        }
        else {
            bottomStacks[0].push(roomFragment);
            bottomStacks[1].push(routinesFragment);
            bottomStacks[2].push(favouritesFragment);

            setFragment(bottomStacks[currentMode].pop());
        }
    }

    private void setEvents() {
        sr = findViewById(R.id.swiperefresh);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                sr.setRefreshing(false);
            }
        });
    }

    private void endApp() {
        Intent intent = new Intent(this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        currentMode = 0;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(bottomStacks[currentMode].size() <= 1){
            endApp();
        }
        else {
            bottomStacks[currentMode].pop();
            Fragment back = bottomStacks[currentMode].pop();
            setFragment(back);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.up_menu, menu);

//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public void settings_onClick(MenuItem item) {
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.app_bar_search) {
//            //Agregar funcionalidad del search
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }



}
