package com.example.starwars.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitImageClient {
    private static Retrofit retrofitInstance;

    public static Retrofit getInstance() {
        if (retrofitInstance == null)
            retrofitInstance = new Retrofit.Builder().
                    baseUrl("https://image.tmdb.org/t/p/")
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        return retrofitInstance;
    }

    public RetrofitImageClient() {
    }
}
