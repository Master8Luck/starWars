package com.example.starwars.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.starwars.domain.model.DataConverter
import com.example.starwars.domain.model.Film

@Database(entities = [Film::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class StarWarsDatabase : RoomDatabase() {
    abstract fun mFilmsDao(): FilmsDao

//    companion object {
//        @JvmStatic
//        @get:Synchronized
//        var instance: StarWarsDatabase? = null
//            get() {
//                if (field == null) {
//                    field = Room.databaseBuilder(
//                        StarWarsApp.context!!,
//                        StarWarsDatabase::class.java,
//                        "database"
//                    )
//                        .build()
//                }
//                return field
//            }
//            private set
//    }
}