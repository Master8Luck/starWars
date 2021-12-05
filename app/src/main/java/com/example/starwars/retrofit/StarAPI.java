package com.example.starwars.retrofit;

import com.example.starwars.model.Films;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StarAPI {
    @GET("discover/movie")
    Observable<Films> getBaseFilms(@Query("api_key") String apiKey, @Query("page") int page);
}
