package com.example.diego.puntajepsu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.Hashtable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TablaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TablaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TablaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NUM_PREGUNTAS = "numPreguntas";
    private static final String ARG_ENSAYO = "ensayo";
    private static final String ARG_NOTA_MAX = "notaMax";
    private static final String ARG_NOTA_MIN = "notaMin";
    private static final String ARG_NOTA_APR = "notaApr";
    private static final String ARG_EXIGENCIA = "exigencia";

    private int numPreguntas;
    private String ensayo;
    private int notaMax,notaMin,notaApr,exigencia;

    private DiscreteScrollView scrollView;
    private CarouselAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    private Hashtable<String,int[]> ensayosHash;

    int [] cien = {150,160,169,179,188,198,211,223,234,245,264,285,306,326,345,365,382,399,413,428,441,453,464,475,484,492,500,508,515,521,527,533,539,544,550,555,560,565,570,575,579,583,588,592,596,600,605,609,613,617,621,625,629,633,638,642,646,650,654,659,663,668,673,678,684,689,695,701,707,714,721,730,739,750,764,777,792,807,822,836,850};
    int [] hist = {150,158,165,173,180,188,195,203,211,221,237,261,284,305,324,343,360,376,391,404,417,429,441,451,460,468,476,484,491,499,505,511,518,524,530,536,541,546,553,557,563,568,573,578,583,589,594,599,604,609,615,620,625,631,636,642,648,654,660,666,673,680,688,696,704,713,723,734,746,758,773,789,804,819,835,850};
    int [] leng = {150,156,161,167,173,178,184,191,196,202,208,213,222,238,254,270,286,299,312,325,337,348,359,370,379,389,398,407,416,423,432,440,447,454,462,469,476,483,489,497,504,510,517,524,530,537,543,551,558,565,572,580,587,595,604,611,620,629,639,649,659,670,683,695,709,724,740,760,773,785,797,808,821,833,839,850};
    int [] mate = {150,160,170,180,190,204,217,235,253,277,301,324,347,368,388,407,425,440,454,467,479,490,499,508,516,523,529,535,541,547,551,557,562,566,571,575,580,584,588,593,596,601,605,608,613,616,621,625,629,633,637,641,646,650,655,659,664,668,673,678,683,689,694,700,706,713,721,728,737,746,760,778,796,814,832,850};

    public TablaFragment() {
        // Required empty public constructor
    }


    public static TablaFragment newInstance(int numPreguntas, String ensayo) {
        TablaFragment fragment = new TablaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUM_PREGUNTAS, numPreguntas);
        args.putString(ARG_ENSAYO, ensayo);
        fragment.setArguments(args);
        return fragment;
    }
    public static TablaFragment newInstance(int numPreguntas, String ensayo, int notaMax, int notaMin, int notaApr, int exigencia) {
        TablaFragment fragment = new TablaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUM_PREGUNTAS, numPreguntas);
        args.putString(ARG_ENSAYO, ensayo);
        args.putInt(ARG_NOTA_MAX, notaMax);
        args.putInt(ARG_NOTA_MIN, notaMin);
        args.putInt(ARG_NOTA_APR, notaApr);
        args.putInt(ARG_EXIGENCIA, exigencia);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numPreguntas = getArguments().getInt(ARG_NUM_PREGUNTAS);
            ensayo = getArguments().getString(ARG_ENSAYO);
            notaMax = getArguments().getInt(ARG_NOTA_MAX);
            notaMin  = getArguments().getInt(ARG_NOTA_MIN);
            notaApr = getArguments().getInt(ARG_NOTA_APR);
            exigencia = getArguments().getInt(ARG_EXIGENCIA);
        }
        ensayosHash = new Hashtable<>();
        ensayosHash.put("Ciencias", cien);
        ensayosHash.put("Historia", hist);
        ensayosHash.put("Lenguaje", leng);
        ensayosHash.put("Matemática",mate);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tabla, container, false);
        scrollView = view.findViewById(R.id.carousel);
        if(notaMax > 0) mAdapter = new CarouselAdapter(ensayosHash.get(ensayo),numPreguntas, notaMax,notaMin,notaApr,exigencia, ensayo);
        else {
            mAdapter = new CarouselAdapter(ensayosHash.get(ensayo), numPreguntas, ensayo);
        }
        scrollView.setAdapter(mAdapter);
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
