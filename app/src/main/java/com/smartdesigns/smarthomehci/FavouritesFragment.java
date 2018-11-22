package com.smartdesigns.smarthomehci;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.smartdesigns.smarthomehci.Utils.FavouritesList;
import com.smartdesigns.smarthomehci.Utils.OnFragmentInteractionListener;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;
import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private static FavouritesList favouritesList = null;

    private static final int AMOUNT_TO_SHOW = 10;

    private ActionBar toolbar;

    RecyclerView favouritesRecycler;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = Home.getMainActionBar();
        if(favouritesList == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            Gson gson = new Gson();
            String json = preferences.getString("FavouriteList", null);
            if(json == null) {
                favouritesList = new FavouritesList();
                saveList();
            }
            else
                favouritesList=gson.fromJson(json, FavouritesList.class);
        }
    }

    public static void access(Device device){
        if(favouritesList == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Home.getInstance());
            Gson gson = new Gson();
            String json = preferences.getString("FavouriteList", null);
            if(json == null) {
                favouritesList = new FavouritesList();
                saveList();
            }
            else
                favouritesList=gson.fromJson(json, FavouritesList.class);
        }
        favouritesList.access(device);
        saveList();
    }

    private static void saveList() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Home.getInstance().getApplicationContext());
        Gson gson = new Gson();
        String json = gson.toJson(favouritesList);
        preferences.edit().putString("FavouriteList", json);
        preferences.edit().commit();
    }

    private int getColumns() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerViewAdapter.setColumns(preferences.getString("columns_amount_potrait","2").charAt(0) - '0');
        }
        else {
            RecyclerViewAdapter.setColumns(preferences.getString("columns_amount_landscape","5").charAt(0) - '0');
        }
        return RecyclerViewAdapter.getColumns();
    }

    private void addCards() {
        RecyclerViewAdapter favouritesRecyclerAdapter = new RecyclerViewAdapter(this.getContext(), favouritesList.getFavouritesDevices(AMOUNT_TO_SHOW));
        Log.d("ELTAMAÑOES",Integer.toString(favouritesList.getFavouritesDevices(AMOUNT_TO_SHOW).size()));
        favouritesRecycler.setLayoutManager(new GridLayoutManager(this.getContext(), getColumns()));
        favouritesRecycler.setAdapter(favouritesRecyclerAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler, container, false);


        ApiConnection api = ApiConnection.getInstance(getContext());


        favouritesRecycler = view.findViewById(R.id.recyclerview);
        toolbar.setTitle(R.string.title_favourites);
        addCards();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("On resume", "here");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
