package com.smartdesigns.smarthomehci;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;

import com.smartdesigns.smarthomehci.Utils.OnFragmentInteractionListener;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;

import java.util.Stack;


public class Home extends AppCompatActivity implements OnFragmentInteractionListener {

    private FrameLayout mMainFrame;


    private static Stack<Fragment> bottomStacks[] = new Stack[5];

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

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerViewAdapter.setColumns(3);
        }
        else {
            RecyclerViewAdapter.setColumns(5);
        }


        if(bottomStacks[0] == null){
            for(int i = 0; i<5; i++) {
                bottomStacks[i] = new Stack<>();
            }
        }

        if(!bottomStacks[currentMode].empty()) {
            setFragment(bottomStacks[currentMode].peek());
            return;
        }
        else if(currentMode == 0) {
            setFragment(roomFragment);
            bottomStacks[1].push(routinesFragment);
            return;
        }
        else if (currentMode == 1) {
            setFragment(routinesFragment);
            bottomStacks[0].push(roomFragment);
            return;
        }

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
        Log.d("ENTRE", "SI");
        Log.d("SIZE", Integer.toString(bottomStacks[currentMode].size()));
        if(bottomStacks[currentMode].size() <= 1){
            endApp();
        }
        else {
            Log.d("poppiong", "pop");
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
