package com.example.starwars.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.starwars.model.Film;
import com.example.starwars.repository.FilmsRepository;

import java.util.List;

public class FilmListActivityViewModel extends ViewModel {

    private LiveData<List<Film>> mFilms;
    private FilmsRepository mRepository;
    private LiveData<Boolean> mIndicator;

    public void init() {
        mRepository = FilmsRepository.getInstance();
        mIndicator = mRepository.getLoadingIndicator();
        loadData();
    }

    public LiveData<List<Film>> getFilms() {
        return mFilms;
    }
    public LiveData<Boolean> getLoadingIndicator() {
        return mIndicator;
    }
    public void loadData() {
        mFilms = mRepository.getDataFromAPI();
    }

}
