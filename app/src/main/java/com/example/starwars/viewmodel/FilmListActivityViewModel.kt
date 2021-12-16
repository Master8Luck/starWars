package com.example.starwars.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.starwars.activity.FilmListActivity.Companion.TAG
import com.example.starwars.model.Film
import com.example.starwars.repository.FilmsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilmListActivityViewModel @Inject constructor(private var mRepository: FilmsRepository) : ViewModel() {
    var currentPage = 1
    lateinit var filmsLiveData: LiveData<List<Film>?>
        private set
    lateinit var indicatorLiveData: LiveData<Boolean>
        private set

    fun init() {
        indicatorLiveData = mRepository.loadingIndicator
    }

    fun loadData(isInternetConnected: Boolean) {
        Log.d(TAG, "init: after loadDataCalled")
        if (isInternetConnected) filmsLiveData = mRepository.filmListFromAPI(currentPage)
        else filmsLiveData = mRepository.getFilmListFromDatabase()
    }
}