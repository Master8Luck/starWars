package com.example.starwars.repository;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.starwars.database.FilmListDao;
import com.example.starwars.database.StarWarsDatabase;
import com.example.starwars.model.Film;
import com.example.starwars.model.Films;
import com.example.starwars.retrofit.RetrofitClient;
import com.example.starwars.retrofit.StarAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FilmsRepository {

    public static final String API_KEY = "430dc4bcb90f3bd8e2616b75a712749c";

    private static FilmsRepository instance;
    private StarAPI mStarAPI;
    private StarWarsDatabase mDatabase;
    private MutableLiveData<List<Film>> mLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mLoadingIndicator = new MutableLiveData<>();

    int currentPage = 0;
    int maxPage = 1;

    private FilmsRepository() {
        Retrofit retrofit = RetrofitClient.getInstance();
        mStarAPI = retrofit.create(StarAPI.class);
        mDatabase = StarWarsDatabase.getInstance();
    }

    public static FilmsRepository getInstance() {
        if (instance == null) {
            instance = new FilmsRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Film>> getDataFromAPI() {
        if (currentPage < maxPage) {
            currentPage++;
            mLoadingIndicator.postValue(true);
            mStarAPI.getBaseFilms(API_KEY, currentPage)
                    .enqueue(new Callback<Films>() {
                        @Override
                        public void onResponse(Call<Films> call, Response<Films> response) {
                            insertFilms(response.body().getFilms());
                            List<Film> currentFilms = mLiveData.getValue();
                            if (currentFilms == null)
                                currentFilms = new ArrayList<>();
                            currentFilms.addAll(response.body().getFilms());
                            mLoadingIndicator.postValue(false);
                            mLiveData.postValue(currentFilms);
                            maxPage = response.body().getPages();
                        }

                        @Override
                        public void onFailure(Call<Films> call, Throwable t) {
                            currentPage = 0;
                            getDataFromDatabase();
                        }
                    });
        }
        return mLiveData;
    }

    private void insertFilms(List<Film> films) {
        new AsyncTask<List<Film>, Void, Void>() {

            @Override
            protected Void doInBackground(List<Film>... film) {
                for (int i = 0; i < film[0].size(); i++) {
                    mDatabase.mFilmListDao().insert(film[0].get(i));
                }
                return null;
            }
        }.execute(films);
    }

    public void getDataFromDatabase() {
        mDatabase.mFilmListDao().getFilms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Film>>() {
                    @Override
                    public void onSuccess(@NonNull List<Film> films) {
                        mLiveData.postValue(films);
                        mLoadingIndicator.postValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mLiveData.postValue(null);
                        mLoadingIndicator.postValue(false);
                    }
                });
    }

    public MutableLiveData<Boolean> getLoadingIndicator() {
        return mLoadingIndicator;
    }
}
