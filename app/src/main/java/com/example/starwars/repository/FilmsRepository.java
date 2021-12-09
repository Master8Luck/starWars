package com.example.starwars.repository;

import static com.example.starwars.activity.FilmListActivity.TAG;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.starwars.database.StarWarsDatabase;
import com.example.starwars.model.Film;
import com.example.starwars.model.Films;
import com.example.starwars.retrofit.RetrofitClient;
import com.example.starwars.retrofit.StarAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
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
    private MutableLiveData<List<Film>> mFilmList = new MutableLiveData<>();
    private MutableLiveData<Boolean> mLoadingIndicator = new MutableLiveData<>();
    private MutableLiveData<Film> mFilm = new MutableLiveData<>();

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

    public MutableLiveData<List<Film>> getFilmListFromAPI() {
        if (currentPage < maxPage) {
            currentPage++;
            mLoadingIndicator.postValue(true);
            mStarAPI.getBaseFilms(API_KEY, currentPage)
                    .enqueue(new Callback<Films>() {
                        @Override
                        public void onResponse(Call<Films> call, Response<Films> response) {
                            insertFilms(response.body().getFilms());
                            List<Film> currentFilms = mFilmList.getValue();
                            if (currentFilms == null)
                                currentFilms = new ArrayList<>();
                            currentFilms.addAll(response.body().getFilms());
                            mLoadingIndicator.postValue(false);
                            mFilmList.postValue(currentFilms);
                            maxPage = response.body().getPages();
                        }

                        @Override
                        public void onFailure(Call<Films> call, Throwable t) {
                            currentPage = 0;
                            getFilmListFromDatabase();
                        }
                    });
        }
        return mFilmList;
    }

    private void insertFilms(List<Film> films) {
        new AsyncTask<List<Film>, Void, Void>() {

            @Override
            protected Void doInBackground(List<Film>... film) {
                for (int i = 0; i < film[0].size(); i++) {
                    mDatabase.mFilmsDao().insert(film[0].get(i));
                }
                return null;
            }
        }.execute(films);
    }

    private void updateFilm(Film film) {
        new AsyncTask<Film, Void, Void>() {

            @Override
            protected Void doInBackground(Film... films) {
                mDatabase.mFilmsDao().update(film);
                return null;
            }
        }.execute(film);
    }

    public void getFilmListFromDatabase() {
        mDatabase.mFilmsDao().getFilms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Film>>() {
                    @Override
                    public void onSuccess(@NonNull List<Film> films) {
                        mFilmList.postValue(films);
                        mLoadingIndicator.postValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mFilmList.postValue(null);
                        mLoadingIndicator.postValue(false);
                    }
                });
    }

    public void getFilmFromDatabase(int id) {
        mDatabase.mFilmsDao().getFilm(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Film>() {
                    @Override
                    public void onSuccess(@NonNull Film film) {
                        mFilm.postValue(film);
                        mLoadingIndicator.postValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mFilm.postValue(null);
                        mLoadingIndicator.postValue(false);
                    }
                });
    }

    public MutableLiveData<Boolean> getLoadingIndicator() {
        return mLoadingIndicator;
    }

    public LiveData<Film> getFilmFromAPI(int id) {
        mLoadingIndicator.postValue(true);
        mStarAPI.getFilm(id, API_KEY)
                .enqueue(new Callback<Film>() {
                    @Override
                    public void onResponse(Call<Film> call, Response<Film> response) {
                        updateFilm(response.body());
                        mFilm.postValue(response.body());
                        Log.d(TAG, "onResponse: ");
                        mLoadingIndicator.postValue(false);
                    }

                    @Override
                    public void onFailure(Call<Film> call, Throwable t) {
                        getFilmFromDatabase(id);
                        Log.d(TAG, "onFailure: ");
                    }
                });
        return mFilm;
    }

}
