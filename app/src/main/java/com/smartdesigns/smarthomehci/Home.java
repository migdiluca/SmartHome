package com.smartdesigns.smarthomehci;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.volley.Response;
import com.smartdesigns.smarthomehci.backend.Room;

import com.smartdesigns.smarthomehci.Utils.BottomNavigationViewHelper;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity implements DevicesFragment.OnFragmentInteractionListener, RoomFragment.OnFragmentInteractionListener {

    private FrameLayout mMainFrame;

    private RoomFragment roomFragment;
    private RoutinesFragment routinesFragment;
    static private Home homeInstance = null;

    public static Home getInstance() {
        return homeInstance;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(roomFragment);
                    return true;
                case R.id.navigation_dashboard:
                    setFragment(routinesFragment);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    protected void setFragmentWithStack(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);


        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        FragmentManager manager = this.getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if(!fragmentPopped) {
            Log.d("as", "asd");
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commit();
    }

    protected void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeInstance = this;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);

        roomFragment = new RoomFragment();
        routinesFragment = new RoutinesFragment();

        setFragment(roomFragment);
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
