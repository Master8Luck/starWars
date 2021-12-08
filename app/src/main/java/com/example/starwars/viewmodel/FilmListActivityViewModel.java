package com.example.starwars.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.starwars.model.Film;
import com.example.starwars.repository.FilmsRepository;

import java.util.List;

public class FilmListActivityViewModel extends ViewModel {

    private FilmsRepository mRepository;
    private LiveData<List<Film>> mFilmsLiveData;
    private LiveData<Boolean> mIndicatorLiveData;

    public void init() {
        mRepository = FilmsRepository.getInstance();
        mIndicatorLiveData = mRepository.getLoadingIndicator();
        loadData();
    }

    public LiveData<List<Film>> getFilmsLiveData() {
        return mFilmsLiveData;
    }
    public LiveData<Boolean> getIndicatorLiveData() {
        return mIndicatorLiveData;
    }
    public void loadData() {
        mFilmsLiveData = mRepository.getFilmListFromAPI();
    }

}
