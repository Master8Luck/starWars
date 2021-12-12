package com.example.starwars.adapter

import android.content.Context
import com.example.starwars.model.Film
import com.example.starwars.adapter.FilmsAdapter.FilmClickListener
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.starwars.R
import androidx.annotation.RequiresApi
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.starwars.StarWarsApp
import com.example.starwars.adapter.FilmsAdapter
import android.widget.TextView
import com.example.starwars.activity.FilmListActivity.Companion.TAG
import com.example.starwars.databinding.FilmListItemBinding
import java.util.ArrayList

class FilmsAdapter(
    private var mFilms: ArrayList<Film>,
    filmClickListener: FilmClickListener
) : RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {
    private val filmClickListener: FilmClickListener = filmClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FilmListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val film = mFilms[position]
            binding.filmItemTitle.text = film.title
            // TODO move "Average vote: " to string resources
            binding.filmItemInfo.text = "Average vote: " + film.voteAverage

            Glide.with(holder.itemView.context)
                .load(IMAGE_BASE_URL + film.posterPath)
                .error(R.drawable.ic_launcher_background)
                .into(binding.filmItemIcon)

            holder.itemView.setOnClickListener { filmClickListener.onItemClick(bindingAdapterPosition) }
        }
    }

    override fun getItemCount(): Int {
        return mFilms.size
    }

    inner class ViewHolder(val binding: FilmListItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    interface FilmClickListener {
        fun onItemClick(position: Int)
    }

    fun setFilms(filmList: ArrayList<Film>) {
        mFilms = filmList
        Log.d(TAG, "setFilms: inside adapter")
        notifyDataSetChanged()
    }

    companion object {
        private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342/"
    }
}