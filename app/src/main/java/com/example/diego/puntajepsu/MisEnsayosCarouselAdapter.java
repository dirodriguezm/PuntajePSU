package com.example.diego.puntajepsu;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class MisEnsayosCarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ArrayList<Ensayo> ensayos;
    private static Context context;
    private static String ensayo;
    MisEnsayosCarouselAdapter.ViewHolder viewHolder;
    MisEnsayosCarouselAdapter.ViewHolder2 viewHolder2;
    public MisEnsayosCarouselAdapter(ArrayList<Ensayo> ensayos, Context context){
        this.ensayos = ensayos;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public RecyclerView mRecyclerView;
        public RecyclerView.LayoutManager mLayoutManager;
        public MisEnsayosTableAdapter mAdapter;
        public Spinner filtro;
        public ViewHolder(View v ) {
            super(v);
            layout = v;
            mRecyclerView = v.findViewById(R.id.recycler_view2);
            mLayoutManager = new LinearLayoutManager(v.getContext());
            mAdapter = new MisEnsayosTableAdapter(ensayos);
            filtro = v.findViewById(R.id.spinner2);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                    R.array.filtro, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            filtro.setAdapter(adapter);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        public View layout;
        public GraphView graph;
        public ViewHolder2(View v) {
            super(v);
            layout = v;
            graph  = v.findViewById(R.id.graph);
        }
    }

    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case 0:
                v = inflater.inflate(R.layout.lista_ensayos_layout, parent, false);
                return new MisEnsayosCarouselAdapter.ViewHolder(v);
            case 1:
                v = inflater.inflate(R.layout.graph_layout, parent, false);
                return new MisEnsayosCarouselAdapter.ViewHolder2(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                viewHolder = (MisEnsayosCarouselAdapter.ViewHolder)holder;
                viewHolder.mRecyclerView.setHasFixedSize(true);
                viewHolder.mRecyclerView.setLayoutManager(viewHolder.mLayoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(viewHolder.mRecyclerView.getContext(),DividerItemDecoration.VERTICAL);
                viewHolder.mRecyclerView.addItemDecoration(dividerItemDecoration);
                viewHolder.mRecyclerView.setAdapter(viewHolder.mAdapter);
                this.ensayo = viewHolder.filtro.getSelectedItem().toString();
                viewHolder.filtro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ensayo = (String) parent.getItemAtPosition(position);
                        ArrayList<Ensayo> newEnsayos = new ArrayList<>();
                        if(ensayos != null) {
                            for (Ensayo en : ensayos) {
                                if (en.getNombreEnsayo().compareTo(ensayo) == 0 || ensayo.compareTo("Todos") == 0) {
                                    newEnsayos.add(en);
                                }
                            }
                            viewHolder.mAdapter = new MisEnsayosTableAdapter(newEnsayos);
                            viewHolder.mAdapter.notifyDataSetChanged();
                            viewHolder.mRecyclerView.setAdapter(viewHolder.mAdapter);
                            if (viewHolder2 != null) {
                                viewHolder2.graph.setTitle("Mi progreso en " + ensayo);
                                DataPoint[] dataPoints = new DataPoint[newEnsayos.size()];
                                int i = 0;
                                for (Ensayo en : newEnsayos) {
                                    dataPoints[i] = new DataPoint(ensayos.get(i).getFecha().getTime(), ensayos.get(i).getPuntaje());
                                    i++;
                                }
                                if (newEnsayos.size() > 0) {
                                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                                    viewHolder2.graph.addSeries(series);
                                    viewHolder2.graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));
                                    viewHolder2.graph.getGridLabelRenderer().setNumHorizontalLabels(3);
                                    viewHolder2.graph.getViewport().setMinX(ensayos.get(0).getFecha().getTimeInMillis());
                                    viewHolder2.graph.getViewport().setMaxX(ensayos.get(ensayos.size()-1).getFecha().getTimeInMillis());
                                    viewHolder2.graph.getViewport().setXAxisBoundsManual(true);
                                    viewHolder2.graph.getGridLabelRenderer().setHumanRounding(false);
                                } else {
                                    viewHolder2.graph.setTitle("No hay ensayos de " + ensayo);
                                    viewHolder2.graph.removeAllSeries();
                                }
                            }
                        }
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case 1:
                viewHolder2 = (MisEnsayosCarouselAdapter.ViewHolder2)holder;
                if(ensayos != null && ensayos.size()> 0) {
                    DataPoint[] dataPoints = new DataPoint[ensayos.size()];
                    for (int i = 0; i < ensayos.size(); i++) {
                        dataPoints[i] = new DataPoint(ensayos.get(i).getFecha().getTime(), ensayos.get(i).getPuntaje());
                    }
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                    viewHolder2.graph.addSeries(series);
                    viewHolder2.graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));
                    viewHolder2.graph.getGridLabelRenderer().setNumHorizontalLabels(3);
                    viewHolder2.graph.getViewport().setMinX(ensayos.get(0).getFecha().getTimeInMillis());
                    viewHolder2.graph.getViewport().setMaxX(ensayos.get(ensayos.size()-1).getFecha().getTimeInMillis());
                    viewHolder2.graph.getViewport().setXAxisBoundsManual(true);
                    viewHolder2.graph.getGridLabelRenderer().setHumanRounding(false);
                    viewHolder2.graph.setTitle("Mi progreso en " + ensayo);

                }
                else {
                    viewHolder2.graph.setTitle("No hay ensayos");
                    viewHolder2.graph.removeAllSeries();
                }
                break;
        }
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
