package com.smartdesigns.smarthomehci;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.backend.RecyclerInterface;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.backend.Routine;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DevicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DevicesFragment extends Fragment {

    private Room room = null;
    private Routine routine = null;
    Response.Listener<List<Device>> devicesList;

    private OnFragmentInteractionListener mListener;
    RecyclerView devicesRecycler;

    public DevicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param room Room.
     * @return A new instance of fragment DevicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DevicesFragment newInstance(Room room) {
        DevicesFragment fragment = new DevicesFragment();
        Bundle args = new Bundle();
        args.putSerializable("Object", room);
        fragment.setArguments(args);
        return fragment;
    }

    private void addCards(Response.Listener<List<Device>> devicesList) {

        List devicesListAux = new ArrayList();
        devicesList.onResponse(devicesListAux);
        devicesListAux.add(new Device("25", "ESTE ES UN DISPOSITIVO", Integer.toString(R.drawable.blind)));

        RecyclerViewAdapter roomRecyclerAdapter = new RecyclerViewAdapter(this.getContext(), devicesListAux);
        devicesRecycler.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
        devicesRecycler.setAdapter(roomRecyclerAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Home.getInstance().getCurrentMode() == 0) {
            room = (Room) getArguments().getSerializable("Object");
            getActivity().setTitle(room.getName());
        } else if (Home.getInstance().getCurrentMode() == 1) {
            routine = (Routine) getArguments().getSerializable("Object");
            getActivity().setTitle(routine.getName());
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devices, container, false);

        if (room != null)
            getActivity().setTitle(room.getName());
        else
            getActivity().setTitle(routine.getName());

        devicesRecycler = view.findViewById(R.id.devices_recyclerview);

        FloatingActionButton playRoutineButton = (FloatingActionButton) view.findViewById(R.id.play_routine_button);
        playRoutineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //ACTIVAR RUTINA API
                Log.d("hola", "hola");
                //SI anda bien
                Toast.makeText(getActivity(), getResources().getString(R.string.apply_routine), Toast.LENGTH_LONG).show();
            }
        });

        if (Home.getInstance().getCurrentMode() != 1)
            playRoutineButton.setVisibility(View.GONE);


        Context appContext = getContext();
        ApiConnection api = ApiConnection.getInstance(appContext);
        devicesList = new Response.Listener<List<Device>>() {
            @Override
            public void onResponse(List<Device> response) {

            }
        };
        if (Home.getInstance().getCurrentMode() == 0)
            api.getRoomDevices(room, devicesList, null);
        else if (Home.getInstance().getCurrentMode() == 1) {
        }
        //getRoutineDevices();
        addCards(devicesList);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (room != null)
            getActivity().setTitle(room.getName());
        else
            getActivity().setTitle(routine.getName());
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
