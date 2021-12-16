package com.example.starwars.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.starwars.activity.FilmListActivity.Companion.TAG
import com.example.starwars.database.StarWarsDatabase
import com.example.starwars.model.Film
import com.example.starwars.retrofit.StarAPI
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilmsRepository @Inject constructor(private val mStarAPI: StarAPI, private val database: StarWarsDatabase) {

    private val mFilmList = MutableLiveData<List<Film>?>()
    val loadingIndicator = MutableLiveData<Boolean>()
    private val mFilm = MutableLiveData<Film?>()

    private var maxPage = 1


    private fun insertFilms(films: List<Film>) {
        Observable.fromCallable { this.database.mFilmsDao().insertAll(films) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    //  Reactive variant:
    //
//        Observable.fromIterable(films)
//            .subscribeOn(Schedulers.io())
//            .subscribe{ film -> mDatabase.mFilmsDao().insert(film) }
    }

    private fun updateFilm(film: Film) {
        Observable.fromCallable {
            -> this.database.mFilmsDao().update(film)
            Log.d(TAG, "updateFilm: " + film.title + " " + film.crews)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun filmListFromAPI(currentPage: Int): MutableLiveData<List<Film>?> {
            if (currentPage <= maxPage) {
                loadingIndicator.postValue(true)
                mStarAPI.getBaseFilms(currentPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { data -> data.films?.let { insertFilms(it) } }
                    .subscribe({ data ->
                        val currentFilms: MutableList<Film>
                        if (mFilmList.value != null) currentFilms = mFilmList.value as MutableList
                        else currentFilms = ArrayList()
                        data.films?.let { currentFilms.addAll(it) }
                        loadingIndicator.postValue(false)
                        mFilmList.postValue(currentFilms)
                        maxPage = data.pages
                    }, {
                        Log.d(TAG, "filmListFromAPI got error: " + it.message)
                    })
            }
        return mFilmList
    }

    fun getFilmListFromDatabase(): MutableLiveData<List<Film>?> {
            this.database.mFilmsDao().films
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                        mFilmList.postValue(it)
                        loadingIndicator.postValue(false)
                }, {
                    mFilmList.postValue(null)
                    loadingIndicator.postValue(false)
                })
        return mFilmList
    }

    fun getFilmFromDatabase(id: Int): LiveData<Film?>  {
        this.database.mFilmsDao().getFilm(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mFilm.postValue(it)
                Log.d(TAG, "getFilmFromDatabase: " + it.title)
                Log.d(TAG, "and crew: " + it.crews)
                loadingIndicator.postValue(false)
            }, {
                mFilm.postValue(null)
                Log.d(TAG, "getFilmFromDatabase: Error!!")
                loadingIndicator.postValue(false)
            })
        return mFilm
    }

    fun getFilmFromAPI(id: Int): LiveData<Film?> {
        loadingIndicator.postValue(true)
        mStarAPI.getFilm(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                updateFilm(it)
                Log.d(TAG, "getFilmFromAPI: " + it.title + " " + it.crews + " and updated")
                mFilm.postValue(it)
                loadingIndicator.postValue(false)
            }, {
                mFilm.postValue(null)
                loadingIndicator.postValue(false)
            })
        return mFilm
    }

}