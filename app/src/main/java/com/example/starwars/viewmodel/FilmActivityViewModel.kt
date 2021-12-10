package com.example.starwars.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.starwars.model.Film
import com.example.starwars.repository.FilmsRepository
import com.example.starwars.repository.FilmsRepository.Companion.instance

class FilmActivityViewModel : ViewModel() {
    private var mRepository: FilmsRepository? = null
    var filmLiveData: LiveData<Film?>? = null
        private set
    var indicator: LiveData<Boolean>? = null
        private set

    fun init(id: Int) {
        mRepository = instance
        indicator = mRepository!!.loadingIndicator
        filmLiveData = mRepository!!.getFilmFromAPI(id)
    }
}