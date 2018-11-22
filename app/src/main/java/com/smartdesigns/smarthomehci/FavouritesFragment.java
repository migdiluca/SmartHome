package com.smartdesigns.smarthomehci;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartdesigns.smarthomehci.Utils.FavouritesList;
import com.smartdesigns.smarthomehci.Utils.OnFragmentInteractionListener;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;
import com.smartdesigns.smarthomehci.Utils.RefreshFragment;
import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

public class FavouritesFragment extends RefreshFragment {

    private static FavouritesList favouritesList = null;
    List<Device> favListAux;

    private static final int AMOUNT_TO_SHOW = 10;

    private ActionBar toolbar;

    private TextView text;
    RecyclerView favouritesRecycler;
    private OnFragmentInteractionListener mListener;

    public static void access(Device device){
        loadList();
        favouritesList.access(device);
        saveList();
    }

    public void refresh(){
        addCards();
    }

    private static void loadList() {
        if(favouritesList == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Home.getInstance());
            Gson gson = new Gson();
            String json = preferences.getString("FavouriteList", null);
            favouritesList=gson.fromJson(json, FavouritesList.class);
            if(favouritesList == null) {
                favouritesList = new FavouritesList();
                saveList();
            }

        }
    }

    private static void saveList() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Home.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favouritesList);
        editor.putString("FavouriteList", json);
        editor.apply();
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

    private void addCards(){ ;
        ApiConnection api = ApiConnection.getInstance(getContext());
        api.getDevices(new Response.Listener<List<Device>>() {
            @Override
            public void onResponse(List<Device> response) {
                favListAux = favouritesList.getFavouritesDevices(AMOUNT_TO_SHOW, getContext());
                ArrayList<Device> devicesList = new ArrayList<>();
                for (Device device : response) {
                    devicesList.add(device);
                }
                for(int i = 0; i < favListAux.size(); i++) {
                    if(!devicesList.contains(favListAux.get(i)))
                        favListAux.remove(i);
                }

                if(favListAux.isEmpty())
                    text.setText(R.string.no_favorites_available);
                else
                    text.setText("");
                RecyclerViewAdapter favouritesRecyclerAdapter = new RecyclerViewAdapter(getContext(), favListAux);
                favouritesRecycler.setLayoutManager(new GridLayoutManager(getContext(), getColumns()));
                favouritesRecycler.setAdapter(favouritesRecyclerAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                text.setText("");
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = Home.getMainActionBar();
        toolbar.setTitle(R.string.title_favourites);
        loadList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        text = view.findViewById(R.id.fragment_text);
        loadList();

        favouritesRecycler = view.findViewById(R.id.recyclerview);
        toolbar.setTitle(R.string.title_favourites);

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
        addCards();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
