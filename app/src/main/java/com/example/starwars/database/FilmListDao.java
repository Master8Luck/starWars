package com.example.starwars.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.starwars.model.Film;

import java.util.List;
import java.util.Observable;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface FilmListDao {

    @Query("SELECT * FROM Film")
    Single<List<Film>> getFilms();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Film film);
}
