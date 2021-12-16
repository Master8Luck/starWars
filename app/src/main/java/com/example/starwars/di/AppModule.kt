package com.example.starwars.di

import androidx.room.Room
import com.example.starwars.API_ENDPOINTS
import com.example.starwars.StarWarsApp
import com.example.starwars.database.StarWarsDatabase
import com.example.starwars.retrofit.StarAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit = Retrofit.Builder()
        .baseUrl(API_ENDPOINTS.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val url = chain
                        .request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("api_key", API_ENDPOINTS.API_KEY)
                        .build()
                    chain.proceed(chain.request().newBuilder().url(url).build())
                }
                .build()
        )
        .build()

    @Provides
    @Singleton
    fun provideDatabase() : StarWarsDatabase = Room
        .databaseBuilder(StarWarsApp.context!!, StarWarsDatabase::class.java, "database")
        .build()

    @Provides
    @Singleton
    fun provideStarAPI(retrofit: Retrofit) : StarAPI = retrofit.create(StarAPI::class.java)

}