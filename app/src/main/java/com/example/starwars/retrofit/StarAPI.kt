package com.example.starwars.retrofit

import retrofit2.http.GET
import com.example.starwars.model.Films
import com.example.starwars.model.Film
import retrofit2.Call
import retrofit2.http.Path
import retrofit2.http.Query

interface StarAPI {
    @GET("discover/movie")
    fun getBaseFilms(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<Films>

    @GET("movie/{movie_id}")
    fun getFilm(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): Call<Film>
}