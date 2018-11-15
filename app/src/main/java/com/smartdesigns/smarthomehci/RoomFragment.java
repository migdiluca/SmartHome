package com.smartdesigns.smarthomehci;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
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

    private OnFragmentInteractionListener mListener;
    RecyclerView roomRecycler;

    public RoomFragment() {
        // Required empty public constructor
    }

    private void addCards(Response.Listener<List<Room>> roomList) {

       List roomListAux = new ArrayList();
       roomList.onResponse(roomListAux);
       roomListAux.add(new Room("25","ES UN TEST","0"));

       RecyclerViewAdapter roomRecyclerAdapter = new RecyclerViewAdapter(this.getContext(), roomListAux);
       roomRecycler.setLayoutManager(new GridLayoutManager(this.getContext(),3));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_room, container, false);


        getActivity().setTitle(R.string.title_rooms);
        roomRecycler = view.findViewById(R.id.room_recyclerview);
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
    public void onResume(){
        super.onResume();
        getActivity().setTitle(R.string.title_rooms);
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
