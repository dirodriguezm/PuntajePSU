package com.example.diego.puntajepsu;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Calendar;

@Entity
public class Ensayo implements Parcelable, Comparable<Ensayo>{
    @PrimaryKey(autoGenerate = true)
    private int ensayoId;
    @ColumnInfo(name="nombre_ensayo")
    private String nombreEnsayo;
    @ColumnInfo(name="fecha")
    private Calendar fecha;
    @ColumnInfo(name="puntaje")
    private int puntaje;
    @Ignore
    private String[] monthName = {"Enero", "Febrero",
            "Marzo", "Abril", "Mayo", "Junio", "Julio",
            "Agosto", "Septiembre", "Octubre", "Noviembre",
            "Diciembre"};

    public Ensayo(String nombreEnsayo, Calendar fecha, int puntaje){
        this.nombreEnsayo = nombreEnsayo;
        this.fecha = fecha;
        this.puntaje = puntaje;
    }

    protected Ensayo(Parcel in) {
        nombreEnsayo = in.readString();
        puntaje = in.readInt();
    }

    public static final Creator<Ensayo> CREATOR = new Creator<Ensayo>() {
        @Override
        public Ensayo createFromParcel(Parcel in) {
            return new Ensayo(in);
        }

        @Override
        public Ensayo[] newArray(int size) {
            return new Ensayo[size];
        }
    };

    public String getNombreEnsayo() {
        return nombreEnsayo;
    }

    public void setNombreEnsayo(String nombreEnsayo) {
        this.nombreEnsayo = nombreEnsayo;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreEnsayo);
        dest.writeInt(puntaje);
    }

    public int getEnsayoId() {
        return ensayoId;
    }

    public void setEnsayoId(int ensayoId) {
        this.ensayoId = ensayoId;
    }

    public String fechaToString(){
        return fecha.get(Calendar.DAY_OF_MONTH) + " de " + monthName[fecha.get(Calendar.MONTH)] + " de " + fecha.get(Calendar.YEAR);
    }


    @Override
    public int compareTo(@NonNull Ensayo o) {
        return this.getFecha().compareTo(o.getFecha());
    }
}


