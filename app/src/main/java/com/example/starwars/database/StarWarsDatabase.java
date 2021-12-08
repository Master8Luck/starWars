package com.example.starwars.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.starwars.StarWarsApp;
import com.example.starwars.model.DataConverter;
import com.example.starwars.model.Film;

@Database(entities = {Film.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class StarWarsDatabase extends RoomDatabase {
    public abstract FilmsDao mFilmsDao();
    private static StarWarsDatabase instance;

    public static synchronized StarWarsDatabase getInstance() {
        if (instance == null) {
            instance = Room.databaseBuilder(StarWarsApp.getContext(), StarWarsDatabase.class, "database")
                    .build();
        }
        return instance;
    }
}
