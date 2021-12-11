package com.example.starwars.repository

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.starwars.activity.FilmListActivity
import com.example.starwars.database.StarWarsDatabase
import com.example.starwars.model.Film
import com.example.starwars.model.Films
import com.example.starwars.retrofit.RetrofitClient
import com.example.starwars.retrofit.StarAPI
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FilmsRepository private constructor() {
    private val mStarAPI: StarAPI
    private val mDatabase: StarWarsDatabase?
    private val mFilmList = MutableLiveData<MutableList<Film>?>()
    val loadingIndicator = MutableLiveData<Boolean>()
    private val mFilm = MutableLiveData<Film?>()

    /** TODO
     * it's better to move info about pagination into viewModel (currentPage, maxPage),
     * and make filmListFromAPI as function and pass into function above params
     */

    var currentPage = 0
    var maxPage = 1
    val filmListFromAPI: MutableLiveData<MutableList<Film>?>
        get() {
            if (currentPage < maxPage) {
                currentPage++
                loadingIndicator.postValue(true)
                // TODO use rxjava + retrofit for call api
                mStarAPI.getBaseFilms(API_KEY, currentPage)
                    .enqueue(object : Callback<Films?> {
                        override fun onResponse(call: Call<Films?>, response: Response<Films?>) {
                            response.body()!!.films?.let { insertFilms(it) }
                            var currentFilms = mFilmList.value
                            if (currentFilms == null) currentFilms = ArrayList()
                            response.body()!!.films?.let { currentFilms.addAll(it) }
                            loadingIndicator.postValue(false)
                            mFilmList.postValue(currentFilms)
                            maxPage = response.body()!!.pages
                        }

                        override fun onFailure(call: Call<Films?>, t: Throwable) {
                            currentPage = 0
                            /** TODO
                             * not good way in any failure call database it's better to find
                             * concrete condition for obtain cache items
                             */
                            filmListFromDatabase
                        }
                    })
            }
            return mFilmList
        }

    // TODO forgot about async tasks in android, it's deprecated, try to use rxjava
    private fun insertFilms(films: List<Film>) {
        object : AsyncTask<List<Film?>?, Void?, Void?>() {

            override fun doInBackground(vararg params: List<Film?>?): Void? {
                for (i in params[0]?.indices!!) {
                    params[0]?.get(i)?.let { mDatabase!!.mFilmsDao()!!.insert(it) }
                }
                return null
            }
        }.execute(films)
    }

    // TODO forgot about async tasks in android, it's deprecated, try to use rxjava
    private fun updateFilm(film: Film) {
        object : AsyncTask<Film, Void?, Void?>() {
            override fun doInBackground(vararg params: Film): Void? {
                mDatabase!!.mFilmsDao()!!.update(params[0])
                return null
            }
        }.execute(film)
    }

    // TODO also better replace with function
    val filmListFromDatabase: Unit
        get() {
            mDatabase!!.mFilmsDao()!!.films
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableSingleObserver<List<Film?>?>() {
                    override fun onError(e: @NonNull Throwable?) {
                        mFilmList.postValue(null)
                        loadingIndicator.postValue(false)
                    }

                    override fun onSuccess(t: List<Film?>?) {
                        // TODO do we need MutableList or we can use List ?
                        mFilmList.postValue(t as MutableList<Film>?)
                        loadingIndicator.postValue(false)
                    }
                })
        }

    fun getFilmFromDatabase(id: Int) {
        mDatabase!!.mFilmsDao()!!.getFilm(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<Film?>() {
                override fun onSuccess(film: @NonNull Film?) {
                    mFilm.postValue(film)
                    loadingIndicator.postValue(false)
                }

                override fun onError(e: @NonNull Throwable?) {
                    mFilm.postValue(null)
                    loadingIndicator.postValue(false)
                }
            })
    }

    fun getFilmFromAPI(id: Int): LiveData<Film?> {
        loadingIndicator.postValue(true)
        mStarAPI.getFilm(id, API_KEY)
            .enqueue(object : Callback<Film?> {
                override fun onResponse(call: Call<Film?>, response: Response<Film?>) {
                    response.body()?.let { updateFilm(it) }
                    mFilm.postValue(response.body())
                    Log.d(FilmListActivity.TAG, "onResponse: ")
                    loadingIndicator.postValue(false)
                }

                override fun onFailure(call: Call<Film?>, t: Throwable) {
                    getFilmFromDatabase(id)
                    Log.d(FilmListActivity.TAG, "onFailure: ")
                }
            })
        return mFilm
    }

    companion object {
        const val API_KEY = "430dc4bcb90f3bd8e2616b75a712749c"
        @JvmStatic
        var instance: FilmsRepository? = null
            get() {
                if (field == null) {
                    field = FilmsRepository()
                }
                return field
            }
            private set
    }

    init {
        val retrofit = RetrofitClient.instance
        mStarAPI = retrofit!!.create(StarAPI::class.java)
        mDatabase = StarWarsDatabase.instance
    }
}