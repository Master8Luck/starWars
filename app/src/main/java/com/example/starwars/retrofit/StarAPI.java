package com.example.starwars.retrofit;

import com.example.starwars.model.Film;
import com.example.starwars.model.Films;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StarAPI {
    @GET("discover/movie")
    Call<Films> getBaseFilms(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{movie_id}")
    Call<Film> getFilm(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}
