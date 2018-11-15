package com.smartdesigns.smarthomehci;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.smartdesigns.smarthomehci.Utils.OnFragmentInteractionListener;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;
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

    private Response.Listener<List<Routine>> routineList;
    private RecyclerView routineRecycler;

    private static Routine currentRoutine;

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

    private void addCards(Response.Listener<List<Routine>> routineList) {

        List routineListAux = new ArrayList();
        routineList.onResponse(routineListAux);
        routineListAux.add(new Routine("25",null,"0"));

        RecyclerViewAdapter routineRecyclerAdapter = new RecyclerViewAdapter(this.getContext(), routineListAux);
        routineRecycler.setLayoutManager(new GridLayoutManager(this.getContext(),3));
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routines, container, false);

        routineRecycler = view.findViewById(R.id.routine_recyclerview);
        getActivity().setTitle(R.string.title_routines);
        Context appContext = getContext();
        ApiConnection api = ApiConnection.getInstance(appContext);
        routineList = new Response.Listener<List<Routine>>() {
            @Override
            public void onResponse(List<Routine> response) {

            }
        };
        api.getRoutines();
        addCards(routineList);
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
