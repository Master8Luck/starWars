package com.example.starwars.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.starwars.activity.FilmListActivity.Companion.TAG
import com.example.starwars.model.Film
import com.example.starwars.repository.FilmsRepository
import com.example.starwars.repository.FilmsRepository.Companion.instance

class FilmListActivityViewModel : ViewModel() {
    private var mRepository: FilmsRepository? = null
    var filmsLiveData: LiveData<MutableList<Film>?>? = null
        private set
    var indicatorLiveData: LiveData<Boolean>? = null
        private set

    fun init() {
        mRepository = instance
        indicatorLiveData = mRepository!!.loadingIndicator
        loadData()
        Log.d(TAG, "init: after loadDataCalled")
    }

    fun loadData() {
        filmsLiveData = mRepository!!.filmListFromAPI()
    }
}