package com.example.starwars.database

import androidx.room.Database
import com.example.starwars.model.Film
import androidx.room.TypeConverters
import com.example.starwars.model.DataConverter
import androidx.room.RoomDatabase
import com.example.starwars.database.FilmsDao
import com.example.starwars.database.StarWarsDatabase
import kotlin.jvm.Synchronized
import androidx.room.Room
import com.example.starwars.StarWarsApp

@Database(entities = [Film::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class StarWarsDatabase : RoomDatabase() {
    abstract fun mFilmsDao(): FilmsDao?

    companion object {
        @JvmStatic
        @get:Synchronized
        var instance: StarWarsDatabase? = null
            get() {
                if (field == null) {
                    field = Room.databaseBuilder(
                        StarWarsApp.context!!,
                        StarWarsDatabase::class.java,
                        "database"
                    )
                        .build()
                }
                return field
            }
            private set
    }
}