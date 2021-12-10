package com.example.starwars.activity

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.starwars.R
import com.example.starwars.StarWarsApp
import com.example.starwars.adapter.CrewAdapter
import com.example.starwars.model.Film
import com.example.starwars.model.Genre
import com.example.starwars.viewmodel.FilmActivityViewModel
import java.util.*

class FilmActivity : AppCompatActivity() {
    private var mViewModel: FilmActivityViewModel? = null
    private var mFilmIcon: ImageView? = null
    private var mFilmTitleTextView: TextView? = null
    private var mFilmDateTextView: TextView? = null
    private var mFilmGenresTextView: TextView? = null
    private var mFilmOverviewTextView: TextView? = null
    private lateinit var mFilmToCrewTextView: TextView
    private var mAdapter: CrewAdapter? = null
    private lateinit var mRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_StarWars)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film)
        mFilmIcon = findViewById(R.id.film_icon)
        mFilmTitleTextView = findViewById(R.id.film_title)
        mFilmDateTextView = findViewById(R.id.film_date)
        mFilmGenresTextView = findViewById(R.id.film_genres)
        mFilmOverviewTextView = findViewById(R.id.film_overview)
        mFilmToCrewTextView = findViewById(R.id.film_to_full_crew)
        val id = intent.getIntExtra(FilmListActivity.FILM_ID_EXTRA_KEY, 0)
        mViewModel = ViewModelProviders.of(this).get(FilmActivityViewModel::class.java)
        mViewModel!!.init(id)
        mViewModel!!.filmLiveData!!.observe(this,
            { t -> setData(t) })
        mRecyclerView = findViewById(R.id.film_crew_rv)
        mAdapter = CrewAdapter(ArrayList(), this)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
        mFilmToCrewTextView.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@FilmActivity, CrewActivity::class.java)
            intent.putExtra(FilmListActivity.FILM_ID_EXTRA_KEY, id)
            startActivity(intent)
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun setData(film: Film?) {
        if (film != null) {
            if (!isInternetConnected) {
                Toast.makeText(this, FilmListActivity.CACHE_MESSAGE, Toast.LENGTH_SHORT).show()
            }
            StarWarsApp.context?.let {
                mFilmIcon?.let { it1 ->
                    Glide.with(it)
                        .load(IMAGE_BASE_URL + film.posterPath)
                        .error(R.drawable.ic_launcher_background)
                        .into(it1)
                }
            }
            mFilmTitleTextView!!.text = film.title
            mFilmDateTextView!!.text = film.releaseDate
            mFilmGenresTextView!!.text = Genre.convertToString(film.genres)
            mFilmOverviewTextView!!.text = film.overview
            mAdapter!!.setData(film.crews)
            mAdapter!!.notifyDataSetChanged()
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