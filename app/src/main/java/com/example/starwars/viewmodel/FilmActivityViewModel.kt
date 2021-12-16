package com.example.starwars.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.starwars.model.Film
import com.example.starwars.repository.FilmsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilmActivityViewModel @Inject constructor(private var mRepository: FilmsRepository) : ViewModel() {
    lateinit var filmLiveData: LiveData<Film?>
        private set
    lateinit var indicator: LiveData<Boolean>
        private set
    private var mId = 0

    fun init(id: Int) {
        mId = id
        indicator = mRepository.loadingIndicator
    }

    fun loadData(isInternetConnected: Boolean) {
        if (isInternetConnected) filmLiveData = mRepository.getFilmFromAPI(mId)
        else filmLiveData = mRepository.getFilmFromDatabase(mId)
    }
}