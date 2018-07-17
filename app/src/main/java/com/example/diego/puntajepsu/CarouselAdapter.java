package com.example.diego.puntajepsu;

import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class CarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static int [] puntajes;
    private static int numPreguntas;
    private static int notaMax;
    private static int notaMin;
    private static int notaApr;
    private static int exigencia;
    private static String ensayo;

    public CarouselAdapter(int[] puntajes, int numPreguntas, int notaMax, int notaMin, int notaApr, int exigencia, String ensayo) {
        if(numPreguntas == puntajes.length - 1) this.puntajes = puntajes;
        else this.puntajes = transformarPuntajes(puntajes, numPreguntas);
        this.numPreguntas = numPreguntas;
        this.notaMax = notaMax;
        this.notaMin = notaMin;
        this.notaApr = notaApr;
        this.exigencia = exigencia;
        this.ensayo = ensayo;
    }

    public CarouselAdapter(int[] puntajes, int numPreguntas, String ensayo) {
        if(numPreguntas == puntajes.length - 1) this.puntajes = puntajes;
        else this.puntajes = transformarPuntajes(puntajes, numPreguntas);
        this.numPreguntas = numPreguntas;
        this.ensayo = ensayo;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public RecyclerView mRecyclerView;
        public RecyclerView.LayoutManager mLayoutManager;
        public TableAdapter mAdapter;
        public ViewHolder(View v ) {
            super(v);
            layout = v;
            mRecyclerView = v.findViewById(R.id.recycler_view);
            mLayoutManager = new LinearLayoutManager(v.getContext());
            if(notaMax > 0) mAdapter = new TableAdapter(puntajes,numPreguntas, notaMax,notaMin,notaApr,exigencia);
            else {
                mAdapter = new TableAdapter(puntajes, numPreguntas);
                TextView notaTextView = v.findViewById(R.id.notaTextView);
                notaTextView.setText("");
            }
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
                v = inflater.inflate(R.layout.table_layout, parent, false);

                return new ViewHolder(v);
            case 1:
                v = inflater.inflate(R.layout.graph_layout, parent, false);
                return new ViewHolder2(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolder viewHolder = (ViewHolder)holder;
                viewHolder.mRecyclerView.setHasFixedSize(true);
                viewHolder.mRecyclerView.setLayoutManager(viewHolder.mLayoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(viewHolder.mRecyclerView.getContext(),DividerItemDecoration.VERTICAL);
                viewHolder.mRecyclerView.addItemDecoration(dividerItemDecoration);
                viewHolder.mRecyclerView.setAdapter(viewHolder.mAdapter);

                break;

            case 1:
                ViewHolder2 viewHolder2 = (ViewHolder2)holder;
                String print = "";
                for(int i = 0; i <= numPreguntas; i++){
                    print += puntajes[i] + ", ";
                }
                Log.d("PUNTAJES",""+puntajes.length + " | " + print);
                DataPoint[] dataPoints = new DataPoint[numPreguntas+1];
                for(int i = 0; i <= numPreguntas; i++){
                    dataPoints[i] = new DataPoint(i,puntajes[i]);
                }
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                viewHolder2.graph.addSeries(series);
                viewHolder2.graph.setTitle("Curva de puntajes en la PSU de " + ensayo);
                viewHolder2.graph.getViewport().setMinX(0);
                viewHolder2.graph.getViewport().setMaxX(numPreguntas+1);
                viewHolder2.graph.getViewport().setXAxisBoundsManual(true);
                //GridLabelRenderer gridLabel = viewHolder2.graph.getGridLabelRenderer();
                //gridLabel.setHorizontalAxisTitle("Preguntas correctas");
                //gridLabel.setVerticalAxisTitle("Puntaje");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    /**
     *
     * @param puntajes puntajes originales del ensayo
     * @param numPreguntas número de preguntas del ensayo a calcular
     * @return nuevo arreglo de puntajes
     */
    int[] transformarPuntajes(int [] puntajes, int numPreguntas){
        float c = (float)numPreguntas/(puntajes.length-1);
        int[] nuevo = new int[numPreguntas+1];
        for(int i = 0; i <= numPreguntas; i++){
            int x = (int)(i/c);
            nuevo[i] = puntajes[x];
        }
        return nuevo;
    }
}
