package com.example.starwars.database

import androidx.room.*
import com.example.starwars.model.Film
import io.reactivex.rxjava3.core.Single
import java.util.*

@Dao
interface FilmsDao {
    @get:Query("SELECT * FROM Film")
    val films: Single<List<Film>>

    @Query("SELECT * FROM Film WHERE id=:id")
    fun getFilm(id: Int): Single<Film>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(film :Film)

    @Update
    fun update(film: Film)
}