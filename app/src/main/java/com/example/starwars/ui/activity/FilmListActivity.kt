package com.example.starwars.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.starwars.ConnectionUtils
import com.example.starwars.databinding.ActivityFilmListBinding
import com.example.starwars.domain.model.Film
import com.example.starwars.ui.adapter.FilmsAdapter
import com.example.starwars.ui.adapter.FilmsAdapter.FilmClickListener
import com.example.starwars.ui.viewmodel.FilmListActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class FilmListActivity : AppCompatActivity(), FilmClickListener {

    private lateinit var binding: ActivityFilmListBinding
    lateinit var mAdapter: FilmsAdapter
    val mViewModel: FilmListActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAdapter = FilmsAdapter(this)

        with(mViewModel) {
            init()
            loadData(ConnectionUtils.isInternetConnected(this@FilmListActivity))
            filmsLiveData.observe(this@FilmListActivity, { films ->
                Log.d(TAG, "onCreate: observer got the response")
                if (films == null || films.isEmpty()) {
                    showErrorMessage()
                    currentPage = 0
                } else {
                    if (!ConnectionUtils.isInternetConnected(this@FilmListActivity)) {
                        showCacheMessage()
                        currentPage = 0
                    }
                    Log.d(TAG, "onCreate: set given films")
                    mAdapter.setFilms(films as ArrayList<Film>)
                    currentPage += 1
                }
            })

            indicatorLiveData.observe(this@FilmListActivity, {
                    isLoading -> binding.pbLoadingIndicator.isVisible = isLoading
            })
        }

        with(binding) {
            rvFilms.adapter = mAdapter
            rvFilms.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    Log.d(TAG, "onScrolled: ")
                    if (!recyclerView.canScrollVertically(1)) {
                        mViewModel.loadData(ConnectionUtils.isInternetConnected(this@FilmListActivity))
                    }
                }
            })
        }

    }

    private fun showErrorMessage() {
        Toast.makeText(this, ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
    }

    private fun showCacheMessage() {
        Toast.makeText(this, CACHE_MESSAGE, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(position: Int) {
        val filmActivityIntent = Intent(this, FilmActivity::class.java)
        filmActivityIntent.putExtra(
            FILM_ID_EXTRA_KEY,
            mViewModel.filmsLiveData.value!![position].id
        )
        Log.d(TAG, "onItemClick: $position ")
        startActivity(filmActivityIntent)
    }

    companion object {
        const val FILM_ID_EXTRA_KEY = "films"
        const val TAG = "asd"
        const val ERROR_MESSAGE = "No result in cache. Please connect to the internet"
        const val CACHE_MESSAGE = "No internet connection. Showing cache"
    }
}