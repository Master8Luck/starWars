package com.example.starwars.database;

import android.app.Application;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.starwars.StarWarsApp;
import com.example.starwars.model.Film;

@Database(entities = {Film.class}, version = 1)
public abstract class StarWarsDatabase extends RoomDatabase {
    public abstract FilmListDao mFilmListDao();
    private static StarWarsDatabase instance;

    public static synchronized StarWarsDatabase getInstance() {
        if (instance == null) {
            instance = Room.databaseBuilder(StarWarsApp.getContext(), StarWarsDatabase.class, "database")
                    .build();
        }
        return instance;
    }
}
