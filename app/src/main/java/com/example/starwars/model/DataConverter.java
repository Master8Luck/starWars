package com.example.starwars.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class DataConverter {
    @TypeConverter
    public String fromGenresArray(Genre[] genres) {
        if (genres == null)
            return null;
        Type type = new TypeToken<Genre[]>() {}.getType();
        return new Gson().toJson(genres, type);
    }

    @TypeConverter
    public Genre[] toGenresArray(String genresString) {
        if (genresString == null)
            return null;
        Type type = new TypeToken<Genre[]>() {}.getType();
        return new Gson().fromJson(genresString, type);
    }
}
