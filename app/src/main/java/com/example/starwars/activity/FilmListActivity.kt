package com.example.starwars.activity

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starwars.R
import com.example.starwars.activity.FilmActivity
import com.example.starwars.adapter.FilmsAdapter
import com.example.starwars.adapter.FilmsAdapter.FilmClickListener
import com.example.starwars.databinding.ActivityFilmListBinding
import com.example.starwars.databinding.FilmListItemBinding
import com.example.starwars.model.Film
import com.example.starwars.viewmodel.FilmListActivityViewModel
import java.util.*

class FilmListActivity : AppCompatActivity(), FilmClickListener {

    private lateinit var binding: ActivityFilmListBinding
    private lateinit var mAdapter: FilmsAdapter
    val mViewModel: FilmListActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAdapter = FilmsAdapter(ArrayList(), this, this)

        mViewModel!!.init()
        mViewModel!!.filmsLiveData!!.observe(this, { films ->
            Log.d(TAG, "onCreate: observer got the response")
            if (films == null || films.isEmpty()) {
                showErrorMessage()
            } else {
                if (!isInternetConnected) {
                    showCacheMessage()
                }
                Log.d(TAG, "onCreate: set givven films")
                mAdapter.setFilms(films as ArrayList<Film>)
            }

        })
        mViewModel!!.indicatorLiveData!!.observe(this, { aBoolean ->
            Log.d(TAG, "onCreate: bool gotten $aBoolean")
            if (aBoolean) {
                binding.pbLoadingIndicator.visibility = View.VISIBLE
            } else {
                binding.pbLoadingIndicator.visibility = View.INVISIBLE
            }
        })

        binding.rvFilms.adapter = mAdapter
        binding.rvFilms.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d(TAG, "onScrolled: ")
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