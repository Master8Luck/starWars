package com.example.starwars.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.starwars.model.Film;
import com.example.starwars.repository.FilmsRepository;

public class FilmActivityViewModel extends ViewModel {

    private FilmsRepository mRepository;
    private LiveData<Film> mFilmLiveData;
    private LiveData<Boolean> mIndicator;

    public void init(int id) {
        mRepository = FilmsRepository.getInstance();
        mIndicator = mRepository.getLoadingIndicator();
        mFilmLiveData = mRepository.getFilmFromAPI(id);
    }

    public LiveData<Film> getFilmLiveData() {
        return mFilmLiveData;
    }

    public LiveData<Boolean> getIndicator() {
        return mIndicator;
    }
}
