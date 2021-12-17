package com.example.starwars.data.retrofit

import com.example.starwars.domain.model.Film
import com.example.starwars.domain.model.Films
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StarAPI {

    @GET("discover/movie")
    fun getBaseFilms( @Query("page") page: Int): Single<Films>

    @GET("movie/{movie_id}")
    fun getFilm(@Path("movie_id") movieId: Int): Observable<Film>
}