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
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.starwars.StarWarsApp
import com.example.starwars.adapter.FilmsAdapter
import android.widget.TextView
import java.util.ArrayList

class FilmsAdapter(
    private var mFilms: ArrayList<Film>,
    context: Context?,
    filmClickListener: FilmClickListener
) : RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    private val filmClickListener: FilmClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.film_list_item, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val film = mFilms[position]
        holder.FilmTitleTextView.text = film.title
        holder.FilmInfoTextView.text = "Average vote: " + film.voteAverage
        StarWarsApp.context?.let {
            Glide.with(it)
                .load(IMAGE_BASE_URL + film.posterPath)
                .error(R.drawable.ic_launcher_background)
                .into(holder.FilmIconImageView)
        }
    }

    override fun getItemCount(): Int {
        return mFilms.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var FilmTitleTextView: TextView
        var FilmInfoTextView: TextView
        var FilmIconImageView: ImageView
        override fun onClick(v: View) {
            filmClickListener.onItemClick(adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
            FilmTitleTextView = itemView.findViewById(R.id.film_item_title)
            FilmInfoTextView = itemView.findViewById(R.id.film_item_info)
            FilmIconImageView = itemView.findViewById(R.id.film_item_icon)
        }
    }

    interface FilmClickListener {
        fun onItemClick(position: Int)
    }

    fun setFilms(filmList: ArrayList<Film>) {
        mFilms = filmList
        notifyDataSetChanged()
    }

    companion object {
        private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342/"
    }

    init {
        inflater = LayoutInflater.from(context)
        this.filmClickListener = filmClickListener
    }
}