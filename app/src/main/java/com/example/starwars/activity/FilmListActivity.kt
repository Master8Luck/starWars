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

        mAdapter = FilmsAdapter(ArrayList(), this)
        // TODO it isn't redundant to use '!!' because it can't be null, and Android Studio highlight this, pay attention on it https://yadi.sk/i/X2NSQw1XLQU_Bw
        mViewModel!!.init()
        mViewModel.loadData(isInternetConnected)
        mViewModel!!.filmsLiveData!!.observe(this, { films ->
            Log.d(TAG, "onCreate: observer got the response")
            if (films == null || films.isEmpty()) {
                showErrorMessage()
                mViewModel.currentPage = 0
            } else {
                if (!isInternetConnected) {
                    showCacheMessage()
                    mViewModel.currentPage = 0
                }
                Log.d(TAG, "onCreate: set given films")
                mAdapter.setFilms(films as ArrayList<Film>)
                mViewModel.currentPage += 1
            }

        })
        mViewModel!!.indicatorLiveData!!.observe(this, {
                // TODO name variables meaningfully like 'isLoading'
                aBoolean ->
            Log.d(TAG, "onCreate: bool gotten $aBoolean")
            // TODO you can simpify it by using view extensions isVisible
            if (aBoolean) {
                binding.pbLoadingIndicator.visibility = View.VISIBLE
            } else {
                binding.pbLoadingIndicator.visibility = View.INVISIBLE
            }
        })
        // TODO in order to not call binding everytime when you need you can use run\apply\with read more about using : https://medium.com/@fatihcoskun/kotlin-scoping-functions-apply-vs-with-let-also-run-816e4efb75f5
        binding.rvFilms.adapter = mAdapter
        binding.rvFilms.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d(TAG, "onScrolled: ")
                if (!recyclerView.canScrollVertically(1)) {
                    mViewModel!!.loadData(isInternetConnected)
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