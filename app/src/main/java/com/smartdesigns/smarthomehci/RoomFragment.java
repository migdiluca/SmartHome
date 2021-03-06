package com.smartdesigns.smarthomehci;

import android.annotation.SuppressLint;
import android.app.Application;
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
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.smartdesigns.smarthomehci.Utils.OnFragmentInteractionListener;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;
import com.smartdesigns.smarthomehci.Utils.RefreshFragment;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.util.ArrayList;
import java.util.List;

public class RoomFragment extends RefreshFragment {

    private static List<Room> roomList;
    private static Room currentRoom;

    private TextView text;
    private ActionBar toolbar;
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerViewAdapter.setColumns(preferences.getString("columns_amount_potrait", "2").charAt(0) - '0');
        } else {
            RecyclerViewAdapter.setColumns(preferences.getString("columns_amount_landscape", "5").charAt(0) - '0');
        }
        return RecyclerViewAdapter.getColumns();
    }

    private void addCards() {
        RecyclerViewAdapter roomRecyclerAdapter = new RecyclerViewAdapter(Home.getInstance(), roomList);
        roomRecycler.setLayoutManager(new GridLayoutManager(Home.getInstance(), getColumns()));
        roomRecycler.setAdapter(roomRecyclerAdapter);
    }

    public void refresh() {
        roomList = new ArrayList<>();
        addCards();
        text.setText(R.string.loading);

        ApiConnection api = ApiConnection.getInstance(getContext());
        api.getRooms(new Response.Listener<List<Room>>() {
            @Override
            public void onResponse(List<Room> response) {

                for (Room room : response) {
                        roomList.add(room);


                        if (room.getMeta().matches("\"background\"") == false) {
                            int aux = room.getBackground();
                            ApiConnection.getInstance(getContext()).updateRoom(room, new Response.Listener<Boolean>() {
                                @Override
                                public void onResponse(Boolean response) {

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                        }

                }

                if(roomList.isEmpty())
                    text.setText(R.string.no_rooms_available);
                else
                    text.setText("");
                addCards();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                text.setText(R.string.no_rooms_error);
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = Home.getMainActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        text = view.findViewById(R.id.fragment_text);
        text.setText(R.string.loading);
        roomRecycler = view.findViewById(R.id.recyclerview);
        toolbar.setTitle(R.string.title_rooms);

        return view;
    }

    @Override
    public void onResume() {
        toolbar.setTitle(R.string.title_rooms);
        super.onResume();
        refresh();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
