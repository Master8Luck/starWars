package com.example.starwars.activity

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.starwars.R
import com.example.starwars.StarWarsApp
import com.example.starwars.adapter.CrewAdapter
import com.example.starwars.databinding.ActivityFilmBinding
import com.example.starwars.model.Film
import com.example.starwars.model.Genre
import com.example.starwars.viewmodel.FilmActivityViewModel
import java.util.*

class FilmActivity : AppCompatActivity() {
    private val mViewModel: FilmActivityViewModel by viewModels()
    private lateinit var binding: ActivityFilmBinding
    private lateinit var mAdapter: CrewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra(FilmListActivity.FILM_ID_EXTRA_KEY, 0)
        mViewModel.init(id)
        mViewModel.filmLiveData!!.observe(this,
            { t -> setData(t) })
        mAdapter = CrewAdapter(ArrayList())
        binding.filmCrewRv.adapter = mAdapter
        binding.filmToFullCrew.setOnClickListener {
            val intent = Intent(this@FilmActivity, CrewActivity::class.java)
            intent.putExtra(FilmListActivity.FILM_ID_EXTRA_KEY, id)
            startActivity(intent)
        }
    }

    private fun setData(film: Film?) {
        if (film != null) {
            if (!isInternetConnected) {
                Toast.makeText(this, FilmListActivity.CACHE_MESSAGE, Toast.LENGTH_SHORT).show()
            }
            Glide.with(applicationContext)
                .load(IMAGE_BASE_URL + film.posterPath)
                .error(R.drawable.ic_launcher_background)
                .into(binding.filmIcon)

            binding.filmTitle.text = film.title
            binding.filmDate.text = film.releaseDate
            binding.filmGenres.text = Genre.convertToString(film.genres)
            binding.filmOverview.text = film.overview
            mAdapter.setData(film.crews)
            mAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this, FilmListActivity.ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
        }
    }

    private val isInternetConnected: Boolean
        private get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }

    companion object {
        private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342/"
    }
}