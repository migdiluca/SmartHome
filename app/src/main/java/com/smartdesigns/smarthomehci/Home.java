package com.smartdesigns.smarthomehci;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.smartdesigns.smarthomehci.Utils.OnFragmentInteractionListener;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;

import java.util.Stack;


public class Home extends AppCompatActivity implements OnFragmentInteractionListener {

    private FrameLayout mMainFrame;

    private static Stack<Fragment> bottomStacks[] = new Stack[3];

    private static int currentMode = 0;
    static private Home homeInstance = null;

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

    public void setFragment(Fragment fragment){
        bottomStacks[currentMode].push(fragment);

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return;
        }
        homeInstance = this;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);


        RoomFragment roomFragment = new RoomFragment();
        RoutinesFragment routinesFragment = new RoutinesFragment();
        FavouritesFragment favouritesFragment = new FavouritesFragment();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerViewAdapter.setColumns(3);
        }
        else {
            RecyclerViewAdapter.setColumns(5);
        }


        if(bottomStacks[0] == null){
            for(int i = 0; i<3; i++) {
                bottomStacks[i] = new Stack<>();
            }
        }

        if(!bottomStacks[currentMode].empty()) {
            setFragment(bottomStacks[currentMode].peek());
            return;
        }
        else {
            bottomStacks[0].push(roomFragment);
            bottomStacks[1].push(routinesFragment);
            bottomStacks[2].push(favouritesFragment);

            setFragment(bottomStacks[currentMode].pop());
        }
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

    private void endApp() {
        Intent intent = new Intent(this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
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
