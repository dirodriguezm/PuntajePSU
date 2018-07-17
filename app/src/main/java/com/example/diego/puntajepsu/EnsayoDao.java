package com.example.diego.puntajepsu;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface EnsayoDao {
    @Query("SELECT * FROM Ensayo")
    List<Ensayo> getAll();

    @Insert
    void insertEnsayo(Ensayo ensayo);

}
