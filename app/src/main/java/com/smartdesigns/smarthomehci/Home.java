package com.smartdesigns.smarthomehci;

import android.content.Context;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.volley.Response;
import com.smartdesigns.smarthomehci.backend.Room;

import com.smartdesigns.smarthomehci.Utils.BottomNavigationViewHelper;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity {


    Response.Listener<List<Room>> roomList;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent newActivity;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    newActivity = new Intent(Home.this, RoutinesActivity.class);
                    newActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    newActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newActivity);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };


    private void addCards(Response.Listener<List<Room>> roomList) {

        List roomListAux = new ArrayList();
        roomList.onResponse(roomListAux);

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, roomListAux);
        myrv.setLayoutManager(new GridLayoutManager(this,3));
        myrv.setAdapter(myAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setSelectedItemId(R.id.navigation_home);

        Context appContext = getApplicationContext();
        ApiConnection api = ApiConnection.getInstance(appContext);
        roomList = new Response.Listener<List<Room>>() {
            @Override
            public void onResponse(List<Room> response) {

            }
        };
        api.getRooms(roomList, null);
        addCards(roomList);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(0,0);
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
