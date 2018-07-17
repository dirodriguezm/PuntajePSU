package com.example.diego.puntajepsu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MisEnsayosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MisEnsayosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisEnsayosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ENSAYOS = "ensayos";

    // TODO: Rename and change types of parameters
    private ArrayList<Ensayo> ensayos;

    private DiscreteScrollView scrollView;
    private CarouselAdapter mAdapter;


    private OnFragmentInteractionListener mListener;

    public MisEnsayosFragment() {
        // Required empty public constructor
    }



    public static MisEnsayosFragment newInstance(ArrayList<? extends Parcelable> ensayos) {
        MisEnsayosFragment fragment = new MisEnsayosFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ENSAYOS, ensayos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ensayos = getArguments().getParcelableArrayList(ARG_ENSAYOS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_mis_ensayos, container, false);
        scrollView = view.findViewById(R.id.carousel2);
        scrollView.setAdapter(new MisEnsayosCarouselAdapter(ensayos, this.getContext()));
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
