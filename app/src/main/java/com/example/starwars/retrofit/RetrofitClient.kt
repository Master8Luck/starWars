package com.example.starwars.retrofit

import retrofit2.Retrofit
import com.example.starwars.retrofit.RetrofitClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

object RetrofitClient {
    private var retrofitInstance: Retrofit? = null
    @JvmStatic
    val instance: Retrofit?
        get() {
            if (retrofitInstance == null) retrofitInstance =
                Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
            return retrofitInstance
        }
}