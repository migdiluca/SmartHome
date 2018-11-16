package com.smartdesigns.smarthomehci;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.smartdesigns.smarthomehci.Utils.OnFragmentInteractionListener;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomFragment extends Fragment {

    Response.Listener<List<Room>> roomList;
    private static Room currentRoom;

    private OnFragmentInteractionListener mListener;
    RecyclerView roomRecycler;

    public RoomFragment() {
        // Required empty public constructor
    }

    public static Room getCurrentRoutine() {
        return currentRoom;
    }

    public static void setCurrentRoutine(Room routine) {
        currentRoom = routine;
    }

    private void addCards(Response.Listener<List<Room>> roomList) {

        List roomListAux = new ArrayList();
        roomList.onResponse(roomListAux);
        roomListAux.add(new Room("25", "ES UN TEST", "0"));
        roomListAux.add(new Room("25", "ES UN TEST", "0"));
        roomListAux.add(new Room("25", "ES UN TEST", "0"));
        roomListAux.add(new Room("25", "ES UN TEST", "0"));
        roomListAux.add(new Room("25", "ES UN TEST", "0"));

        RecyclerViewAdapter roomRecyclerAdapter = new RecyclerViewAdapter(this.getContext(), roomListAux);
        roomRecycler.setLayoutManager(new GridLayoutManager(this.getContext(), roomRecyclerAdapter.getColumns()));
        roomRecycler.setAdapter(roomRecyclerAdapter);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomFragment newInstance() {
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_room, container, false);

        setBackgroundColor(view);

        roomRecycler = view.findViewById(R.id.room_recyclerview);
        getActivity().setTitle(R.string.title_rooms);
        Context appContext = getContext();
        ApiConnection api = ApiConnection.getInstance(appContext);
        roomList = new Response.Listener<List<Room>>() {
            @Override
            public void onResponse(List<Room> response) {

            }
        };
        api.getRooms(roomList, null);
        addCards(roomList);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
