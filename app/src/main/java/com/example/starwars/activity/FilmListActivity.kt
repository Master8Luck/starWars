package com.example.starwars.activity

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starwars.R
import com.example.starwars.activity.FilmActivity
import com.example.starwars.adapter.FilmsAdapter
import com.example.starwars.adapter.FilmsAdapter.FilmClickListener
import com.example.starwars.model.Film
import com.example.starwars.viewmodel.FilmListActivityViewModel
import java.util.*

class FilmListActivity : AppCompatActivity(), FilmClickListener {
    private var mViewModel: FilmListActivityViewModel? = null
    private var mAdapter: FilmsAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private var loadingProgressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_list)
        mViewModel = ViewModelProviders.of(this).get(
            FilmListActivityViewModel::class.java
        )
        mViewModel!!.init()
        mViewModel!!.filmsLiveData!!.observe(this, { films ->
            if (films == null || films.isEmpty()) {
                showErrorMessage()
            } else if (!isInternetConnected) {
                showCacheMessage()
            }
            mAdapter!!.setFilms(films as ArrayList<Film>)
        })
        mViewModel!!.indicatorLiveData!!.observe(this, { aBoolean ->
            if (aBoolean) {
                loadingProgressBar!!.visibility = View.VISIBLE
            } else {
                loadingProgressBar!!.visibility = View.INVISIBLE
            }
        })
        recyclerView = findViewById(R.id.rv_films)
        loadingProgressBar = findViewById(R.id.pb_loading_indicator)
        mAdapter = FilmsAdapter(ArrayList(), this, this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = mAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    mViewModel!!.loadData()
                }
            }
        })
    }

    private fun showErrorMessage() {
        Toast.makeText(this, ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
    }

    private fun showCacheMessage() {
        Toast.makeText(this, CACHE_MESSAGE, Toast.LENGTH_SHORT).show()
    }

    private val isInternetConnected: Boolean
        private get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }

    override fun onItemClick(position: Int) {
        val filmActivityIntent = Intent(this, FilmActivity::class.java)
        filmActivityIntent.putExtra(
            FILM_ID_EXTRA_KEY,
            mViewModel!!.filmsLiveData!!.value!![position].id
        )
        startActivity(filmActivityIntent)
    }

    companion object {
        const val FILM_ID_EXTRA_KEY = "films"
        const val TAG = "asd"
        const val ERROR_MESSAGE = "No result in cache. Please connect to the internet"
        const val CACHE_MESSAGE = "No internet connection. Showing cache"
    }
}