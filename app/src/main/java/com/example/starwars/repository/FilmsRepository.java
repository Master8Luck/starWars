package com.example.starwars.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.starwars.model.Film;
import com.example.starwars.model.Films;
import com.example.starwars.retrofit.RetrofitClient;
import com.example.starwars.retrofit.StarAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FilmsRepository {

    public static final String API_KEY = "430dc4bcb90f3bd8e2616b75a712749c";

    private static FilmsRepository instance;
    private StarAPI mStarAPI;
    private MutableLiveData<List<Film>> mLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mLoadingIndicator = new MutableLiveData<>();

    int currentPage = 0;
    int maxPage = 1;

    public FilmsRepository() {
        mLiveData.postValue(new ArrayList<>());
        Retrofit retrofit = RetrofitClient.getInstance();
        mStarAPI = retrofit.create(StarAPI.class);
    }

    public static FilmsRepository getInstance() {
        if (instance == null) {
            instance = new FilmsRepository();
        }
        return instance;
    }

    public LiveData<List<Film>> getData() {
        if (currentPage < maxPage) {
            currentPage++;
            mLoadingIndicator.postValue(true);
            mStarAPI.getBaseFilms(API_KEY, currentPage)
                    .enqueue(new Callback<Films>() {
                        @Override
                        public void onResponse(Call<Films> call, Response<Films> response) {
                            mLoadingIndicator.postValue(false);
                            mLiveData.postValue(response.body().getFilms());
                            maxPage = response.body().getPages();
                        }

                        @Override
                        public void onFailure(Call<Films> call, Throwable t) {
                            mLoadingIndicator.postValue(false);
                            mLiveData.postValue(null);
                        }
                    });
        }
        return mLiveData;
    }

    public MutableLiveData<Boolean> getLoadingIndicator() {
        return mLoadingIndicator;
    }
}
