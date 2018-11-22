package com.smartdesigns.smarthomehci;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartdesigns.smarthomehci.Utils.OnFragmentInteractionListener;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;
import com.smartdesigns.smarthomehci.backend.Device;
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
    private List<Device> devicesList = new ArrayList<>();
    private ActionBar toolbar = Home.getMainActionBar();

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
        RecyclerViewAdapter devicesRecyclerAdapter = new RecyclerViewAdapter(this.getContext(), devicesList);
        devicesRecycler.setLayoutManager(new GridLayoutManager(this.getContext(), getColumns()));
        devicesRecycler.setAdapter(devicesRecyclerAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Home.getInstance().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Home.getInstance().getSupportActionBar().setHomeButtonEnabled(true);

        if (Home.getInstance().getCurrentMode() == 0) {
            room = (Room) getArguments().getSerializable("Object");
            getActivity().setTitle(room.getName());
        } else if (Home.getInstance().getCurrentMode() == 1) {
            routine = (Routine) getArguments().getSerializable("Object");
            getActivity().setTitle(routine.getName());
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Home.getInstance().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context appContext = getContext();
        ApiConnection api = ApiConnection.getInstance(appContext);

        View view = inflater.inflate(R.layout.fragment_devices, container, false);
        setBackgroundColor(view);

        if (room != null)
            toolbar.setTitle(room.getName());
        else
            toolbar.setTitle(routine.getName());

        devicesRecycler = view.findViewById(R.id.devices_recyclerview);

        FloatingActionButton playRoutineButton = (FloatingActionButton) view.findViewById(R.id.play_routine_button);
        playRoutineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Context appContext = getContext();
                ApiConnection api = ApiConnection.getInstance(appContext);

                api.executeRoutine(routine, new Response.Listener<Object>() {
                    @Override
                    public void onResponse(Object response) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.apply_routine), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

            }
        });

        if (Home.getInstance().getCurrentMode() != 1) {
            playRoutineButton.setVisibility(View.GONE);

        }


        if (Home.getInstance().getCurrentMode() == 0){
            api.getRoomDevices(room, new Response.Listener<List<Device>>() {
                @Override
                public void onResponse(List<Device> response) {
                    for (Device device : response) {
                        if (!devicesList.contains(device))
                            devicesList.add(device);
                        if (device.getMeta().matches("\"background\"") == false) {
                            int aux = device.getBackground();
                            ApiConnection.getInstance(getContext()).updateDevice(device, new Response.Listener<Boolean>() {
                                @Override
                                public void onResponse(Boolean response) {

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("LOADINGDEVICES", error.toString());
                                }
                            });
                        }

                    }

                    addCards();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }else if (Home.getInstance().getCurrentMode() == 1) {
            devicesList = new ArrayList<>();
            api.getDevices(new Response.Listener<List<Device>>() {
                @Override
                public void onResponse(List<Device> response) {
                    for(Device device: response) {
                        if(routine.containsDevice(device)) {
                            devicesList.add(device);
                            if (device.getMeta().matches("\"background\"") == false) {
                                int aux = device.getBackground();
                                ApiConnection.getInstance(getContext()).updateDevice(device, new Response.Listener<Boolean>() {
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
                    }

                    addCards();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("LOADINGDEVICES", error.toString());
                }
            });
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void setBackgroundColor(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean darkTheme = preferences.getBoolean("dark_theme_checkbox",false);
        if(darkTheme == true) {
            Home.getInstance().setTheme(AppCompatDelegate.MODE_NIGHT_YES);
            view.setBackgroundColor(getResources().getColor(R.color.black));
            Home.setNavColor(R.color.dark_grey);
            toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dark_grey)));
            Home.getInstance().getWindow().setStatusBarColor(getResources().getColor(R.color.dark_grey_navbar));
        } else if(getView() != null) {
            Home.getInstance().setTheme(AppCompatDelegate.MODE_NIGHT_NO);
            view.setBackgroundColor(getResources().getColor(R.color.white));
            Home.setNavColor(R.color.colorPrimary);
            toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            Home.getInstance().getWindow().setStatusBarColor(getResources().getColor(R.color.dark_grey));
            toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
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

    @Override
    public void onResume() {
        super.onResume();
        if (room != null)
            getActivity().setTitle(room.getName());
        else
            getActivity().setTitle(routine.getName());
        setBackgroundColor(getView());
        addCards();
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
