package com.example.diego.puntajepsu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MisEnsayosFragment.OnFragmentInteractionListener, EscojerEnsayoFragment.OnFragmentInteractionListener, TablaFragment.OnFragmentInteractionListener{


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction.replace(R.id.fragment_container, MisEnsayosFragment.newInstance(ensayos));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_tabla:
                    fragmentTransaction.replace(R.id.fragment_container, EscojerEnsayoFragment.newInstance());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
            }

            return false;
        }
    };
    private static FragmentManager fragmentManager;
    private static FragmentTransaction fragmentTransaction;
    private ArrayList<Ensayo> ensayos;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("LLAMANDO", "AAAAh");
        db = AppDatabase.getAppDatabase(this);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ensayos = getEnsayos();
        guardarEnsayo();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, MisEnsayosFragment.newInstance(ensayos));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public ArrayList<Ensayo> getEnsayos(){
        final ArrayList<Ensayo> ensayos = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Ensayo> ensayoList = db.ensayoDao().getAll();
                Collections.sort(ensayoList);
               for(Ensayo e: ensayoList){
                   ensayos.add(e);
               }
            }
        }).start();
        return ensayos;
    }

    public void guardarEnsayo(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            Calendar fecha = (Calendar) bundle.get("fecha");
            String ensayo = bundle.getString("ensayo");
            int puntaje = bundle.getInt("puntaje");
            final Ensayo nuevo = new Ensayo(ensayo, fecha, puntaje);
            ensayos.add(nuevo);
            Collections.sort(ensayos);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.ensayoDao().insertEnsayo(nuevo);

                }
            }).start();

        }
    }



    @Override
    public void onFragmentInteraction(int numPreguntas, String ensayo, boolean calcularNota) {
        if(calcularNota){
            DialogFragment newFragment = new FireMissilesDialogFragment();
            newFragment.show(getSupportFragmentManager(),"parametrosNota");
            Bundle args = new Bundle();
            args.putInt("numPreguntas",numPreguntas);
            args.putString("ensayo",ensayo);
            newFragment.setArguments(args);
        }
        else{
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, TablaFragment.newInstance(numPreguntas,ensayo));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void agregarEnsayo(View v){
        Intent intent = new Intent(this, AgregarEnsayoActivity.class);
        startActivity(intent);
    }

    public static class FireMissilesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            // Use the Builder class for convenient dialog construction
            final int numPreguntas = getArguments().getInt("numPreguntas");
            final String ensayo = getArguments().getString("ensayo");
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.parametros_nota,null);
            final NumberPicker notaMax = view.findViewById(R.id.notaMax);
            final NumberPicker notaMin = view.findViewById(R.id.notaMin);
            final NumberPicker notaApr = view.findViewById(R.id.notaApr);
            final NumberPicker exigencia = view.findViewById(R.id.exigencia);
            notaMax.setMinValue(1);
            notaMax.setMaxValue(7);
            notaMin.setMinValue(1);
            notaMin.setMaxValue(7);
            notaApr.setMinValue(1);
            notaApr.setMaxValue(7);
            exigencia.setMinValue(1);
            exigencia.setMaxValue(100);
            notaMax.setValue(7);
            notaMin.setValue(1);
            notaApr.setValue(4);
            exigencia.setValue(60);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Parametros de nota").setView(view)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, TablaFragment.newInstance(numPreguntas,ensayo, notaMax.getValue(), notaMin.getValue(), notaApr.getValue(), exigencia.getValue()));
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
