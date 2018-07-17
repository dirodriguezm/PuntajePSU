package com.example.diego.puntajepsu;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    private int[] puntajes;
    private float[] notas;
    private int numPreguntas, notaMax, notaMin, notaApr, exigencia;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView corregido;
        public TextView puntaje;
        public TextView nota;
        public View layout;
        public ViewHolder(View v) {
            super(v);
            layout = v;
            corregido = v.findViewById(R.id.corregido);
            puntaje = v.findViewById(R.id.puntaje);
            nota = v.findViewById(R.id.nota);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TableAdapter(int[] puntajes, int numPreguntas) {
        this.puntajes = puntajes;
        this.numPreguntas = numPreguntas;
    }
    public TableAdapter(int[] puntajes, int numPreguntas, int notaMax, int notaMin, int notaApr, int exigencia) {
        this.puntajes = puntajes;
        this.numPreguntas = numPreguntas;
        this.notaMax = notaMax;
        this.notaMin = notaMin;
        this.notaApr = notaApr;
        this.exigencia = exigencia;
        this.notas = calcularNotas(notaMax,notaMin,notaApr,exigencia,numPreguntas);
    }



    // Create new views (invoked by the layout manager)
    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.table_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.corregido.setText(""+position);
        holder.puntaje.setText(""+puntajes[position]);
        if(notaMax>0){
            holder.nota.setText(""+notas[position]);
            if(notas[position] >= notaApr){
                holder.nota.setTextColor(Color.BLUE);
            }else{
                holder.nota.setTextColor(Color.RED);
            }
        }
        else{
            holder.nota.setVisibility(View.INVISIBLE);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return numPreguntas+1;
    }

    /**
     * Calcula la nota segun puntaje
     * @param notaMax Nota máxima posible
     * @param notaMin Nota mínima posible
     * @param notaApr Nota de aprobación
     * @param exigencia Porcentaje de exigencia. Ej: 60
     * @param puntajeMax El puntaje máximo posible
    */
    float[] calcularNotas(int notaMax, int notaMin, int notaApr, int exigencia, int puntajeMax){
        float e = (float)exigencia/100; // porcentaje a numero decimal
        float[] notas = new float[puntajeMax+1]; //arreglo que almacena las notas, cada índice del arreglo es un puntaje
        //Cálculo de notas
        for(int i = 0; i <= puntajeMax; i++){
            if(i < (e * puntajeMax)){
                notas[i] = (notaApr - notaMin) * (i /(e * puntajeMax)) + notaMin;
            }
            else{
                notas[i] = (notaMax - notaApr) * (i - e * puntajeMax) / (puntajeMax * (1 - e)) + notaApr;
            }
            //Primero se trunca la nota a dos decimales
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.FLOOR);
            notas[i] = Float.parseFloat(df.format(notas[i]));
            //Luego se aproxima hacia arriba si el segundo decimal es mayor a 5
            df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.HALF_UP);
            notas[i] = Float.parseFloat(df.format(notas[i]));
        }
        return notas;
    }


}
