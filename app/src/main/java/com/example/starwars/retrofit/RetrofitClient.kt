package com.example.starwars.retrofit

import retrofit2.Retrofit
import com.example.starwars.retrofit.RetrofitClient
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

object RetrofitClient {
    private var retrofitInstance: Retrofit? = null
    private const val API_KEY = "430dc4bcb90f3bd8e2616b75a712749c"
    @JvmStatic
    val instance: Retrofit?
        get() {
            if (retrofitInstance == null) retrofitInstance =
                Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(
                        OkHttpClient.Builder()
                            .addInterceptor { chain ->
                        val url = chain
                            .request()
                            .url()
                            .newBuilder()
                            .addQueryParameter("api_key", API_KEY)
                            .build()
                                chain.proceed(chain.request().newBuilder().url(url).build())
                            }
                            .build()
                    )
                    .build()
            return retrofitInstance
        }
}