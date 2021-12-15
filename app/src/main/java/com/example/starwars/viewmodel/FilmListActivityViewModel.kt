package com.example.starwars.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.starwars.activity.FilmListActivity.Companion.TAG
import com.example.starwars.model.Film
import com.example.starwars.repository.FilmsRepository
import com.example.starwars.repository.FilmsRepository.Companion.instance

class FilmListActivityViewModel : ViewModel() {
    private lateinit var mRepository: FilmsRepository
    var currentPage = 1
    // TODO why liveData is nullable, maybe make it lateinit
    var filmsLiveData: LiveData<List<Film>?>? = null
        private set
    var indicatorLiveData: LiveData<Boolean>? = null
        private set

    fun init() {
        mRepository = instance!!
        indicatorLiveData = mRepository.loadingIndicator
    }

    fun loadData(isInternetConnected: Boolean) {
        Log.d(TAG, "init: after loadDataCalled")
        if (isInternetConnected) filmsLiveData = mRepository.filmListFromAPI(currentPage)
        else filmsLiveData = mRepository.getFilmListFromDatabase()
    }
}