package com.example.starwars.retrofit;

import com.example.starwars.model.Films;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface StarAPI {
    @GET("films")
    Observable<Films> getFilms();
}
