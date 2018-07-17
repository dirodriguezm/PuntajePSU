package com.example.diego.puntajepsu;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MisEnsayosTableAdapter extends RecyclerView.Adapter<MisEnsayosTableAdapter.ViewHolder>{
    private ArrayList<Ensayo> ensayos;
    public MisEnsayosTableAdapter(ArrayList<Ensayo> ensayos){
        this.ensayos = ensayos;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView fecha;
        public TextView puntaje;
        public TextView ensayo;
        public View layout;
        public ViewHolder(View v) {
            super(v);
            layout = v;
            fecha = v.findViewById(R.id.fecha);
            puntaje = v.findViewById(R.id.puntaje2);
            ensayo = v.findViewById(R.id.nombre_ensayo);
        }
    }

    @NonNull
    @Override
    public MisEnsayosTableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.ensayos_list_item_layout, parent, false);
        MisEnsayosTableAdapter.ViewHolder vh = new MisEnsayosTableAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fecha.setText(ensayos.get(position).fechaToString());
        holder.puntaje.setText(ensayos.get(position).getPuntaje()+"");
        holder.ensayo.setText(ensayos.get(position).getNombreEnsayo());
    }

    @Override
    public int getItemCount() {
        if(ensayos!= null){
            return ensayos.size();
        }
        return 0;
    }



}
