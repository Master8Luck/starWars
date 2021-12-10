package com.example.starwars.model

import androidx.room.TypeConverter
import com.example.starwars.model.Genre
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.example.starwars.model.Crew

class DataConverter {
    @TypeConverter
    fun fromGenresArray(genres: Array<Genre?>?): String? {
        if (genres == null) return null
        val type = object : TypeToken<Array<Genre?>?>() {}.type
        return Gson().toJson(genres, type)
    }

    @TypeConverter
    fun toGenresArray(genresString: String?): Array<Genre>? {
        if (genresString == null) return null
        val type = object : TypeToken<Array<Genre?>?>() {}.type
        return Gson().fromJson(genresString, type)
    }

    @TypeConverter
    fun fromCrewList(crews: List<Crew?>?): String? {
        if (crews == null || crews.isEmpty()) return null
        val type = object : TypeToken<List<Crew?>?>() {}.type
        return Gson().toJson(crews, type)
    }

    @TypeConverter
    fun toCrewList(crewsString: String?): List<Crew>? {
        if (crewsString == null) return null
        val type = object : TypeToken<List<Crew?>?>() {}.type
        return Gson().fromJson(crewsString, type)
    }
}