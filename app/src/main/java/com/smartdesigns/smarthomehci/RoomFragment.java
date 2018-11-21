package com.smartdesigns.smarthomehci;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartdesigns.smarthomehci.Utils.OnFragmentInteractionListener;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;


public class RoomFragment extends Fragment {

    private static List<Room> roomList = new ArrayList<>();
    private static Room currentRoom;

    private OnFragmentInteractionListener mListener;
    RecyclerView roomRecycler;

    public static Room getCurrentRoutine() {
        return currentRoom;
    }

    public static void setCurrentRoutine(Room routine) {
        currentRoom = routine;
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

        //roomList.add(new Room("nmasd",""));
        RecyclerViewAdapter roomRecyclerAdapter = new RecyclerViewAdapter(this.getContext(), roomList);
        roomRecycler.setLayoutManager(new GridLayoutManager(this.getContext(), getColumns()));
        roomRecycler.setAdapter(roomRecyclerAdapter);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        roomRecycler = view.findViewById(R.id.recyclerview);
        getActivity().setTitle(R.string.title_rooms);


        Log.d("ROOMSIZEASD", "asd");
        ApiConnection api = ApiConnection.getInstance(getContext());
        api.getRooms(new Response.Listener<List<Room>>() {
            @Override
            public void onResponse(List<Room> response) {
                Log.d("ROOMSIZEASD", Integer.toString(response.size()));
                for(Room room: response) {
                    if(!roomList.contains(room))
                        roomList.add(room);
                    addCards();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERRORLOADING ROOMS", error.toString());
            }
        });
        setBackgroundColor(view);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setBackgroundColor(getView());
        addCards();
    }

    private void setBackgroundColor(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean darkTheme = preferences.getBoolean("dark_theme_checkbox",false);
        if(darkTheme == true) {
            Home.getInstance().setTheme(AppCompatDelegate.MODE_NIGHT_YES);
            view.setBackgroundColor(getResources().getColor(R.color.black));
            Home.setNavColor(R.color.dark_grey);
            Home.getInstance().getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dark_grey)));
            Home.getInstance().getWindow().setStatusBarColor(getResources().getColor(R.color.dark_grey_navbar));
        } else if(getView() != null) {
            Home.getInstance().setTheme(AppCompatDelegate.MODE_NIGHT_NO);
            view.setBackgroundColor(getResources().getColor(R.color.white));
            Home.setNavColor(R.color.colorPrimary);
            Home.getInstance().getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            Home.getInstance().getWindow().setStatusBarColor(getResources().getColor(R.color.dark_grey));
            Home.getInstance().getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            Home.getInstance().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



}
