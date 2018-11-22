package com.smartdesigns.smarthomehci;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
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
import com.smartdesigns.smarthomehci.Utils.OnFragmentInteractionListener;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.backend.Routine;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RoutinesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutinesFragment extends Fragment {

    private List<Routine> routineList = new ArrayList<>();
    private RecyclerView routineRecycler;

    private static Routine currentRoutine;

    private ActionBar toolbar;

    private OnFragmentInteractionListener mListener;

    public RoutinesFragment() {
        // Required empty public constructor
    }

    public static Routine getCurrentRoutine() {
        return currentRoutine;
    }

    public static void setCurrentRoutine(Routine routine) {
        currentRoutine = routine;
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
        RecyclerViewAdapter routineRecyclerAdapter = new RecyclerViewAdapter(this.getContext(), routineList);
        routineRecycler.setLayoutManager(new GridLayoutManager(this.getContext(),getColumns()));
        routineRecycler.setAdapter(routineRecyclerAdapter);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoutinesFragment.
     */
    public static RoutinesFragment newInstance(String param1, String param2) {
        RoutinesFragment fragment = new RoutinesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = Home.getMainActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        routineRecycler = view.findViewById(R.id.recyclerview);
        toolbar.setTitle(R.string.title_routines);
        Context appContext = getContext();
        ApiConnection api = ApiConnection.getInstance(appContext);

        routineList = new ArrayList<>();
        api.getRoutines(new Response.Listener<List<Routine>>() {
            @Override
            public void onResponse(List<Routine> response) {
                for(Routine routine: response) {
                    if (!routineList.contains(routine)) {
                        routineList.add(routine);
                        if (routine.getMeta().matches("\"background\"") == false) {
                            int aux = routine.getBackground();
                            ApiConnection.getInstance(getContext()).updateRoutine(routine, new Response.Listener<Boolean>() {
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
                Log.d("LOADINGROUTINES", error.toString());
            }
        });
        addCards();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addCards();
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
