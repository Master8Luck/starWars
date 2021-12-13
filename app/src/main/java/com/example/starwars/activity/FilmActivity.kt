package com.example.starwars.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.starwars.ConnectionUtils
import com.example.starwars.ConnectionUtils.Companion.IMAGE_BASE_URL
import com.example.starwars.R
import com.example.starwars.activity.FilmListActivity.Companion.TAG
import com.example.starwars.adapter.CrewAdapter
import com.example.starwars.databinding.ActivityFilmBinding
import com.example.starwars.model.Film
import com.example.starwars.model.Genre
import com.example.starwars.viewmodel.FilmActivityViewModel
import java.util.*

class FilmActivity : AppCompatActivity() {

    private val mViewModel: FilmActivityViewModel by viewModels()
    private lateinit var mAdapter: CrewAdapter
    private val binding by lazy { ActivityFilmBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            if (!ConnectionUtils.isInternetConnected(this)) {
                Toast.makeText(this, FilmListActivity.CACHE_MESSAGE, Toast.LENGTH_SHORT).show()
            }
            Log.d(TAG, "setData: " + film.title)

            with(binding) {
                Glide.with(applicationContext)
                    .load(IMAGE_BASE_URL + film.posterPath)
                    .error(R.drawable.ic_launcher_background)
                    .into(filmIcon)
                filmTitle.text = film.title
                filmDate.text = film.releaseDate
                filmGenres.text = Genre.convertToString(film.genres)
                filmOverview.text = film.overview
                mAdapter.setData(film.crews)

            }
        } else {
            Toast.makeText(this, FilmListActivity.ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
        }
    }

}